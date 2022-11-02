package Model.Expression;

import Model.ADT.MyDictionaryInterface;
import Model.Type.IntType;
import Model.Value.IntValue;
import Model.Value.Value;
import Exception.MyException;

public class ArithmeticExpression implements Expression{
    int op; // 1-plus, 2-minus, 3-star, 4-divide
    Expression e1;
    Expression e2;

    public ArithmeticExpression(char c, Expression e1, Expression e2) {
        this.e1 = e1;
        this.e2 = e2;
        if(c=='+'){op=1;}
        if(c=='-'){op=2;}
        if(c=='*'){op=3;}
        if(c=='/'){op=4;}
    }

    public ArithmeticExpression(int op, Expression e1, Expression e2) {
        this.e1 = e1;
        this.e2 = e2;
        this.op = op;
    }


    @Override
    public Value evaluate(MyDictionaryInterface<String, Value> symTable) throws MyException {
        Value v1, v2;
        v1 = e1.evaluate(symTable);
        if (v1.getType().equals(new IntType())) {
            v2 = e2.evaluate(symTable);
            if (v2.getType().equals(new IntType())) {
                IntValue i1 = (IntValue) v1;
                IntValue i2 = (IntValue) v2;
                int n1 = i1.getValue();
                int n2 = i2.getValue();
                if (op == 1) {
                    return new IntValue(n1 + n2);
                }
                if (op == 2) {
                    return new IntValue(n1 - n2);
                }
                if (op == 3) {
                    return new IntValue(n1 * n2);
                }
                if (op == 4) {
                    if (n2 == 0) {
                        throw new MyException("zero division");
                    } else {
                        return new IntValue(n1 + n2);
                    }
                }
            }
            else{
                throw new MyException("second operand is not an integer");
            }
        }

        throw new MyException("first operand is not an integer");

    }

    @Override
    public Expression deepCopy() {
        return new ArithmeticExpression(op, e1, e2);
    }
}
