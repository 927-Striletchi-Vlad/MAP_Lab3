package Model.Statement;

import java.io.BufferedReader;

import Exception.MyException;
import Model.ProgramState;
import Model.ADT.MyDictionary;
import Model.ADT.MyHeap;
import Model.ADT.MyList;
import Model.ADT.MyStack;
import Model.Value.StringValue;
import Model.Value.Value;

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
            stmt.deepCopy());    

        return newState;
    }
    
}
