package Model.Statement;

import Model.ADT.MyDictionaryInterface;
import Model.ADT.MyHeapInterface;
import Model.Expression.Expression;
import Model.ProgramState;
import Exception.MyException;
import Model.Type.IntType;
import Model.Type.StringType;
import Model.Value.IntValue;
import Model.Value.StringValue;
import Model.Value.Value;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadFileStmt implements IStmt{
    Expression expression;
    Value variableName;

    public ReadFileStmt(Expression expression, Value variableName) {
        this.expression = expression;
        this.variableName = variableName;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        if (!variableName.getType().equals(new StringType())){
            throw new MyException("Variable name must be string.");
        }
        String variableNameString = ((StringValue)variableName).getVal();
        MyDictionaryInterface<String, Value> symTable = state.getSymbolTable();
        MyHeapInterface<Integer, Value> heap = state.getHeap();
        if (!symTable.isDefined(variableNameString)){
            throw new MyException("Variable name does not exist in the symbol table.");
        }
        if (!symTable.lookup(variableNameString).getType().equals(new IntType())){
            throw new MyException("Variable is not of type IntType.");
        }
        Value evaluated = expression.evaluate(symTable, heap);
        if (!evaluated.getType().equals(new StringType())){
            throw new MyException("Expression does not evaluate to string.");
        }
        MyDictionaryInterface<StringValue, BufferedReader> fileTable = state.getFileTable();

        BufferedReader bufferedReader = fileTable.lookup(((StringValue)evaluated));
        if (bufferedReader==null){
            throw new MyException("String name cannot be found in FileTable.");
        }
        String readLine = null;
        try {
            readLine = bufferedReader.readLine();
        } catch (IOException e) {
            throw new MyException("Read error: " + e.getMessage());
        }
        IntValue readInt;
        if (readLine==null){
            readInt = (IntValue) new IntType().defaultValue();
        }
        else{
            readInt = new IntValue(Integer.parseInt(readLine));
        }
        symTable.update(variableNameString, readInt);
        return state;
    }

    @Override
    public IStmt deepCopy() {
        return new ReadFileStmt(expression,variableName);
    }

    @Override
    public String toString() {
        return "ReadFileStmt{" +
                "expression=" + expression +
                ", variableName=" + variableName +
                '}';
    }
}
