package Model.Statement;

import Model.ProgramState;
import Exception.MyException;

public class NoOperationStmt implements IStmt{
    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        return state;
    }

    @Override
    public IStmt deepCopy() {
        return new NoOperationStmt();
    }

    @Override
    public String toString() {
        return "NoOperationStatement{}";
    }
}
