package Model.Statement;

import Model.ProgramState;
import Model.ADT.MyDictionaryInterface;
import Model.Type.Type;
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

    @Override
    public MyDictionaryInterface<String, Type> typeCheck(MyDictionaryInterface<String, Type> typeEnv)
    throws MyException {
        return typeEnv;
    }
}
