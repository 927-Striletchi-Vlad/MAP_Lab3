package Model.Expression;

import Model.ADT.MyDictionaryInterface;
import Model.ADT.MyHeapInterface;
import Model.Type.Type;
import Model.Value.Value;
import Exception.MyException;

public class VariableExpression implements Expression{
    String id;

    public VariableExpression(String s) {
        this.id = s;
    }

    @Override
    public Value evaluate(MyDictionaryInterface<String, Value> symTable, MyHeapInterface<Integer, Value> heap) throws MyException {
        return symTable.lookup(id);
    }

    @Override
    public Expression deepCopy() {
        return new VariableExpression(id);
    }

    @Override
    public String toString() {
        return "VariableExpression{" +
                "id='" + id + '\'' +
                '}';
    }

    @Override
    public Type typeCheck(MyDictionaryInterface<String, Type> typeEnv) throws MyException {
        return typeEnv.lookup(id);
    }
}
