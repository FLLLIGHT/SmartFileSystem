package entity;

//todo: 能够在重新启动后接管原有系统
//todo: 也不一定要全部塞进内存

public interface BlockManager {
    Block getBlock(Id indexId);
    Block newBlock(byte[] b);
    default Block newEmptyBlock(int blockSize){
        return newBlock(new byte[blockSize]);
    }
    Id getId();
}
