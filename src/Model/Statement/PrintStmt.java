package Model.Statement;

import Model.ADT.MyDictionaryInterface;
import Model.ADT.MyHeapInterface;
import Model.ADT.MyListInterface;
import Model.Expression.Expression;
import Model.ProgramState;
import Exception.MyException;
import Model.Value.Value;

public class PrintStmt implements IStmt{
    Expression expression;

    public PrintStmt(Expression expression) {
        this.expression = expression;
    }

    public String toString(){
        return "print(" + expression.toString() + ")";
    }

    public ProgramState execute(ProgramState state) throws MyException{
        MyListInterface<Value> stateOutput = state.getOutput();
        MyDictionaryInterface<String, Value> symTable = state.getSymbolTable();
        MyHeapInterface<Integer, Value> heap = state.getHeap();
        stateOutput.add(expression.evaluate(symTable, heap));
        state.setOutput(stateOutput);
        return state;
    }

    @Override
    public IStmt deepCopy() {
        return new PrintStmt(expression.deepCopy());
    }
}
