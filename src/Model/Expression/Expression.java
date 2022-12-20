package Model.Expression;

import Model.ADT.MyDictionaryInterface;
import Model.ADT.MyHeapInterface;
import Model.Type.Type;
import Model.Value.Value;
import Exception.MyException;

public interface Expression {
    Value evaluate(MyDictionaryInterface<String, Value> symTable, MyHeapInterface<Integer, Value> heap) throws MyException;    
    Expression deepCopy();
    Type typeCheck(MyDictionaryInterface<String, Type> typeEnv) throws MyException;
}
