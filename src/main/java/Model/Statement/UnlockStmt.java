package Model.Statement;

import Model.ADT.MyDictionary;
import Model.ADT.MyDictionaryInterface;
import Model.ProgramState;
import Model.Type.Type;
import Exception.MyException;
import Model.Value.Value;
import Model.Value.IntValue;

public class UnlockStmt implements IStmt{
    private String variableName;

    public UnlockStmt(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        MyDictionary<String, Value> symTable = state.getSymbolTable();
        if (!symTable.isDefined(variableName)) {
            throw new MyException("Variable not defined");
        }
        Value value = symTable.lookup(variableName);
        if (!value.getType().equals(new IntValue(0).getType())) {
            throw new MyException("Variable is not an integer");
        }
        int address = ((IntValue)value).getValue();
        if (!state.getLockHeap().isDefined(address)) {
            throw new MyException("Address not defined");
        }
        Value lockValue = state.getLockHeap().lookup(address);
        if (!lockValue.getType().equals(new IntValue(0).getType())) {
            throw new MyException("Lock value is not an integer");
        }
        int lockNumber = ((IntValue)lockValue).getValue();
        if (lockNumber != state.getId()) {
            throw new MyException("Lock number is not the same as the thread id");
        }
        state.getLockHeap().update(address, new IntValue(-1));


        return state;
    }

    @Override
    public IStmt deepCopy() {
        return new UnlockStmt(variableName);
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
