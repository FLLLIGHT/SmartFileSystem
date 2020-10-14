package entity;

public interface File {
    int MOVE_CURR = 0;
    int MOVE_HEAD = 1;
    int MOVE_TAIL = 2;
    Id getFileId();
    FileManager getFileManager();
    byte[] read(int length);
    void write(byte[] b);
    default long pos(){
        return move(0, MOVE_CURR);
    }
    //返回move后的位置
    long move(long offset, int where);

    //buffer
    void close();
    long size();
    void setSize(long newSize);
}
