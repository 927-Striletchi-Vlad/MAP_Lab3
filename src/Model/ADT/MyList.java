package Model.ADT;

import Model.Value.Value;

import java.util.ArrayList;
import java.util.List;

public class MyList<TElem> implements MyListInterface<TElem>{
    List<TElem> list = new ArrayList<TElem>();

    public MyList() {
        this.list = new ArrayList<TElem>();
    }

    @Override
    public void add(TElem elem) {
        list.add(elem);
    }

    @Override
    public void remove(TElem elem) {
        list.remove(elem);
    }

    @Override
    public String toString() {
        String result = new String();
        for(TElem elem:list){
            result = result.concat(elem.toString());
            result=result.concat("\n");
        }
        return result;
    }

    public List<TElem> getList() {
        return list;
    }
}
