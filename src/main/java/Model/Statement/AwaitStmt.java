package Model.Statement;

import Model.ADT.MyDictionary;
import Model.ADT.MyDictionaryInterface;
import Model.ADT.MyLockHeap;
import Model.ProgramState;
import Model.Type.IntType;
import Model.Type.Type;
import Exception.MyException;
import Model.Value.IntValue;
import Model.Value.Value;

public class AwaitStmt implements IStmt{
    String var;

    public AwaitStmt(String var) {
        this.var = var;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        MyDictionary<String, Value> symTable = state.getSymbolTable();
        MyLockHeap<Integer, Integer> lockHeap = (MyLockHeap<Integer, Integer>) state.getLatchTable();

        if(!symTable.isDefined(var))
            throw new MyException("Variable " + var + " is not defined!");
        if(!(symTable.lookup(var).getType() instanceof IntType)){
            throw new MyException("Variable " + var + " is not an integer!");
        }
        int foundIndex = ((IntValue)symTable.lookup(var)).getValue();

        if(!lockHeap.isDefined(foundIndex))
            throw new MyException("Index " + foundIndex + " is not defined in the lock heap!");

        int foundValue = lockHeap.lookup(foundIndex);

        if(foundValue!=0){
            state.getExeStack().push(this);
        }

        return state;
    }

    @Override
    public IStmt deepCopy() {
        return new AwaitStmt(var);
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
