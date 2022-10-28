package Model.ADT;

public interface MyDictionaryInterface<TKey, TValue> {
    public void add(TKey k, TValue v);

    public void update(TKey k, TValue v);

    public TValue lookup(TKey k) ;

    public boolean isDefined(String id);

    public String toString();
}
