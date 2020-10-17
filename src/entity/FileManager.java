package entity;

public interface FileManager {
    File getFile(Id fileId);
    File newFile(Id fileId);
    File copyFile(Id from, Id to);
    Id getId();
}
