package Model.Statement;

import Model.ADT.MyDictionaryInterface;
import Model.ProgramState;
import Model.Type.Type;
import Model.Value.Value;
import Exception.MyException;

public class VariableDeclarationStmt implements IStmt{
    String name;
    Type type;

    public VariableDeclarationStmt(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        MyDictionaryInterface<String, Value> symTable = state.getSymbolTable();
        symTable.add(name, type.defaultValue());

        return state;
    }

    @Override
    public IStmt deepCopy() {
        return new VariableDeclarationStmt(name.toString(), type.deepCopy());
    }

    @Override
    public String toString() {
        return "VariableDeclarationStmt{" +
                "name='" + name + '\'' +
                ", type=" + type +
                '}';
    }

    @Override
    public MyDictionaryInterface<String, Type> typeCheck(MyDictionaryInterface<String, Type> typeEnv)
    throws MyException {
        typeEnv.add(name, type);
        return typeEnv;
    }
}
