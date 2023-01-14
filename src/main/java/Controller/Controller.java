package Controller;

import Model.GarbageCollector;
import Model.ProgramState;
import Repository.RepositoryInterface;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import Exception.MyException;

public class Controller {
    RepositoryInterface repositoryInterface;
    ExecutorService executor;
    boolean completeFlag;

    public Controller(RepositoryInterface repositoryInterface) {
        this.repositoryInterface = repositoryInterface;
        this.completeFlag = false;
    }

    public void oneStepAll(List<ProgramState> programStates){
        programStates.forEach(programState -> {
            try {
                repositoryInterface.logProgramStateExecution(programState);
            } catch (MyException e) {
                e.printStackTrace();
            }
        });

        List<Callable<ProgramState>> callList = programStates.stream()
                .map((ProgramState programState) -> (Callable<ProgramState>)(() -> {return programState.oneStep();}))
                .toList();


        List<ProgramState> newProgramStates = null;
        try {
            newProgramStates = executor.invokeAll(callList).stream()
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    })
                    .filter(programState -> programState != null)
                    .collect(Collectors.toList());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        Set<ProgramState> set = new HashSet<>(programStates); // remove duplicates
        set.addAll(programStates);
        set.addAll(newProgramStates);
        programStates.clear();
        programStates.addAll(set);

        programStates.forEach(programState -> {
            try {
                repositoryInterface.logProgramStateExecution(programState);
            } catch (MyException e) {
                e.printStackTrace();
            }
        });

        repositoryInterface.setAllStates(programStates);
    }

    public void oneStepNoCleanup(){
        executor = Executors.newFixedThreadPool(2);
        List<ProgramState> programStates = repositoryInterface.getAllStates();
        GarbageCollector garbageCollector = new GarbageCollector();

        garbageCollector.collectGarbage(programStates);
        if (!programStates.isEmpty()){
            oneStepAll(programStates);
        }

        executor.shutdownNow();
        repositoryInterface.setAllStates(programStates);
    }

    public void cleanup(){
        executor = Executors.newFixedThreadPool(2);
        List<ProgramState> programStates = removeCompletedPrograms(repositoryInterface.getAllStates());
        GarbageCollector garbageCollector = new GarbageCollector();
        garbageCollector.collectGarbage(programStates);
        repositoryInterface.setAllStates(programStates);
    }

    public void oneStep(){
        executor = Executors.newFixedThreadPool(2);
        List<ProgramState> programStates = removeCompletedPrograms(repositoryInterface.getAllStates());
        GarbageCollector garbageCollector = new GarbageCollector();

        garbageCollector.collectGarbage(programStates);
        oneStepAll(programStates);
        programStates = removeCompletedPrograms(repositoryInterface.getAllStates());


        executor.shutdownNow();
        repositoryInterface.setAllStates(programStates);
    }
    public void allStep(){
        executor = Executors.newFixedThreadPool(2);
        List<ProgramState> programStates = removeCompletedPrograms(repositoryInterface.getAllStates());
        GarbageCollector garbageCollector = new GarbageCollector();

        while(programStates.size() > 0){
            garbageCollector.collectGarbage(programStates);
            oneStepAll(programStates);
            programStates = removeCompletedPrograms(repositoryInterface.getAllStates());
        }

        executor.shutdownNow();
        repositoryInterface.setAllStates(programStates);
    }

    public boolean isCompleted(){
        List<ProgramState> programStates = repositoryInterface.getAllStates();
        for(ProgramState programState:programStates){
            if(!programState.isCompleted()){
                return false;
            }
        }
        return true;
    }

    public boolean isActuallyCompleted(){
        if (completeFlag == false){
            if (isCompleted()){
                completeFlag = true;
            }
            return false;

        }
        else {
            return true;
        }


    }


    public List<ProgramState> removeCompletedPrograms(List<ProgramState> programStates ){
        
        List<ProgramState> res = programStates.stream()
                .filter(programState -> programState.isNotCompleted())
                .collect(Collectors.toList());

        return res;
    }

    public List<ProgramState> getProgramStates(){
        return repositoryInterface.getAllStates();
    }

    public ProgramState getProgramStateById(int id) throws MyException {
        List<ProgramState> allStates = repositoryInterface.getAllStates();
        for (ProgramState programState : allStates) {
            if (programState.getId() == id) {
                return programState;
            }
        }
        throw new MyException("No program state with id " + id);
    }

    public int getFirstProgramStateId() {
        return repositoryInterface.getAllStates().get(0).getId();
    }
}
