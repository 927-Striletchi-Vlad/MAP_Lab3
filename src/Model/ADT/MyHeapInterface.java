package Model.ADT;

public interface MyHeapInterface<TKey extends Integer, TValue> {

    public void add(TKey k, TValue v);

    public void update(TKey k, TValue v);

    public void remove(TKey k);

    public TValue lookup(TKey k) ;

    public boolean isDefined(int id);

    public String toString();
/**Add a new value to the heap and
 *  return the address where it was added
 */
    public int allocate(TValue v);
}
