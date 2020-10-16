package utils;

import java.io.*;
import java.util.HashMap;
import java.util.Properties;

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

    public static void update(String filename, byte[] bytes){
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            fos.write(bytes, 0, bytes.length);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static byte[] read(String filename, int off, int len){
        byte[] data = new byte[len];
        try (FileInputStream fis = new FileInputStream(filename)) {
            int n = fis.read(data, off, len);
        }catch (IOException e){
            e.printStackTrace();
        }
        return data;
    }

    public static byte[] readAll(String filename){
        int len = -1;
        try (FileInputStream fis = new FileInputStream(filename)) {
            len = fis.available();
        }catch (IOException e){
            e.printStackTrace();
        }
        return read(filename, 0, len);
    }

    public static String getProperty(String propertyKey){
        Properties properties = new Properties();
        String propertyVal = "";
        try(InputStream is = FileUtils.class.getClassLoader().getResourceAsStream("file.properties");) {
            properties.load(is);
            propertyVal = properties.getProperty(propertyKey);
        } catch (IOException e){
            e.printStackTrace();
        }
        return propertyVal;
    }

    public static HashMap<String, String> getMetaInfo(String filename){
        String val = new String(FileUtils.readAll(filename));
        String[] valByLine = val.split("\n");
        HashMap<String, String> valMap = new HashMap<>();
        for(String valPerLine : valByLine){
            String[] nameAndVal = valPerLine.split(":");
            if(nameAndVal.length==1){
                valMap.put(nameAndVal[0], "");
            }else if(nameAndVal.length==2){
                valMap.put(nameAndVal[0], nameAndVal[1]);
            }else{
                //todo: 异常
                System.out.println("meta info error");
            }
        }
        return valMap;
    }

    public static void writeMetaInfo(){

    }
}
