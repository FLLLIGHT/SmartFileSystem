package entity.impl;

import entity.Block;
import entity.BlockManager;
import entity.Id;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

public class BlockManagerImpl implements BlockManager {
    private Id id;
    private HashMap<Id, Block> blockManagerMap;

    public BlockManagerImpl(String idStr){
        this.id = new IdImpl(idStr);
        blockManagerMap = new HashMap<>();
    }

    @Override
    public Block getBlock(Id indexId) {
        return blockManagerMap.get(indexId);
    }

    @Override
    public Block newBlock(byte[] b) {
        int blockSize = getBlockSize();
        String idStr = "";
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
        Properties properties = new Properties();
        String blockSize = "";
        try(InputStream is = this.getClass().getClassLoader().getResourceAsStream("file.properties");) {
            properties.load(is);
            blockSize = properties.getProperty("blockSize");
        } catch (IOException e){
            e.printStackTrace();
        }
        return Integer.parseInt(blockSize);
    }

//    public static void main(String args[]) throws IOException {
//        BlockManagerImpl blockManager = new BlockManagerImpl("a");
//        System.out.println(blockManager.getBlockSize());
//    }
}
