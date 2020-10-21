package entity.impl;

import entity.Block;
import entity.BlockManager;
import entity.Id;
import exception.ErrorCode;
import utils.FileUtils;
import utils.SmartUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class BlockManagerImpl implements BlockManager {
    private Id id;
    private HashMap<Id, Block> blockManagerMap;

    public BlockManagerImpl(Id id){
        this.id = id;
        blockManagerMap = new HashMap<>();

        //如果是新的Block Manager，则初始化id计数并持久化
        String path = "out/BlockManager/"+id.toString();
        if(FileUtils.createDirectory(path)){
            generateIdCount();
        }else{
            //如果不是新的，则对原有的block建立索引，将索引拉至内存中，便于之后查找，不必每次都读文件/文件名
            indexBlocks();
        }
    }

    //从该manager管理的map中找到block并返回
    @Override
    public Block getBlock(Id indexId) {
        Block block = blockManagerMap.get(indexId);
        if (block==null) throw new ErrorCode(ErrorCode.BLOCK_NOT_EXISTED);
        return block;
    }

    //新建block并加到该manager管理的map中
    @Override
    public Block newBlock(byte[] b) {
        String idStr = getAndUpdateNextAvailableId();
        Id id = new IdImpl(idStr);
        if(blockManagerMap.containsKey(id)) throw new ErrorCode(ErrorCode.BLOCK_ALREADY_EXISTED);
        Block block = new BlockImpl(this, id, b);
        blockManagerMap.put(id ,block);
        return block;
    }

    @Override
    public Block newEmptyBlock(int blockSize) {
        return newBlock(new byte[blockSize]);
    }

    public Id getId(){
        return id;
    }

    //获取并更新该manager的下一个可用id
    private String getAndUpdateNextAvailableId(){
        String prefix = "out";
        String filename = prefix +"/BlockManager/"+id.toString()+"/id.data";
        String id = new String(FileUtils.readAll(filename, "id data"));
        FileUtils.update(filename, ((Integer.parseInt(id)+1)+"").getBytes());
        return id;
    }

    //获取该manager当前的下一个可用id
    private String getAvailableId(){
        String prefix = "out";
        String filename = prefix +"/BlockManager/"+id.toString()+"/id.data";
        return new String(FileUtils.readAll(filename, "id data"));
    }

    //初始化可用id，并持久化
    private void generateIdCount(){
        String prefix = "out";
        String filename = prefix +"/BlockManager/"+id.toString()+"/id.data";
        FileUtils.create(filename);
        String content = "0";
        FileUtils.write(filename, content.getBytes());
    }

    //对block manager内的所有block建立索引
    private void indexBlocks(){
        int n = Integer.parseInt(getAvailableId()) - 1;
        for(int i=0; i<=n; i++){
            Id id = new IdImpl(i+"");
            Block block = new BlockImpl(this, id);
            blockManagerMap.put(id, block);
        }
    }
}
