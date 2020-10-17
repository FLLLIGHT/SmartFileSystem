package entity.impl;

import entity.File;
import entity.FileManager;
import entity.Id;

public class FileManagerImpl implements FileManager {
    Id id;

    public FileManagerImpl(Id id){
        this.id = id;
    }

    @Override
    public File getFile(Id fileId) {
        return null;
    }

    @Override
    public File newFile(Id fileId) {
        return null;
    }

    public Id getId(){
        return id;
    }
}
