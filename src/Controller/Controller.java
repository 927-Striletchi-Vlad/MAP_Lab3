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

    public Controller(RepositoryInterface repositoryInterface) {
        this.repositoryInterface = repositoryInterface;
    }

    void oneStepAll(List<ProgramState> programStates){
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


    public List<ProgramState> removeCompletedPrograms(List<ProgramState> programStates ){
        
        List<ProgramState> res = programStates.stream()
                .filter(programState -> programState.isNotCompleted())
                .collect(Collectors.toList());

        return res;
    }
}
