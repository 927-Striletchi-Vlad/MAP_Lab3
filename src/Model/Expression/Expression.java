package Model.Expression;

import Model.ADT.MyDictionaryInterface;
import Model.Value.Value;
import Exception.MyException;

public interface Expression {
    Value evaluate(MyDictionaryInterface<String, Value> symTable) throws MyException;
    Expression deepCopy();
}
