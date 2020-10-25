package utils;

import exception.ErrorCode;

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
            new ErrorCode(ErrorCode.IO_EXCEPTION).printStackTrace();
        }
    }

    public static void delete(String filename){
        File file = new File(filename);
        file.delete();
    }

    public static void write(String filename, byte[] bytes){
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            fos.write(bytes);
        }catch (IOException e){
            new ErrorCode(ErrorCode.IO_EXCEPTION).printStackTrace();
        }
    }

    public static void update(String filename, byte[] bytes){
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            fos.write(bytes, 0, bytes.length);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private static byte[] read(String filename, int off, int len){
        byte[] data = new byte[len];
        try (FileInputStream fis = new FileInputStream(filename)) {
            int n = fis.read(data, off, len);
        }catch (IOException e){
            e.printStackTrace();
        }
        return data;
    }

    public static byte[] readAll(String filename, String source){
        int len = -1;
        try (FileInputStream fis = new FileInputStream(filename)) {
            len = fis.available();
        }catch (IOException e){
            if(source.equals("block data")) throw new ErrorCode(ErrorCode.BLOCK_DATA_NOT_EXISTED);
            if(source.equals("id data")) throw new ErrorCode(ErrorCode.ID_DATA_NOT_EXISTED);
            if(source.equals("file meta")) throw new ErrorCode(ErrorCode.FILE_META_NOT_EXISTED);
            if(source.equals("block meta")) throw new ErrorCode(ErrorCode.BLOCK_META_NOT_EXISTED);
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
            throw new ErrorCode(ErrorCode.SETTING_FILE_ERROR);
        }
        if(propertyVal==null) throw new ErrorCode(ErrorCode.SETTING_FILE_ERROR);
        return propertyVal;
    }

    public static HashMap<String, String> getMetaInfo(String filename, String source){
        String val = new String(FileUtils.readAll(filename, source));
        String[] valByLine = val.split("\n");
        HashMap<String, String> valMap = new HashMap<>();
        for(String valPerLine : valByLine){
            String[] nameAndVal = valPerLine.split(":");
            if(nameAndVal.length==1){
                valMap.put(nameAndVal[0], "");
            }else if(nameAndVal.length==2){
                valMap.put(nameAndVal[0], nameAndVal[1]);
            }else{
                System.out.println("meta info error");
            }
        }
        return valMap;
    }

    public static boolean createDirectory(String filename){
        File folder = new File(filename);
        if(!folder.exists()||!folder.isDirectory()){
            return folder.mkdirs();
        }
        return false;
    }

    public static String[] list(String path){
        File folder = new File(path);
        return folder.list();
    }
}
