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

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class Main {
    public static ArrayList<BlockManager> blockManagers = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        String filename = "out/BlockManager/BM1/0.meta";
        System.out.println(FileUtils.getMetaInfo(filename).get("checksum"));
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


//        BlockManager blockManager1 = new BlockManagerImpl("BM1");
//        BlockManager blockManager2 = new BlockManagerImpl("BM2");
//        BlockManager blockManager3 = new BlockManagerImpl("BM3");
//        blockManagers.add(blockManager1);
//        blockManagers.add(blockManager2);
//        blockManagers.add(blockManager3);
//        FileManager fileManager = new FileManagerImpl();
//        Id id = new IdImpl("hello");
//        File file = new FileImpl(fileManager, id);
//        String strstr = "Hello World!";
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
