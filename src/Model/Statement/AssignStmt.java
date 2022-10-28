package Model.Statement;

import Model.ADT.MyDictionaryInterface;
import Model.ADT.MyStackInterface;
import Model.Expression.Expression;
import Model.Expression.ValueExpression;
import Model.ProgramState;
import Exception.MyException;
import Model.Type.Type;
import Model.Value.Value;

public class AssignStmt implements IStmt{
    String id;
    Expression expression;

    public AssignStmt(String id, Expression expression) {
        this.id = id;
        this.expression = expression;
    }

    public String toString(){
        return id + "=" + expression.toString();
    }

    public ProgramState execute(ProgramState state) throws MyException{
        MyStackInterface<IStmt> stack = state.getStack();
        MyDictionaryInterface<String, Value> symbolTable = state.getSymbolTable();

        if (!(symbolTable.isDefined(id))) {
            throw new MyException("undefined variable");
        }
        Value value = expression.evaluate(symbolTable);
        Type typeId = (symbolTable.lookup(id)).getType();
        if (!(value.getType().equals(typeId))){
            throw new MyException("type does not match");
        }
        symbolTable.update(id,value);
        return state;
    }
}
