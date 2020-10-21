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
        byte[] data = FileUtils.readAll(filename);
        if(!checkChecksum(data)) throw new ErrorCode(ErrorCode.CHECKSUM_CHECK_FAILED);
        return data;
    }

    @Override
    public int blockSize() {
        String prefix = "out";
        String filename = prefix +"/BlockManager/"+blockManager.getId().toString()+"/"+id.toString()+".meta";
        return Integer.parseInt(FileUtils.getMetaInfo(filename).get("size"));
    }

    private String getChecksum(){
        String prefix = "out";
        String filename = prefix +"/BlockManager/"+blockManager.getId().toString()+"/"+id.toString()+".meta";
        return FileUtils.getMetaInfo(filename).get("checksum");
    }

    private boolean checkChecksum(byte[] data){
        return getChecksum().equals(SHA256Utils.getSHA256(data));
    }

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
