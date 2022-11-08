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

public class OpenReadFileStmt implements IStmt{
    Expression expression;

    public OpenReadFileStmt(Expression expression) {
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException{
        Value value = expression.evaluate(state.getSymbolTable());
        if (!value.getType().equals(new StringType())){
            throw new MyException("Expression does not contain a StringValue.");
        }
        if (state.getFileTable().lookup((StringValue) value)!=null){
            throw new MyException("File name is already taken in the FileTable.");
        }
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(((StringValue)value).getVal()));
        }catch (FileNotFoundException e){
            throw new MyException(e.getMessage());
        }
        MyDictionaryInterface<StringValue, BufferedReader> fileTable = state.getFileTable();
        fileTable.add((StringValue) value,bufferedReader);

        return state;
    }

    @Override
    public IStmt deepCopy() {
        return new OpenReadFileStmt(expression);
    }

    @Override
    public String toString() {
        return "OpenReadFileStmt{" +
                "expression=" + expression +
                '}';
    }
}
