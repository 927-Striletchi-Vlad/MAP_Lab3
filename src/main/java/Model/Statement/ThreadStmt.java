package Model.Statement;

import Exception.MyException;
import Model.ProgramState;
import Model.ADT.MyDictionaryInterface;
import Model.ADT.MyStack;
import Model.Type.Type;

public class ThreadStmt implements IStmt{
    private IStmt stmt;

    public ThreadStmt(IStmt stmt) {
        this.stmt = stmt;
    }

    @Override
    public String toString() {
        return "thread(" + stmt.toString() + ")";
    }

    @Override
    public IStmt deepCopy() {
        return new ThreadStmt(stmt.deepCopy());
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {

        ProgramState newState = new ProgramState(
            new MyStack<IStmt>(),
            state.getSymbolTable().deepCopy(),
            state.getFileTable(),
            state.getOutput(),
            state.getHeap(),
            stmt.deepCopy(),
            state.getLockHeap(),
                state.getLatchTable());



        return newState;
    }

    @Override
    public MyDictionaryInterface<String, Type> typeCheck(MyDictionaryInterface<String, Type> typeEnv)
    throws MyException {
        stmt.typeCheck(typeEnv.deepCopy());
        return typeEnv;
    }
    
}
