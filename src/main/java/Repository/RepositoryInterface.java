package Repository;

import Model.ProgramState;

import java.util.List;

import Exception.MyException;

public interface RepositoryInterface {
    List<ProgramState> getAllStates();
    void setAllStates(List<ProgramState> allStates);
    void logProgramStateExecution(ProgramState programState)throws MyException;
    void logCustomMessage(String message) throws MyException;
    public void addState(ProgramState state);
}
