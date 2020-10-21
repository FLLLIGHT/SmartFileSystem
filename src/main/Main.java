package main;

import entity.BlockManager;
import entity.File;
import entity.FileManager;
import entity.Id;
import entity.impl.BlockManagerImpl;
import entity.impl.FileImpl;
import entity.impl.FileManagerImpl;
import entity.impl.IdImpl;
import utils.FileUtils;
import utils.SHA256Utils;
import utils.SmartUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class Main {
    public static HashMap<Id, BlockManager> blockManagers = new HashMap<>();
    public static HashMap<Id, FileManager> fileManagers = new HashMap<>();

    public static void main(String[] args) throws IOException {
        BlockManager blockManager1 = new BlockManagerImpl(new IdImpl("BM1"));
        BlockManager blockManager2 = new BlockManagerImpl(new IdImpl("BM2"));
        BlockManager blockManager3 = new BlockManagerImpl(new IdImpl("BM3"));
        blockManagers.put(new IdImpl("BM1"), blockManager1);
        blockManagers.put(new IdImpl("BM2"), blockManager2);
        blockManagers.put(new IdImpl("BM3"), blockManager3);
        FileManager fileManager = new FileManagerImpl(new IdImpl("FM1"));
        fileManagers.put(new IdImpl("FM1"), fileManager);

        Id id = new IdImpl("helloWorld");
        File file = fileManager.getFile(id);
        SmartUtils.smartCat(id.toString(), 0, -1, fileManager);
        System.out.println(file.size());
        SmartUtils.smartCat(id.toString(), 0, -1, fileManager);
        SmartUtils.smartCat(id.toString(), 0, -1, fileManager);
//        String add = "kkkkkkkkkkkkkkk";
//        file.move(14, File.MOVE_CURR);
//        file.write(add.getBytes());
//        SmartUtils.smartCat(id.toString(), 0, -1, fileManager);
    }
}
