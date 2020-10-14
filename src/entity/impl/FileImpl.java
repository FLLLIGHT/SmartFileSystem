package entity.impl;

import entity.File;
import entity.FileManager;
import entity.Id;

public class FileImpl implements File {
    @Override
    public Id getFileId() {
        return null;
    }

    @Override
    public FileManager getFileManager() {
        return null;
    }

    @Override
    public byte[] read(int length) {
        return new byte[0];
    }

    @Override
    public void write(byte[] b) {

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
}
