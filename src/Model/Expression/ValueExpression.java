package Model.Expression;

import Model.ADT.MyDictionaryInterface;
import Model.ADT.MyHeapInterface;
import Model.Type.Type;
import Model.Value.Value;
import Exception.MyException;

public class ValueExpression implements Expression{
    Value e;

    public ValueExpression(Value value) {
        this.e = value;
    }

    @Override
    public Value evaluate(MyDictionaryInterface<String, Value> symTable, MyHeapInterface<Integer, Value> heap) throws MyException{
        return e;
    }

    @Override
    public Expression deepCopy() {
        return new ValueExpression(e);
    }

    @Override
    public String toString() {
        return "ValueExpression{" +
                "e=" + e +
                '}';
    }

    @Override
    public Type typeCheck(MyDictionaryInterface<String, Type> typeEnv) throws MyException {
        return e.getType();
    }
}
