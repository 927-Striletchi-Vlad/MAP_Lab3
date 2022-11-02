package Model.Statement;

import Model.ADT.MyStack;
import Model.ProgramState;
import Exception.MyException;

public class CompoundStmt implements IStmt{
    IStmt first;
    IStmt second;

    public CompoundStmt(IStmt first, IStmt second) {
        this.first = first;
        this.second = second;
    }

    public String toString(){
        return "(" + first.toString() + second.toString() + ")";
    }

    public ProgramState execute(ProgramState state) throws MyException {
        MyStack<IStmt> stack = state.getStack();
        stack.push(second);
        stack.push(first);
        return state;
    }

    @Override
    public IStmt deepCopy() {
        return new CompoundStmt(first.deepCopy(), second.deepCopy());
    }
}
