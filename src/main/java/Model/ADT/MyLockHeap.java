package Model.ADT;

import java.util.HashMap;

public class MyLockHeap<TKey extends Integer, TValue> implements MyLockHeapInterface<TKey,TValue> {
    HashMap<TKey, TValue> heap;
    int freeAddress;

    public MyLockHeap() {
        freeAddress = 1;
        this.heap = new HashMap<TKey, TValue>();
    }


    @Override
    public synchronized void add(TKey k, TValue v) {
        heap.put(k,v);
    }

    @Override
    public synchronized void update(TKey k, TValue v) {
        heap.put(k, v);
    }

    @Override
    public synchronized void remove(TKey k) {
        heap.remove(k);
    }

    @Override
    public synchronized TValue lookup(TKey k) {
        return heap.get(k);

    }

    @Override
    public boolean isDefined(int id) {
        return heap.containsKey(id);
    }

    @Override
    public String toString() {
        String result = new String();
        for(TKey k: heap.keySet()){
            String subResult = new String(k.toString()+ " " + heap.get(k).toString()+"\n");
            result = result.concat(subResult);
        }
        return result;
    }

    @Override
    public synchronized int allocate(TValue v) {
        heap.put((TKey)(Integer)freeAddress, v);
        int res = freeAddress;
        freeAddress = firstFreeAddress();
        return res;
    }

    @Override
    public HashMap<TKey, TValue> getContent() {
        return heap;
    }

    private int firstFreeAddress() {
        for (int i = 1; i < freeAddress; i++) {
            if (!heap.containsKey(i)) {
                return i;
            }
        }
        return freeAddress + 1;
    }

    public HashMap<TKey, TValue> getHeap() {
        return heap;
    }

    public void setHeap(HashMap<TKey, TValue> newHeapContent) {
        heap = newHeapContent;
    }
}
