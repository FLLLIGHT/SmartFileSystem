package entity.impl;

import entity.BlockManager;
import entity.File;
import entity.FileManager;
import entity.Id;
import main.Main;
import utils.FileUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class FileImpl implements File {

    private FileManager fileManager;
    private Id id;
    private long currCursor;
    private final int MOVE_CURR = 0;
    private final int MOVE_HEAD = 1;
    private final int MOVE_TAIL = 2;

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
        return new byte[0];
    }

    //todo: 从文件的中间开始修改block，后面的全部需要改变
    @Override
    public void write(byte[] b) {
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
        return Integer.parseInt(FileUtils.getProperty("blockSize"));
    }

    private int getFileSize(){
        //todo: 从file meta中读文件大小
        return 0;
    }

    private void setFileMetaLogicBlock(int index){
        //todo: 设置file meta中的logic block数据，一行一行设置
    }

    //todo: 为什么每个block（除最后）都要拉满：方便索引，直接可以计算出位置，不然不能直接通过位置计算出哪个logic block中
}
