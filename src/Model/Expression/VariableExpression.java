package Model.Expression;

import Model.ADT.MyDictionaryInterface;
import Model.Value.Value;
import Exception.MyException;

public class VariableExpression implements Expression{
    String id;

    public VariableExpression(String s) {
        this.id = s;
    }

    @Override
    public Value evaluate(MyDictionaryInterface<String, Value> symTable) throws MyException {
        return symTable.lookup(id);
    }

    @Override
    public String toString() {
        return "VariableExpression{" +
                "id='" + id + '\'' +
                '}';
    }
}
