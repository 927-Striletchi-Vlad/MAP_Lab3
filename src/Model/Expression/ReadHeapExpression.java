package Model.Expression;

import Model.ADT.MyDictionaryInterface;
import Model.ADT.MyHeapInterface;
import Model.Type.IntType;
import Model.Type.ReferenceType;
import Model.Value.ReferenceValue;
import Model.Value.Value;
import Exception.MyException;
public class ReadHeapExpression implements Expression{

    Expression expression;

    public ReadHeapExpression(Expression expression) {
        this.expression = expression;
    }


    @Override
    public Value evaluate(MyDictionaryInterface<String, Value> symTable, MyHeapInterface<Integer, Value> heap) throws MyException {
        Value evaluatedExpression = null;
        try {
            evaluatedExpression = expression.evaluate(symTable, heap);
        } catch (MyException e) {
            throw new MyException("Expression evaluation failed");
        }
        if (!evaluatedExpression.getType().equals(new ReferenceType(new IntType())))
            throw new MyException("Expression is not a reference");
        Integer address = ((ReferenceValue) evaluatedExpression).getAddress();
        Value value = heap.lookup(address);
        if (value == null)
            throw new MyException("Address not found in heap");
        return value;
    }

    @Override
    public String toString() {
        return "ReadHeapExpression{" +
                "expression=" + expression +
                '}';
    }

    @Override
    public Expression deepCopy() {
        return new ReadHeapExpression(expression.deepCopy());
    }
}
