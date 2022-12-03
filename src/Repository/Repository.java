package Repository;

import Model.ProgramState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import Exception.MyException;

public class Repository implements RepositoryInterface{
    List<ProgramState> allStates;
    String logFilePath;

    public Repository(ProgramState state) {
        this.allStates = new ArrayList<>();
        allStates.add(state);
    }

    public Repository(ProgramState state, String logFilePath) {
        this.allStates = new ArrayList<>();
        allStates.add(state);
        this.logFilePath = logFilePath;
    }

    public void addState(ProgramState state){
        allStates.add(state);
    }

    public List<ProgramState> getAllStates() {
        return this.allStates;
    }

    public void setAllStates(List<ProgramState> allStates) {
        this.allStates = allStates;
    }

    

    // @Override
    // public ProgramState getCurrentProgram() {
    //     return allStates.get(0);
    // }

    @Override
    public void logProgramStateExecution(ProgramState programState) throws MyException {
        PrintWriter logFile = null;
        try {
            logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)));
        } catch (IOException e) {
            throw new MyException(e.getMessage());
        }
        logFile.write(programState.toString());
        logFile.close();

    }

    @Override
    public void logCustomMessage(String message) throws MyException {
        PrintWriter logFile = null;
        try {
            logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)));
        } catch (IOException e) {
            throw new MyException(e.getMessage());
        }
        logFile.write(message);
        logFile.close();
    }

        
}

