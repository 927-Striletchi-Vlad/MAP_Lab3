package Model.Statement;

import Model.Expression.Expression;
import Model.ProgramState;
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
        BoolValue temp = (BoolValue) expression.evaluate(state.getSymbolTable());
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
}
