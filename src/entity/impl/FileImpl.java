package entity.impl;

import entity.*;
import main.Main;
import utils.FileUtils;

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
    private HashMap<Integer, Byte[]> buffer = new HashMap<>();

    public FileImpl(FileManager fileManager, Id id){
        this.fileManager = fileManager;
        this.id = id;
        this.currCursor = 0;
    }

    @Override
    public Id getFileId() {
        return id;
    }

    @Override
    public FileManager getFileManager() {
        return fileManager;
    }

    @Override
    public byte[] read(int length) {
        int blockSize = getBlockSize();
        int startBlockIndex = (int)(currCursor / blockSize);
        int endBlockIndex = (int)((currCursor + length - 1) / blockSize);
        HashMap<String, String> metaInfo = getMetaInfo();
        byte[] data = new byte[length];
        int index = 0;
        for(int i=startBlockIndex; i<=endBlockIndex; i++){
            if(i==endBlockIndex&&length%blockSize!=0){
                blockSize = length % blockSize;
            }
            List<String[]> logicBlocks = getLogicBlocks(metaInfo.get(i+""));
            //todo: 若读取失败，读取下一个
            for(String[] logicBlock : logicBlocks){

                BlockManager blockManager = getBlockManagerById(logicBlock[0]);
                Block block = blockManager.getBlock(new IdImpl(logicBlock[1]));
                System.arraycopy(block.read(), 0, data, index, blockSize);
                break;
            }
            index += blockSize;
        }
        return data;
    }

    //todo: 从文件的中间开始修改block，后面的全部需要改变
    @Override
    public void write(byte[] b) {
//
//        if(getBufferSetting().equals("yes")){
//            //写入hashmap中
//        }else if(getBufferSetting().equals("no")){
//            int blockSize = getBlockSize();
//            //从第blockUnchangedNumber个block(logic block)开始，就需要改了
//            int blockUnchangedNumber = (int)(currCursor / blockSize);
//            int totalSize = getFileSize() - blockUnchangedNumber * blockSize + b.length;
//            byte[] changedByte = new byte[totalSize];
//
//        }
//

        int blockManagerNumber = Main.blockManagers.size();
        int duplicationNumber = getDuplicationNumber();
        int blockSize = getBlockSize();
        int blockNumber = (b.length-1) / blockSize + 1;
        int from = 0;
        int to;
        for(int i=0; i<blockNumber; i++){
            to = Math.min(from + blockSize, b.length);
            for(int j=0; j<duplicationNumber; j++){
                int blockManagerIndex = getRandomNumber(blockManagerNumber);
                BlockManager blockManager = Main.blockManagers.get(blockManagerIndex);
                blockManager.newBlock(Arrays.copyOfRange(b, from, to));
            }
            from += blockSize;
        }
    }

    public void write(byte[] b, int startIndex){

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
            currCursor = getBlockSize() - 1 + offset;
        }
        return currCursor;
    }

    @Override
    public void close() {

    }

    @Override
    public long size() {
        //todo: 从meta中读文件大小
        return 0;
    }

    @Override
    public void setSize(long newSize) {

    }

    private int getRandomNumber(int range){
        Random random = new Random();
        //range = 10 is 0~9
        return random.nextInt(range);
    }

    private int getDuplicationNumber(){
        return Integer.parseInt(FileUtils.getProperty("duplicationNumber"));
    }

    private int getBlockSize() {
        //todo: 当前配置文件中的block size大小，不一定是本文件的block size大小，本文件的block size大小要从meta读
        String prefix = "out";
        String filename = prefix +"/FileManager/"+fileManager.getId().toString()+"/"+id.toString()+".meta";
        return Integer.parseInt(FileUtils.getMetaInfo(filename).get("block size"));
    }

    private int getFileSize(){
        //todo: 从file meta中读文件大小
        String prefix = "out";
        String filename = prefix +"/FileManager/"+fileManager.getId().toString()+"/"+id.toString()+".meta";
        return Integer.parseInt(FileUtils.getMetaInfo(filename).get("size"));
    }

    private HashMap<String, String> getMetaInfo(){
        String prefix = "out";
        String filename = prefix +"/FileManager/"+fileManager.getId().toString()+"/"+id.toString()+".meta";
        return FileUtils.getMetaInfo(filename);
    }

    private void setFileMetaLogicBlock(int index){
        //todo: 设置file meta中的logic block数据，一行一行设置
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

    private List<String[]> getLogicBlocks(String logicBlocks){
        List<String[]> list = new ArrayList<>();
        Matcher matcher = Pattern.compile("\\[([^\\]]+)").matcher(logicBlocks);
        int pos = -1;
        while (matcher.find(pos+1)){
            pos = matcher.start();
            String[] one = matcher.group(1).split(",");
            list.add(one);
        }
        return list;
    }

    private BlockManager getBlockManagerById(String id){
        for(BlockManager blockManager : Main.blockManagers){
            if(blockManager.getId().toString().equals(id)){
                return blockManager;
            }
        }
        return null;
    }

    //todo: 为什么每个block（除最后）都要拉满：方便索引，直接可以计算出位置，不然不能直接通过位置计算出哪个logic block中

//    public static void main(String args[]){
//        String s = "[\"bm-01\",13][\"bm-02\",14][\"bm-03\",20]";
//        List<String[]> list = getLogicBlocks(s);
//        for(String[] ls : list){
//            for(String ss : ls){
//                System.out.println(ss);
//            }
//        }
//    }
}
