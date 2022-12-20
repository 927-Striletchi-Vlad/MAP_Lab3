package Model.Statement;

import Model.ADT.MyDictionaryInterface;
import Model.ADT.MyStack;
import Model.Type.Type;
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
    public String toStringUnpacked(){
        /* Unpack the statement, and put each non-compound statement
         *  on a new line.
         */
        String result = "";
        if (first instanceof CompoundStmt){
            result = result.concat(((CompoundStmt) first).toStringUnpacked());
        }
        else{
            result = result.concat(first.toString());
        }
        result = result.concat("\n");

        if (second instanceof CompoundStmt){
            result = result.concat(((CompoundStmt) second).toStringUnpacked());
        }
        else{
            result = result.concat(second.toString());
        }

        return result;

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

    @Override
    public MyDictionaryInterface<String, Type> typeCheck(MyDictionaryInterface<String, Type> typeEnv)
    throws MyException {
        return second.typeCheck(first.typeCheck(typeEnv));
    }
}
