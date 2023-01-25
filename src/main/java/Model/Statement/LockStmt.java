package Model.Statement;

import Model.ADT.MyDictionaryInterface;
import Model.ProgramState;
import Model.Type.Type;
import Exception.MyException;
import Model.Value.Value;
import Model.ADT.MyLockHeapInterface;
import Model.Value.IntValue;

public class LockStmt implements IStmt{
    private String variableName;

    public LockStmt(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        MyDictionaryInterface<String, Value> symTable = state.getSymbolTable();
        MyLockHeapInterface<Integer, Value> lockHeap = state.getLockHeap();
        if (!symTable.isDefined(variableName)) {
            throw new MyException("Variable not defined");
        }
        Value value = symTable.lookup(variableName);
        if (!value.getType().equals(new IntValue(0).getType())) {
            throw new MyException("Variable is not an integer");
        }
        int address = ((IntValue)value).getValue();
        if (!lockHeap.isDefined(address)) {
            throw new MyException("Address not defined");
        }
        Value lockValue = lockHeap.lookup(address);
        if (!lockValue.getType().equals(new IntValue(0).getType())) {
            throw new MyException("Lock value is not an integer");
        }
        int lockNumber = ((IntValue)lockValue).getValue();
        if (lockNumber == -1) {
            lockHeap.update(address, new IntValue(state.getId()));
        }
        else {
            state.getExeStack().push(this);
        }
        return state;
    }

    @Override
    public IStmt deepCopy() {
        return new LockStmt(variableName);
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
