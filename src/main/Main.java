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
//        String filename = "out/BlockManager/BM1/0.meta";
//        System.out.println(FileUtils.getMetaInfo(filename).get("checksum"));
//        String filename = "111.meta";
//        String val = new String(FileUtils.readAll(filename));
//        String[] s = val.split("\n");
//        HashMap<String, String> smap = new HashMap<>();
//        for(String ss : s){
//            String[] sss = ss.split(":");
//            if(sss.length==1){
//                smap.put(sss[0], "");
//            }else{
//                smap.put(sss[0], sss[1]);
//            }
//        }
//        System.out.println(smap.get("1"));


        //牛逼！！！
        BlockManager blockManager1 = new BlockManagerImpl(new IdImpl("BM1"));
        BlockManager blockManager2 = new BlockManagerImpl(new IdImpl("BM2"));
        BlockManager blockManager3 = new BlockManagerImpl(new IdImpl("BM3"));
        blockManagers.put(new IdImpl("BM1"), blockManager1);
        blockManagers.put(new IdImpl("BM2"), blockManager2);
        blockManagers.put(new IdImpl("BM3"), blockManager3);
        FileManager fileManager = new FileManagerImpl(new IdImpl("FM1"));
        fileManagers.put(new IdImpl("FM1"), fileManager);

        Id id = new IdImpl("caonima16");
        File file = fileManager.newFile(id);
        String s = "hello world!!";
        file.write(s.getBytes());
        FileImpl file1 = (FileImpl)file;
        System.out.println(new String(file1.readAll()));

        file.setSize(8);
        String add = "xxxxxx";
        file.move(0, File.MOVE_TAIL);
        file.write(add.getBytes());
        System.out.println(new String(file1.readAll()));
//        file1.move(2, 0);
//        String add = "zzzzzzzzz";
//        file1.write(add.getBytes());
//        System.out.println(new String(file1.readAll()));
//        SmartUtils.smartCopy("caonima3", "fuckyou3", fileManager);
//        SmartUtils.smartCat("fuckyou3",0 , -1, fileManager);
//        SmartUtils.smartCat("caonima3",0 , -1, fileManager);
//        SmartUtils.smartWrite("caonima3", 5, fileManager, "yyyyy");
//        SmartUtils.smartCat("caonima3",0 , -1, fileManager);
//        SmartUtils.smartCat("fuckyou3",0 , -1, fileManager);
//        Id id = new IdImpl("helloworld");
//        FileImpl file = new FileImpl(fileManager, id);
//        Id id2 = new IdImpl("helloworld");
//        System.out.println(id.equals(id2));
//        file.move(2, 1);
//        file.move(2, 0);

//        System.out.println(new String(file.read(3)));
//        System.out.println(new String(file.readAll()));
//        System.out.println(file.pos());
//        file.move(31,1);
//        String add = "长颈鹿";
//        file.write(add.getBytes());
//        System.out.println(new String(file.readAll()));

//
//        String strstr = "Hello World!!";
//        file.write(strstr.getBytes());

        //e7e7f78235aebae81f814ee0420607c9bbf4857e5476775de37f5a0da1f95cf3
//        String str = "Hello World!\nHH";
//        System.out.println(SHA256Utils.getSHA256(str.getBytes()));

//        String filename = "out/BlockManager/BM1/30.data";
//        FileUtils.create(filename);
//        FileUtils.write(filename, str.getBytes());
//        System.out.println(new String(FileUtils.readAll(filename)));

//        InputStream is = Main.class.getClassLoader().getResourceAsStream("file.properties");
//        Properties properties = new Properties();
//        properties.load(is);
//        String blockSize = properties.getProperty("blockSize");
//        System.out.println(blockSize);
    }
}
