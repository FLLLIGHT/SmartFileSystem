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
        return 0;
    }

    @Override
    public long move(long offset, int where) {
        return 0;
    }

    @Override
    public void close() {

    }

    @Override
    public long size() {
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
        return Integer.parseInt(FileUtils.getProperty("blockSize"));
    }
}
