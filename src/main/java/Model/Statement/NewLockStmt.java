package Model.Statement;

import Model.ADT.MyDictionaryInterface;
import Model.ADT.MyLockHeapInterface;
import Model.ProgramState;
import Model.Type.Type;
import Exception.MyException;
import Model.Value.IntValue;
import Model.Value.Value;

public class NewLockStmt implements IStmt{
    private String variableName;

    public NewLockStmt(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        MyLockHeapInterface<Integer, Value> lockHeap = state.getLockHeap();
        int address = lockHeap.allocate(new IntValue(-1));
        MyDictionaryInterface<String, Value> symTable = state.getSymbolTable();
        if (symTable.isDefined(variableName)) {
            if (symTable.lookup(variableName).getType().equals(new IntValue(0).getType())) {
                symTable.update(variableName, new IntValue(address));
            }
            else {
                throw new MyException("Variable is not an integer");
            }
        }
        else {
            throw new MyException("Variable not defined");
        }


        return state;
    }

    @Override
    public IStmt deepCopy() {
        return new NewLockStmt(variableName);
    }

    @Override
    public MyDictionaryInterface<String, Type> typeCheck(MyDictionaryInterface<String, Type> typeEnv) throws MyException {
        if (typeEnv.lookup(variableName).equals(new IntValue(0).getType())) {
            return typeEnv;
        }
        else {
            throw new MyException("Variable is not an integer");
        }
    }
}
