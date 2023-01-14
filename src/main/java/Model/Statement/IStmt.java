package Model.Statement;

import Model.ProgramState;
import Model.ADT.MyDictionaryInterface;
import Model.Type.Type;
import Exception.MyException;
public interface IStmt {
    public ProgramState execute(ProgramState state) throws MyException;
    IStmt deepCopy();
    MyDictionaryInterface<String, Type> typeCheck(MyDictionaryInterface<String, Type> typeEnv) throws MyException;
}
