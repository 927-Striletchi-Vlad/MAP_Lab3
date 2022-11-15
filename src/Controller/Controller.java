package Controller;

import Model.ADT.MyStackInterface;
import Model.GarbageCollector;
import Model.ProgramState;
import Model.Statement.IStmt;
import Repository.RepositoryInterface;
import Exception.MyException;

public class Controller {
    RepositoryInterface repositoryInterface;

    public Controller(RepositoryInterface repositoryInterface) {
        this.repositoryInterface = repositoryInterface;
    }

    public ProgramState oneStep(ProgramState state) throws MyException{
        MyStackInterface<IStmt> exeStack = state.getStack();
        if (exeStack.isEmpty()){
            throw new MyException("execution stack is empty.");
        }
        IStmt currentStatement = exeStack.pop();
        return currentStatement.execute(state);
    }

    public void allStep() throws MyException{
        ProgramState state = repositoryInterface.getCurrentProgram();
        GarbageCollector garbageCollector = new GarbageCollector();
        repositoryInterface.logProgramStateExecution();

        while (!state.getStack().isEmpty()){
            try{
                state = oneStep(state);
                repositoryInterface.logProgramStateExecution();
                state =  garbageCollector.collectGarbage(state);
                repositoryInterface.logCustomMessage("################## INTERVENTION OF THE GARBAGE COLLECTOR ##################");
                repositoryInterface.logProgramStateExecution();
            }
            catch (Exception exception){
                throw new MyException(exception.getMessage());
            }
        }
    }

    public ProgramState getCurrentState(){
        return repositoryInterface.getCurrentProgram();
    }
}
