package Model.ADT;

import Model.Statement.CompoundStmt;

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
            String subResult;
            if (elem instanceof CompoundStmt){
                subResult = new String(((CompoundStmt) elem).toStringUnpacked() + "\n");
            }
            else{
                subResult = new String(elem.toString() + "\n\n");
            }
            result = subResult.concat(result);
        }
        return result;
    }
}
