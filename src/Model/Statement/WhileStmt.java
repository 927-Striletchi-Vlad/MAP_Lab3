package Model.Statement;

import Model.ADT.MyStack;
import Model.Expression.Expression;
import Model.ProgramState;
import Exception.MyException;
import Model.Type.BoolType;
import Model.Value.BoolValue;
import Model.Value.Value;

/**
 * while (expression) statement
 */
public class WhileStmt implements IStmt {
    private IStmt statement;
    private Expression expression;

    public WhileStmt(Expression expression, IStmt statement) {
        this.expression = expression;
        this.statement = statement;
    }
    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        Value evaluatedExpression = expression.evaluate(state.getSymbolTable(), state.getHeap());
        if (!evaluatedExpression.getType().equals(new BoolType())){
            throw new MyException("The expression is not a boolean.");
        }
        if (((BoolValue)evaluatedExpression).getValue() == true){
            MyStack<IStmt> exeStack = state.getStack();
            exeStack.push(this);
            exeStack.push(statement);
        }
        return state;
    }

    @Override
    public String toString() {
        return "WhileStmt{" +
                "statement=" + statement +
                ", expression=" + expression +
                '}';
    }

    @Override
    public IStmt deepCopy() {
        return new WhileStmt(expression.deepCopy(), statement.deepCopy());
    }
}
