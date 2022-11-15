package Model.Statement;

import Model.ADT.MyDictionaryInterface;
import Model.ADT.MyHeapInterface;
import Model.Expression.Expression;
import Model.ProgramState;
import Exception.MyException;
import Model.Type.ReferenceType;
import Model.Value.ReferenceValue;
import Model.Value.StringValue;
import Model.Value.Value;

public class NewStmt implements IStmt{
    String varName;
    Expression expression;

    public NewStmt(String varName, Expression expression) {
        this.varName = varName;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        MyDictionaryInterface<String, Value> symTable = state.getSymbolTable();
        MyHeapInterface<Integer, Value> heap = state.getHeap();
        Value lookResult = symTable.lookup(varName);
        if(lookResult == null)
            throw new MyException("Variable not found in symbol table");
        if (!(lookResult.getType() instanceof ReferenceType))
            throw new MyException("Variable is not a reference");
        Value evaluatedExpression = expression.evaluate(symTable, heap);
        if (!((ReferenceValue)lookResult).getLocationType().equals(evaluatedExpression.getType())) {
            throw new MyException("Variable and expression have different types");
        }
        int address = heap.allocate(evaluatedExpression);
        symTable.update(varName, new ReferenceValue(address, evaluatedExpression.getType()));
        return state;
    }

    @Override
    public String toString() {
        return "NewStmt{" +
                "varName='" + varName + '\'' +
                ", expression=" + expression +
                '}';
    }

    @Override
    public IStmt deepCopy() {
        return new NewStmt(varName, expression.deepCopy());
    }
}
