package entity;

public interface FileManager {
    File getFile(Id fileId);
    File newFile(Id fileId);
    Id getId();
}
