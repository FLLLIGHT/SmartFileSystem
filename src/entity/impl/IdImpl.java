package entity.impl;

import entity.Id;

import java.util.Objects;

public class IdImpl implements Id {

    private final String id;

    public IdImpl(String id){
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public int hashCode(){
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o){
        if(o != null){
            return this.toString().equals(o.toString());
        }else{
            return false;
        }
    }


}
