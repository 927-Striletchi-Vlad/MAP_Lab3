package Model.Statement;

import Model.Expression.Expression;
import Model.Expression.LogicExpression;
import Model.Type.BoolType;
import Model.Type.Type;
import Model.ProgramState;
import Model.ADT.MyDictionaryInterface;
import Exception.MyException;
import Model.Value.BoolValue;

public class IfStmt implements IStmt{
    Expression expression;
    IStmt thenS, elseS;

    public IfStmt(Expression expression, IStmt thenS, IStmt elseS) {
        this.expression = expression;
        this.thenS = thenS;
        this.elseS = elseS;
    }

    @Override
    public String toString() {
        return "IfStmt{" +
                "IF " + expression +
                " THEN" + thenS +
                " ELSE" + elseS +
                '}';
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        BoolValue temp = (BoolValue) expression.evaluate(state.getSymbolTable(), state.getHeap());
        boolean evalResult = temp.getValue();
        if(evalResult){
            thenS.execute(state);
        }
        else{
            elseS.execute(state);
        }
        return state;
    }

    @Override
    public IStmt deepCopy() {
        return new IfStmt(expression.deepCopy(),thenS.deepCopy(), elseS.deepCopy());
    }

    @Override
    public MyDictionaryInterface<String, Type> typeCheck(MyDictionaryInterface<String, Type> typeEnv)
    throws MyException {
        if(expression instanceof LogicExpression){
            thenS.typeCheck(typeEnv.deepCopy());
            elseS.typeCheck(typeEnv.deepCopy());
            return typeEnv;
        }
        else{
            throw new MyException("The condition of IF is not bool type");
        }
    }
}
