package Model.ADT;

import Model.Type.Type;
import Model.Value.Value;

public interface MyDictionaryInterface<TKey, TValue> {
    public void add(TKey k, TValue v);

    public void update(TKey k, TValue v);

    public void remove(TKey k);

    public TValue lookup(TKey k) ;

    public boolean isDefined(String id);

    public String toString();

    public MyDictionaryInterface<TKey, TValue> deepCopy();
}

