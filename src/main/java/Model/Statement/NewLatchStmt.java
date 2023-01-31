package Model.Statement;

import Model.ADT.MyDictionaryInterface;
import Model.Expression.Expression;
import Model.ProgramState;
import Model.Type.IntType;
import Model.Type.Type;
import Exception.MyException;
import Model.Value.IntValue;
import Model.Value.Value;

public class NewLatchStmt implements IStmt{
    String var;
    Expression exp;

    public NewLatchStmt(String var, Expression exp) {
        this.var = var;
        this.exp = exp;
    }
    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        Value num1 = exp.evaluate(state.getSymbolTable(), state.getHeap());
        if(!(num1.getType() instanceof IntType))
            throw new MyException("Expression is not an integer");
        int num = ((IntValue)num1).getValue();
        state.getLatchTable().setFreeAddress(num);
        state.getLatchTable().add(num, state.getLatchTable().getFreeAddress());

        MyDictionaryInterface<String, Value> symTable = state.getSymbolTable();

        if (symTable.isDefined(var)){
            if(!(symTable.lookup(var).getType() instanceof IntType)){
                throw new MyException("Variable is not an integer");
            }
            symTable.update(var, new IntValue(state.getLatchTable().getFreeAddress()));
        }

        else{
            symTable.add(var, new IntValue(state.getLatchTable().getFreeAddress()));
        }

        return state;
    }

    @Override
    public IStmt deepCopy() {
        return new NewLatchStmt(var, exp.deepCopy());
    }

    @Override
    public MyDictionaryInterface<String, Type> typeCheck(MyDictionaryInterface<String, Type> typeEnv) throws MyException {
        if (typeEnv.isDefined(var)){
            if(!(typeEnv.lookup(var) instanceof IntType)){
                throw new MyException("Variable is not an integer");
            }
        }
        else{
            typeEnv.add(var, new IntType());
        }

        if(!(exp.typeCheck(typeEnv) instanceof IntType)){
            throw new MyException("Expression is not an integer");
        }

        return typeEnv;
    }
}
