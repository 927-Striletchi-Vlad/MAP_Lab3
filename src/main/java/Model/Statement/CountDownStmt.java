package Model.Statement;

import Model.ADT.MyDictionary;
import Model.ADT.MyDictionaryInterface;
import Model.ADT.MyLockHeap;
import Model.ADT.MyLockHeapInterface;
import Model.ProgramState;
import Model.Type.IntType;
import Model.Type.Type;
import Exception.MyException;
import Model.Value.IntValue;
import Model.Value.Value;

public class CountDownStmt implements IStmt{
    String var;

    public CountDownStmt(String var) {
        this.var = var;
    }
    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        MyLockHeap<Integer, Integer> latchTable = (MyLockHeap <Integer, Integer>) state.getLatchTable();
        MyDictionary<String, Value> symTable = state.getSymbolTable();

        if(!symTable.isDefined(var)){
            throw new MyException("Variable " + var + " is not defined!");
        }
        if(!(symTable.lookup(var).getType() instanceof IntType)){
            throw new MyException("Variable " + var + " is not an integer!");
        }
        int foundIndex = ((IntValue)symTable.lookup(var)).getValue();

        if(!latchTable.isDefined(foundIndex)){
            throw new MyException("Index " + foundIndex + " is not defined in the latch table!");
        }

        int foundValue = latchTable.lookup(foundIndex);
        if(foundValue>0){
            latchTable.update(foundIndex, foundValue-1);
        }
        state.getOutput().add(new IntValue(foundValue));

        return state;
    }

    @Override
    public IStmt deepCopy() {
        return new CountDownStmt(var);
    }

    @Override
    public MyDictionaryInterface<String, Type> typeCheck(MyDictionaryInterface<String, Type> typeEnv) throws MyException {
        if(!typeEnv.isDefined(var))
            throw new MyException("Variable " + var + " is not defined!");
        if(!(typeEnv.lookup(var) instanceof IntType)){
            throw new MyException("Variable " + var + " is not an integer!");
        }

        return typeEnv;
    }
}
