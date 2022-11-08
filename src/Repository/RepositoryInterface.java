package Repository;

import Model.ProgramState;

import Exception.MyException;

public interface RepositoryInterface {
    ProgramState getCurrentProgram();

    void logProgramStateExecution() throws MyException;
}
