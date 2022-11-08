package Model.ADT;

import java.util.HashMap;

public class MyDictionary<TKey, TValue> implements MyDictionaryInterface<TKey, TValue>{
    HashMap<TKey, TValue> dictionary;

    public MyDictionary() {
        this.dictionary = new HashMap<TKey, TValue>();
    }

    @Override
    public void add(TKey k, TValue v) {
        dictionary.put(k,v);
    }

    @Override
    public void update(TKey k, TValue v) {
        dictionary.put(k, v);
    }

    @Override
    public void remove(TKey k) {
        dictionary.remove(k);
    }

    @Override
    public TValue lookup(TKey k) {
        return dictionary.get(k);
    }

    @Override
    public boolean isDefined(String id) {
        return dictionary.containsKey(id);
    }

    @Override
    public String toString() {
        String result = new String();
        for(TKey k: dictionary.keySet()){
            String subResult = new String(k.toString()+ " " + dictionary.get(k).toString()+"\n");
            result = result.concat(subResult);
        }
        return result;
    }
}
