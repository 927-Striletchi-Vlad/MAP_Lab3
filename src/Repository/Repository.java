package Repository;

import Model.ProgramState;

import java.util.ArrayList;
import java.util.List;

public class Repository implements RepositoryInterface{
    List<ProgramState> allStates;

    public Repository(ProgramState state) {
        this.allStates = new ArrayList<>();
        allStates.add(state);
    }

    @Override
    public ProgramState getCurrentProgram() {
        return allStates.get(0);
    }
}
