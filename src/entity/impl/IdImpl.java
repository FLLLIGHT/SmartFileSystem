package entity.impl;

import entity.Id;

public class IdImpl implements Id {

    private final String id;

    public IdImpl(String id){
        this.id = id;
    }

    @Override
    public String parseId() {
        return id;
    }
}
