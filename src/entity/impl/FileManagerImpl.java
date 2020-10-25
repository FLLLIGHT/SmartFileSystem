package entity.impl;

import entity.File;
import entity.FileManager;
import entity.Id;
import exception.ErrorCode;
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
        File file = fileManagerMap.get(fileId);
        if(file==null) throw new ErrorCode(ErrorCode.FILE_NOT_EXISTED);
        return file;
    }

    @Override
    public File newFile(Id fileId) throws ErrorCode{
        if(fileManagerMap.containsKey(fileId)) throw new ErrorCode(ErrorCode.FILE_ALREADY_EXISTED);
        File file = new FileImpl(this, fileId, true);
        fileManagerMap.put(fileId, file);
        return file;
    }

    @Override
    public File copyFile(Id from, Id to, FileManager fileManagerFrom) {
        File file = new FileImpl(fileManagerFrom, this, to, from.toString());
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
