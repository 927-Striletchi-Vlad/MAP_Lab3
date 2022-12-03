package Model.Expression;

import Model.ADT.MyDictionaryInterface;
import Model.ADT.MyHeapInterface;
import Model.Type.BoolType;
import Model.Type.IntType;
import Model.Value.BoolValue;
import Model.Value.IntValue;
import Model.Value.Value;
import Exception.MyException;


public class LogicExpression implements Expression{
    Expression e1, e2;
    int op; //1-"==", 2-"!=", 3-"<=", 4-">=", 5-"<", 6-">"

    public LogicExpression(Expression e1, Expression e2, int op) {
        this.e1 = e1;
        this.e2 = e2;
        this.op = op;
    }
    public LogicExpression(Expression e1, Expression e2, String op) {
        this.e1 = e1;
        this.e2 = e2;
        switch (op) {
            case "==" -> this.op = 1;
            case "!=" -> this.op = 2;
            case "<=" -> this.op = 3;
            case ">=" -> this.op = 4;
            case "<" -> this.op = 5;
            case ">" -> this.op = 6;
        }
    }

    private boolean evaluateInt(IntValue v1, IntValue v2, int op){
        if(op==1){ return v1.getValue()==v2.getValue();}
        if(op==2){ return v1.getValue()!=v2.getValue();}
        if(op==3){ return v1.getValue()<=v2.getValue();}
        if(op==4){ return v1.getValue()>=v2.getValue();}
        if(op==5){ return v1.getValue()<v2.getValue();}
        if(op==6){ return v1.getValue()>v2.getValue();}
        return false;
    }
    private boolean evaluateBool(BoolValue v1, BoolValue v2, int op){
        if(op==1){ return v1.getValue()==v2.getValue();}
        if(op==2){ return v1.getValue()!=v2.getValue();}
        return false;
    }


    @Override
    public Value evaluate(MyDictionaryInterface<String, Value> symTable, MyHeapInterface<Integer, Value> heap) throws MyException {
        Value v1 = e1.evaluate(symTable, heap);
        Value v2 = e2.evaluate(symTable, heap);

        if(!v1.getType().equals(v2.getType())){
            throw new MyException("Different data types, illegal.");
        }

        if (v1.getType().equals(new IntType())){
            return new BoolValue(evaluateInt((IntValue) v1, (IntValue) v2,op));
        }

        if (v1.getType().equals(new BoolType())){
            if (op>2){throw new MyException("Illegal operation for bool.");}
            return new BoolValue(evaluateBool((BoolValue) v1, (BoolValue) v2,op));
        }

        throw new MyException("Could not evaluate.");
    }

    @Override
    public Expression deepCopy() {
        return new LogicExpression(e1,e2,op);
    }

    @Override
    public String toString() {
        return "LogicExpression{" +
                "e1=" + e1 +
                ", e2=" + e2 +
                ", op=" + op +
                '}';
    }
}
