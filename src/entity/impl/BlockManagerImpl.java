package entity.impl;

import entity.Block;
import entity.BlockManager;
import entity.Id;
import utils.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class BlockManagerImpl implements BlockManager {
    private Id id;
    private HashMap<Id, Block> blockManagerMap;

    public BlockManagerImpl(String idStr){
        this.id = new IdImpl(idStr);
        //todo: 也不一定要生成，如果是索引时就不需要生成，生成新文件夹时需要生成
//        generateIdCount();
        blockManagerMap = new HashMap<>();
        indexBlocks();
    }

    @Override
    public Block getBlock(Id indexId) {
        return blockManagerMap.get(indexId);
    }

    @Override
    public Block newBlock(byte[] b) {
        int blockSize = getBlockSize();
        String idStr = getAndUpdateNextAvailableId();
        Id id = new IdImpl(idStr);
        Block block = new BlockImpl(this, id, blockSize, b);
        blockManagerMap.put(id ,block);
        return block;
    }

    @Override
    public Block newEmptyBlock(int blockSize) {
        return null;
    }

    public Id getId(){
        return id;
    }

    private int getBlockSize() {
        return Integer.parseInt(FileUtils.getProperty("blockSize"));
    }

    //持久化该manager的下一个可用id
    private String getAndUpdateNextAvailableId(){
        String prefix = "out";
        String filename = prefix +"/BlockManager/"+id.toString()+"/id.data";
        String id = new String(FileUtils.readAll(filename));
        FileUtils.update(filename, ((Integer.parseInt(id)+1)+"").getBytes());
        return id;
    }

    private String getAvailableId(){
        String prefix = "out";
        String filename = prefix +"/BlockManager/"+id.toString()+"/id.data";
        return new String(FileUtils.readAll(filename));
    }

    private void generateIdCount(){
        String prefix = "out";
        String filename = prefix +"/BlockManager/"+id.toString()+"/id.data";
        FileUtils.create(filename);
        String content = "0";
        FileUtils.write(filename, content.getBytes());
    }

    private void indexBlocks(){
        int n = Integer.parseInt(getAvailableId()) - 1;
        for(int i=0; i<=n; i++){
            Id id = new IdImpl(i+"");
            Block block = new BlockImpl(this, id);
            blockManagerMap.put(id, block);
        }
    }

//    public static void main(String args[]) throws IOException {
//        BlockManagerImpl blockManager = new BlockManagerImpl("a");
//        System.out.println(blockManager.getBlockSize());
//    }
}
