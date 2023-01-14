package Model.ADT;

public interface MyStackInterface<TElem> {
    public void push(TElem elem);

    public TElem pop();

    public boolean isEmpty();

    public String toString();
}
