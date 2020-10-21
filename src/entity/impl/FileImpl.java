package entity.impl;

import entity.*;
import exception.ErrorCode;
import main.Main;
import utils.FileUtils;
import utils.SmartUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileImpl implements File {

    private FileManager fileManager;
    private Id id;

    private long currCursor;
    private final int MOVE_CURR = 0;
    private final int MOVE_HEAD = 1;
    private final int MOVE_TAIL = 2;
    //todo: 如果在统一位置插入，则直接加在原来的后面
    //todo: key是插入的位置（cursor）
    //todo: 考虑在哪里初始化比较好
    //为什么每个block（除最后）都要拉满：方便索引，直接可以计算出位置，不然不能直接通过位置计算出哪个logic block中
    private byte[] buffer;

    //索引已有的file
    public FileImpl(FileManager fileManager, Id id){
        this.fileManager = fileManager;
        this.id = id;
        this.currCursor = 0;
    }

    //新建file，初始并持久化meta文件
    public FileImpl(FileManager fileManager, Id id, boolean newFile){
        this.fileManager = fileManager;
        this.id = id;
        this.currCursor = 0;
        HashMap<String, String> valMap = new HashMap<>();
        valMap.put("size", "0");
        //新建文件时的block size要从配置文件中读，之后从文件的meta中读，否则会冲突
        valMap.put("block size", FileUtils.getProperty("blockSize"));
        writeMeta(generateMeta(valMap).getBytes());
    }

    //复制文件
    public FileImpl(FileManager fileManager, Id id, String copyFrom){
        this.fileManager = fileManager;
        this.id = id;
        this.currCursor = 0;
        System.out.println(fileManager.getFile(new IdImpl(copyFrom)));
        HashMap<String, String> valMap = ((FileImpl)fileManager.getFile(new IdImpl(copyFrom))).getMetaInfo();
        writeMeta(generateMeta(valMap).getBytes());
    }

    @Override
    public Id getFileId() {
        return id;
    }

    @Override
    public FileManager getFileManager() {
        return fileManager;
    }

    //从startIndex的位置开始读length个字节的数据
    private byte[] read(int length, long startIndex, boolean buffer){
        //如果是从buffer读，则直接读buffer即可。
        if(buffer){
            byte[] data = new byte[length];
            System.arraycopy(this.buffer, (int)startIndex, data, 0, length);
            return data;
        }

        int blockSize = getBlockSize();
        int startBlockIndex = (int)(startIndex / blockSize);
        int endBlockIndex = (int)((startIndex + length - 1) / blockSize);
        HashMap<String, String> metaInfo = getMetaInfo();
        byte[] data = new byte[length];
        int index = 0;
        for(int i=startBlockIndex; i<=endBlockIndex; i++){
            //计算第一个block的开始位置
            int start = 0;
            if(i==startBlockIndex){
                start = (int)(startIndex % blockSize);
            }
            //计算最后一个block的结束位置
            if(i==endBlockIndex&&(int)((length+startIndex) % blockSize)!=0){
                blockSize = (int)((length+startIndex) % blockSize);
            }
            //其他block全部都读
            List<String[]> logicBlocks = getLogicBlocks(metaInfo.get(i+""));
            //todo: 若读取失败，读取下一个
            for(String[] logicBlock : logicBlocks){
                try {
                    BlockManager blockManager = getBlockManagerById(logicBlock[0]);
                    Block block = blockManager.getBlock(new IdImpl(logicBlock[1]));
                    SmartUtils.smartHex(block);
                    System.arraycopy(block.read(), start, data, index, blockSize-start);
                    break;
                } catch (ErrorCode errorCode){
                    errorCode.printStackTrace();
                }
            }
            index += (blockSize - start);
        }
        return data;
    }

    //直接从当前指针位置开始读
    @Override
    public byte[] read(int length) {
        if(getBufferSetting().equals("yes")&&buffer==null){
            //读入buffer
            readAll();
            return read(length, currCursor, true);
        }else if(getBufferSetting().equals("yes")){
            //直接从buffer读
            return read(length, currCursor, true);
        }
        return read(length, currCursor, false);
    }

    public byte[] readAll(){
        if(getBufferSetting().equals("yes")&&buffer==null){
            //读入buffer
            buffer = read(getFileSize(), 0, false);
            return buffer;
        }else if(getBufferSetting().equals("yes")){
            //直接从buffer读
            return buffer;
        }
        return read(getFileSize(), 0, false);
    }

    //todo: 从文件的中间开始修改block，后面的全部需要改变
    @Override
    public void write(byte[] b) {
        if(getBufferSetting().equals("yes")){
            if(buffer==null) readAll();
            int newFileSize = buffer.length + b.length;
            byte[] newBuffer = new byte[newFileSize];
            System.arraycopy(buffer, 0, newBuffer, 0, (int)currCursor);
            System.arraycopy(b, 0, newBuffer, (int)currCursor, b.length);
            System.arraycopy(buffer, (int)currCursor, newBuffer, (int)currCursor+b.length, buffer.length-(int)currCursor);
            buffer = newBuffer;
        }else if(getBufferSetting().equals("no")){
            int blockSize = getBlockSize();
            int fileSize = getFileSize();
            //从第blockUnchangedNumber个block(logic block)开始，就需要改了
            int blockUnchangedNumber = (int)(currCursor / blockSize);
            //文件的总大小-不需要改的大小+新增的大小
            int totalSize = fileSize - blockUnchangedNumber * blockSize + b.length;
            //changedByte: 要更新的所有data
            byte[] changedData = new byte[totalSize];
            if(fileSize!=0) {
                /////////////////////////////////
                move(blockUnchangedNumber * blockSize, MOVE_HEAD);
                byte[] oldData = read(fileSize - blockUnchangedNumber * blockSize);
                System.arraycopy(oldData, 0, changedData, 0, (int)currCursor%blockSize);
                System.arraycopy(b, 0, changedData, (int)currCursor%blockSize, b.length);
                System.arraycopy(oldData, (int)currCursor%blockSize, changedData, (int)currCursor%blockSize+b.length, oldData.length-(int)currCursor%blockSize);
            }else{
                System.arraycopy(b, 0, changedData, 0, b.length);
            }
            write(changedData, blockUnchangedNumber);
        }
    }

    private void write(byte[] b, int startIndex){
        int duplicationNumber = getDuplicationNumber();
        int blockSize = getBlockSize();
        int blockNumber = (b.length-1) / blockSize + 1;
        int from = 0;
        int to;

        HashMap<String, String> valMap = getMetaInfo();
        for(int i=startIndex; i<blockNumber+startIndex; i++){
            to = Math.min(from + blockSize, b.length);
            StringBuilder sb = new StringBuilder();
            for(int j=0; j<duplicationNumber; j++){
                BlockManager blockManager = getRandomBlockManager();
                Block block = blockManager.newBlock(Arrays.copyOfRange(b, from, to));
                sb.append("[").append(blockManager.getId().toString()).append(",")
                        .append(block.getIndexId().toString()).append("]");
            }
            valMap.put(i+"", sb.toString());
            from += blockSize;
        }
        valMap.put("size", b.length+startIndex*blockSize+"");
        //去掉后面不需要的logic block
        int count = blockNumber + startIndex;
        while(valMap.get(count+"")!=null){
            valMap.remove(count+"");
            count++;
        }
        String metaInfo = generateMeta(valMap);
        writeMeta(metaInfo.getBytes());
    }

    //更新meta数据
    private void writeMeta(byte[] content){
        String prefix = "out";
        String filename = prefix +"/FileManager/"+fileManager.getId().toString()+"/"+id.toString()+".meta";
        FileUtils.delete(filename);
        FileUtils.create(filename);
        FileUtils.write(filename, content);
    }

    private String getBufferSetting(){
        return FileUtils.getProperty("buffer");
    }

    @Override
    public long pos() {
        return move(0, MOVE_CURR);
    }

    @Override
    public long move(long offset, int where) {
        //todo: 异常处理，指针位置大于文件大小
        if(where==MOVE_CURR) {
            currCursor += offset;
        } else if(where==MOVE_HEAD) {
            currCursor = offset;
        } else if(where==MOVE_TAIL) {
            currCursor = getFileSize() + offset;
        }
        return currCursor;
    }

    @Override
    public void close() {
        write(buffer, 0);
    }

    @Override
    public long size() {
        String prefix = "out";
        String filename = prefix +"/FileManager/"+fileManager.getId().toString()+"/"+id.toString()+".meta";
        return Long.parseLong(FileUtils.getMetaInfo(filename).get("size"));
    }

    @Override
    public void setSize(long newSize) {
        long oldSize = size();
        if(newSize>oldSize){
            move(0, MOVE_TAIL);
            write(new byte[(int)(newSize-oldSize)]);
        }else if(newSize<oldSize){
            int blockSize = getBlockSize();
            int fileSize = getFileSize();
            //从第blockUnchangedNumber个block(logic block)开始，就需要改了
            int blockUnchangedNumber = (int)(newSize / blockSize);
            //文件的总大小-不需要改的大小+新增的大小
            int totalSize = (int)(newSize % blockSize);
            //changedByte: 要更新的所有data
            byte[] changedData = new byte[totalSize];
            ////////////////////////////////////
            move(blockUnchangedNumber * blockSize, MOVE_HEAD);
            byte[] oldData = read(Math.min(blockSize, fileSize-blockUnchangedNumber * blockSize));
            System.arraycopy(oldData, 0, changedData, 0, totalSize);
            write(changedData, blockUnchangedNumber);
        }
    }

    //获取配置文件要求的副本数量
    private int getDuplicationNumber(){
        return Integer.parseInt(FileUtils.getProperty("duplicationNumber"));
    }

    //当前配置文件中的block size大小，不一定是本文件的block size大小，本文件的block size大小要从meta读
    private int getBlockSize() {
        String prefix = "out";
        String filename = prefix +"/FileManager/"+fileManager.getId().toString()+"/"+id.toString()+".meta";
        return Integer.parseInt(FileUtils.getMetaInfo(filename).get("block size"));
    }

    //从file meta中读文件大小
    private int getFileSize(){
        String prefix = "out";
        String filename = prefix +"/FileManager/"+fileManager.getId().toString()+"/"+id.toString()+".meta";
        return Integer.parseInt(FileUtils.getMetaInfo(filename).get("size"));
    }

    //获取解析后的meta文件
    private HashMap<String, String> getMetaInfo(){
        String prefix = "out";
        String filename = prefix +"/FileManager/"+fileManager.getId().toString()+"/"+id.toString()+".meta";
        return FileUtils.getMetaInfo(filename);
    }

    private String generateMeta(HashMap<String, String> valMap){
        StringBuilder sb = new StringBuilder();
        sb.append("size:").append(valMap.get("size")).append("\n");
        sb.append("block size:").append(valMap.get("block size")).append("\n");
        sb.append("logic block:").append("\n");
        int count = 0;
        while(valMap.get(""+count)!=null){
            sb.append(count).append(":").append(valMap.get(""+count)).append("\n");
            count++;
        }
        return sb.toString();
    }

    //解析logic blocks
    //例如：
    // 输入[BM1,2][BM2,23][BM3,17]
    // 输出一个ArrayList，其中有3个String数组，每个数组的第一个元素是Block Manager的id，第二个元素是Block的id
    private List<String[]> getLogicBlocks(String logicBlocks){
        List<String[]> list = new ArrayList<>();
        //解析[]中的内容
        Matcher matcher = Pattern.compile("\\[([^\\]]+)").matcher(logicBlocks);
        int pos = -1;
        while (matcher.find(pos+1)){
            pos = matcher.start();
            String[] one = matcher.group(1).split(",");
            list.add(one);
        }
        return list;
    }

    //从系统维护的block manager的列表中找到对应的block manager
    private BlockManager getBlockManagerById(String id){
        return Main.blockManagers.get(new IdImpl(id));
    }

    //从系统维护的block manager的列表中随机找一个block manager
    private BlockManager getRandomBlockManager(){
        Random generator = new Random();
        Object[] blockManagers = Main.blockManagers.values().toArray();
        return (BlockManager)blockManagers[generator.nextInt(blockManagers.length)];
    }
}
