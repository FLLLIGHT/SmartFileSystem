package utils;

import entity.Block;
import entity.File;
import entity.FileManager;
import entity.impl.FileImpl;
import entity.impl.IdImpl;

public class SmartUtils {
    public static void smartCat(String fileName, long startIndex, int length, FileManager fileManager){
        File file = fileManager.getFile(new IdImpl(fileName));
        if(length==-1) length = (int)file.size();
        file.move(startIndex ,File.MOVE_HEAD);
        System.out.println(new String(file.read(length)));
    }

    public static void smartHex(Block block){

    }

    public static void smartWrite(String fileName, long startIndex, FileManager fileManager, String data){
        File file = fileManager.getFile(new IdImpl(fileName));
        file.move(startIndex ,File.MOVE_HEAD);
        file.write(data.getBytes());
    }

    public static void smartCopy(String from, String to, FileManager fileManager){
        fileManager.copyFile(new IdImpl(from), new IdImpl(to));
    }
}
