package entity.impl;

import entity.Block;
import entity.BlockManager;
import entity.Id;
import utils.FileUtils;
import utils.SHA256Utils;

public class BlockImpl implements Block {

    private final BlockManager blockManager;
    private final Id id;

    public BlockImpl(BlockManager blockManager, Id id){
        this.blockManager = blockManager;
        this.id = id;
    }

    public BlockImpl(BlockManager blockManager, Id id, int blockSize, byte[] data){
        this.blockManager = blockManager;
        this.id = id;
        writeBlock(blockManager.getId().toString(), ".meta", generateMeta(data, blockSize).getBytes());
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
        //todo: 不能存在这里，要放在meta里待读取
        return 0;
    }

    private void writeBlock(String blockManagerId, String suffix, byte[] content){
        String prefix = "out";
        String filename = prefix +"/BlockManager/"+blockManagerId+"/"+id.toString()+suffix;
        FileUtils.create(filename);
        FileUtils.write(filename, content);
    }

    private String generateMeta(byte[] data, int blockSize){
        return "size:"+blockSize+"\n"+"checksum:"+ SHA256Utils.getSHA256(data);
    }
}
