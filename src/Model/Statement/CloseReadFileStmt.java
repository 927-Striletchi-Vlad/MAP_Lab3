package Model.Statement;

import Model.ADT.MyDictionaryInterface;
import Model.Expression.Expression;
import Model.ProgramState;
import Exception.MyException;
import Model.Type.StringType;
import Model.Value.StringValue;
import Model.Value.Value;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CloseReadFileStmt implements IStmt{
    Expression expression;

    public CloseReadFileStmt(Expression expression) {
        this.expression=expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        Value value = expression.evaluate(state.getSymbolTable(), state.getHeap());
        if (!value.getType().equals(new StringType())){
            throw new MyException("Expression does not contain a StringValue.");
        }
        MyDictionaryInterface<StringValue, BufferedReader> fileTable = state.getFileTable();
        BufferedReader bufferedReader = fileTable.lookup((StringValue) value);
        if (bufferedReader==null){
            throw new MyException("File name cannot be found in the FileTable.");
        }
        try {
            bufferedReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        fileTable.remove((StringValue) value);
        return state;
    }

    @Override
    public IStmt deepCopy() {
        return new CloseReadFileStmt(expression);
    }

    @Override
    public String toString() {
        return "CloseReadFileStmt{" +
                "expression=" + expression +
                '}';
    }
}
