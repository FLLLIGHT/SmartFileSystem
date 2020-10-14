package utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

    public static void create(String filename) {
        try {
            File file = new File(filename);
            if(file.createNewFile()){
                System.out.println("File created: " + file.getName());
            } else{
                System.out.println("File already exists.");
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void write(String filename, byte[] bytes){
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            fos.write(bytes);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
