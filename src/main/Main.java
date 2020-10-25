package main;

import entity.BlockManager;
import entity.File;
import entity.FileManager;
import entity.Id;
import entity.impl.BlockManagerImpl;
import entity.impl.FileImpl;
import entity.impl.FileManagerImpl;
import entity.impl.IdImpl;
import exception.ErrorCode;
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

    public static void main(String[] args) throws IOException, InterruptedException {
        BlockManager blockManager1 = new BlockManagerImpl(new IdImpl("BM1"));
        BlockManager blockManager2 = new BlockManagerImpl(new IdImpl("BM2"));
        BlockManager blockManager3 = new BlockManagerImpl(new IdImpl("BM3"));
        blockManagers.put(new IdImpl("BM1"), blockManager1);
        blockManagers.put(new IdImpl("BM2"), blockManager2);
        blockManagers.put(new IdImpl("BM3"), blockManager3);
        FileManager fileManager1 = new FileManagerImpl(new IdImpl("FM1"));
        FileManager fileManager2 = new FileManagerImpl(new IdImpl("FM2"));
        fileManagers.put(new IdImpl("FM1"), fileManager1);
        fileManagers.put(new IdImpl("FM2"), fileManager2);

//        byte[] enp = new byte[]{0x00,0x30,0x31,0x00,0x41};
//        System.out.println(new String(enp));

//        Id id = new IdImpl("file1");
//        File file = fileManager1.getFile(id);
//        SmartUtils.smartWrite("file1", 0, fileManager1);
//        file.move(0, File.MOVE_HEAD);
//        SmartUtils.smartCat("file1", fileManager1);
//        file.setSize(20);
//        System.out.println(file.size());
//        SmartUtils.smartWrite("file1", 15, fileManager1);
//

//        SmartUtils.smartCat("file1", fileManager1);

//        SmartUtils.smartCopy("file1", "file2", fileManager1, fileManager2);
//        File file2 = fileManager2.getFile(new IdImpl("file2"));
//        SmartUtils.smartWrite("file2", 10, fileManager2);
//        file.move(0, File.MOVE_HEAD);
//        file2.move(0, File.MOVE_HEAD);
//        SmartUtils.smartCat("file2", fileManager2);
//        SmartUtils.smartCat("file1", fileManager1);

//        SmartUtils.smartCat("file1", fileManager1);

//        Id id = new IdImpl("file2");
//        File file2 = fileManager2.getFile(id);
//        file2.setSize(7);
//        file2.move(0, File.MOVE_HEAD);
//        SmartUtils.smartCat("file2", fileManager2);
//        file2.move(5, File.MOVE_HEAD);
//        SmartUtils.smartCat("file2", fileManager2);
//        file.close();
//        file2.close();
    }
}
