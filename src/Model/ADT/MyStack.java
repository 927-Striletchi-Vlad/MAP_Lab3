package Model.ADT;

import java.util.Stack;

public class MyStack<TElem> implements MyStackInterface<TElem>{

    Stack<TElem> stack;

    public MyStack() {
        this.stack = new Stack<TElem>();
    }

    @Override
    public void push(TElem elem) {
        stack.push(elem);
    }

    @Override
    public TElem pop() {
        return stack.pop();
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public String toString() {
        String result = new String();
        for(TElem elem: stack){
            String subResult = new String(elem.toString() + "\n");
            result = subResult.concat(result);
        }
        return result;
    }
}
