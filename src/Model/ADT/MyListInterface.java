package Model.ADT;

import java.util.List;

public interface MyListInterface<TElem> {
    public void add(TElem elem);

    public void remove(TElem elem);

    public String toString();

    public List<TElem> getList();
}
