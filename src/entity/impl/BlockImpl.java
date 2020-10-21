package entity.impl;

import entity.Block;
import entity.BlockManager;
import entity.Id;
import exception.ErrorCode;
import utils.FileUtils;
import utils.SHA256Utils;

public class BlockImpl implements Block {

    private final BlockManager blockManager;
    private final Id id;

    //索引已有的block
    public BlockImpl(BlockManager blockManager, Id id){
        this.blockManager = blockManager;
        this.id = id;
    }

    //初始化block（第一次持久化）
    public BlockImpl(BlockManager blockManager, Id id, byte[] data){
        this.blockManager = blockManager;
        this.id = id;
        writeBlock(blockManager.getId().toString(), ".meta", generateMeta(data).getBytes());
        writeBlock(blockManager.getId().toString(), ".data", data);
    }

    @Override
    public Id getIndexId() {
        return id;
    }

    @Override
    public BlockManager getBlockManager() {
        return blockManager;
    }

    //读data文件中的数据并判断checksum
    //如果checksum错误，则抛出异常
    @Override
    public byte[] read() throws ErrorCode {
        String prefix = "out";
        String filename = prefix +"/BlockManager/"+blockManager.getId().toString()+"/"+id.toString()+".data";
        byte[] data = FileUtils.readAll(filename, "block data");
        if(!checkChecksum(data)) throw new ErrorCode(ErrorCode.CHECKSUM_CHECK_FAILED);
        return data;
    }

    //从block的meta中获取当前block的实际size大小（通常，一个文件中，除了最后一个block，其他block的实际大小与文件meta信息中的block size大小相同）
    @Override
    public int blockSize() {
        String prefix = "out";
        String filename = prefix +"/BlockManager/"+blockManager.getId().toString()+"/"+id.toString()+".meta";
        return Integer.parseInt(FileUtils.getMetaInfo(filename, "block meta").get("size"));
    }

    //从文件的meta中获取当前block的checksum
    private String getChecksum(){
        String prefix = "out";
        String filename = prefix +"/BlockManager/"+blockManager.getId().toString()+"/"+id.toString()+".meta";
        return FileUtils.getMetaInfo(filename, "block meta").get("checksum");
    }

    //判断checksum是否相同
    private boolean checkChecksum(byte[] data){
        return getChecksum().equals(SHA256Utils.getSHA256(data));
    }

    //写入block的meta或data信息
    private void writeBlock(String blockManagerId, String suffix, byte[] content){
        String prefix = "out";
        String filename = prefix +"/BlockManager/"+blockManagerId+"/"+id.toString()+suffix;
        FileUtils.create(filename);
        FileUtils.write(filename, content);
    }

    private String generateMeta(byte[] data){
        return "size:"+data.length+"\n"+"checksum:"+ SHA256Utils.getSHA256(data);
    }
}
