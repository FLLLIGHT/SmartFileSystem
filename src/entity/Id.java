package entity;

public interface Id {
    @Override
    public String toString();

    @Override
    public int hashCode();

    @Override
    public boolean equals(Object obj);
}
