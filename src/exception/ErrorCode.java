package exception;

import java.util.HashMap;
import java.util.Map;

public class ErrorCode extends RuntimeException{
    public static final int IO_EXCEPTION = 1;
    public static final int CHECKSUM_CHECK_FAILED = 2;
    public static final int FILE_ALREADY_EXISTED = 3;
    public static final int BLOCK_DATA_NOT_EXISTED = 4;
    public static final int ID_DATA_NOT_EXISTED = 5;
    public static final int FILE_META_NOT_EXISTED = 6;
    public static final int FILE_NOT_EXISTED = 7;
    public static final int BLOCK_NOT_EXISTED = 8;
    public static final int BLOCK_META_NOT_EXISTED = 9;
    public static final int SETTING_FILE_ERROR = 10;
    public static final int BLOCK_ALREADY_EXISTED = 11;
    public static final int FILE_DATA_DAMAGED = 12;
    public static final int MOVE_OUT_OF_BOUNDARY = 13;
    public static final int READ_OUT_OF_BOUNDARY = 14;

    public static final int UNKNOWN = 1000;

    private static final Map<Integer, String> ErrorCodeMap = new HashMap<>();

    static {
        ErrorCodeMap.put(IO_EXCEPTION, "IO exception");
        ErrorCodeMap.put(CHECKSUM_CHECK_FAILED, "block checksum check failed");

        ErrorCodeMap.put(FILE_ALREADY_EXISTED, "this file has already been created in this file manager");
        ErrorCodeMap.put(BLOCK_ALREADY_EXISTED, "this block has already been created in this block manager, there maybe some wrong with id data");


        //试图寻找本该存在的数据，但是不存在
        ErrorCodeMap.put(BLOCK_DATA_NOT_EXISTED, "this block data is not existed");
        ErrorCodeMap.put(ID_DATA_NOT_EXISTED, "this id data is not existed");
        ErrorCodeMap.put(FILE_META_NOT_EXISTED, "this file meta is not existed");

        //试图寻找本就不存在的数据
        ErrorCodeMap.put(FILE_NOT_EXISTED, "this file is not existed");
        ErrorCodeMap.put(BLOCK_NOT_EXISTED, "this block is not existed");

        //试图寻找本该存在的数据，但是不存在
        ErrorCodeMap.put(BLOCK_META_NOT_EXISTED, "this block meta is not existed");

        ErrorCodeMap.put(SETTING_FILE_ERROR, "there is something wrong with setting file");
        ErrorCodeMap.put(FILE_DATA_DAMAGED, "there is something wrong with the file");

        ErrorCodeMap.put(MOVE_OUT_OF_BOUNDARY, "you have moved out of the boundary");
        ErrorCodeMap.put(READ_OUT_OF_BOUNDARY, "you have read out of the boundary");

        ErrorCodeMap.put(UNKNOWN, "unknown");
    }

    public static String getErrorText(int errorCode){
        return ErrorCodeMap.getOrDefault(errorCode, "invalid");
    }

    private int errorCode;

    public ErrorCode(int errorCode){
        super(String.format("error code '%d' \"%s\"", errorCode, getErrorText(errorCode)));
        this.errorCode = errorCode;
    }

    public int getErrorCode(){
        return errorCode;
    }
}
