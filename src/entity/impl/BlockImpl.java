package entity.impl;

import entity.Block;
import entity.BlockManager;
import entity.Id;
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

    @Override
    public byte[] read() {
        //todo: 读data数据
        //todo: 判断checksum
        String prefix = "out";
        String filename = prefix +"/BlockManager/"+blockManager.getId().toString()+"/"+id.toString()+".data";
        return FileUtils.readAll(filename);
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
