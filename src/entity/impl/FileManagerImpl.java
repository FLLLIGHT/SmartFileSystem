package entity.impl;

import entity.File;
import entity.FileManager;
import entity.Id;
import utils.FileUtils;

import java.util.HashMap;

public class FileManagerImpl implements FileManager {
    private Id id;
    private HashMap<Id, File> fileManagerMap;

    public FileManagerImpl(Id id){
        this.id = id;
        fileManagerMap = new HashMap<>();

        String path = "out/FileManager/"+id.toString();
        if(!FileUtils.createDirectory(path)){
            indexFiles();
        }
    }

    @Override
    public File getFile(Id fileId) {
        return fileManagerMap.get(fileId);
    }

    @Override
    public File newFile(Id fileId) {
        File file = new FileImpl(this, fileId, true);
        fileManagerMap.put(fileId, file);
        return file;
    }

    @Override
    public File copyFile(Id from, Id to) {
        File file = new FileImpl(this, to, from.toString());
        fileManagerMap.put(to, file);
        return file;
    }

    public Id getId(){
        return id;
    }

    private void indexFiles(){
        String prefix = "out";
        String path = prefix +"/FileManager/"+id.toString();
        String[] filenames = FileUtils.list(path);
        for(String filename : filenames){
            Id id = new IdImpl(filename.substring(0, filename.lastIndexOf('.')));
            File file = new FileImpl(this, id);
            fileManagerMap.put(id, file);
        }
    }
}
