package utils;

import entity.Block;
import entity.File;
import entity.FileManager;
import entity.impl.FileImpl;
import entity.impl.IdImpl;

import java.util.Scanner;

public class SmartUtils {
    public static void smartCat(String fileName, FileManager fileManager){
        File file = fileManager.getFile(new IdImpl(fileName));
        int length = (int)(file.size() - file.pos());
        System.out.println(new String(file.read(length)));
//        从startIndex开始，读length长度的数据
//        File file = fileManager.getFile(new IdImpl(fileName));
//        if(length==-1) length = (int)file.size();
//        file.move(startIndex ,File.MOVE_HEAD);
//        System.out.println(new String(file.read(length)));
    }

    public static void smartHex(Block block){
        System.out.println(bytesToHex(block.read()));
    }

    public static void smartWrite(String fileName, int index, FileManager fileManager){
        File file = fileManager.getFile(new IdImpl(fileName));
        file.move(index, File.MOVE_HEAD);
        System.out.println("Please input the data you want to write:");
        Scanner scanner = new Scanner(System.in);
        String data = scanner.next();
        file.write(data.getBytes());
    }

    public static void smartCopy(String from, String to, FileManager fileManagerFrom, FileManager fileManagerTo){
        fileManagerTo.copyFile(new IdImpl(from), new IdImpl(to), fileManagerFrom);
    }

    public static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes){
        char[] hexChars = new char[bytes.length*2];
        for(int i=0; i<bytes.length; i++){
            int v = bytes[i] & 0xFF;
            hexChars[i*2] = HEX_ARRAY[v>>>4];
            hexChars[i*2+1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}
