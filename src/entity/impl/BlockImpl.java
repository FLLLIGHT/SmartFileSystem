package entity.impl;

import entity.Block;
import entity.BlockManager;
import entity.Id;

public class BlockImpl implements Block {
    @Override
    public Id getIndexId() {
        return null;
    }

    @Override
    public BlockManager getBlockManager() {
        return null;
    }

    @Override
    public byte[] read() {
        return new byte[0];
    }

    @Override
    public int blockSize() {
        return 0;
    }
}
