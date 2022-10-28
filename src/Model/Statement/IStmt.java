package Model.Statement;

import Model.ProgramState;
import Exception.MyException;
public interface IStmt {
    public ProgramState execute(ProgramState state) throws MyException;

}
