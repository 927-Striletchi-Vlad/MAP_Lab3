package Model.Statement;

import Model.ADT.MyDictionaryInterface;
import Model.ADT.MyStack;
import Model.Expression.Expression;
import Model.Expression.LogicExpression;
import Model.Expression.VariableExpression;
import Model.ProgramState;
import Model.Type.IntType;
import Model.Type.Type;
import Exception.MyException;

public class ForStmt implements IStmt{
    private String variableName;
    private Expression exp1;
    private Expression exp2;
    private Expression exp3;
    private IStmt statement;

    public ForStmt(String variableName, Expression exp1, Expression exp2, Expression exp3, IStmt statement) {
        this.variableName = variableName;
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.exp3 = exp3;
        this.statement = statement;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        MyStack<IStmt> stack = state.getStack();
//        while((v<exp2) stmt ;v=exp3);
        stack.push(new WhileStmt(new LogicExpression(new VariableExpression(variableName), exp2, "<"),
                new CompoundStmt(statement, new AssignStmt(variableName, exp3))));
//        v=exp1;
        stack.push(new AssignStmt(variableName, exp1));
//        int v;
        stack.push(new VariableDeclarationStmt(variableName, new IntType()));
        return state;
    }

    @Override
    public IStmt deepCopy() {
        return new ForStmt(variableName, exp1.deepCopy(), exp2.deepCopy(), exp3.deepCopy(), statement.deepCopy());
    }

    @Override
    public MyDictionaryInterface<String, Type> typeCheck(MyDictionaryInterface<String, Type> typeEnv) throws MyException {
        //Type type0 = typeEnv.lookup(variableName);
        typeEnv.add(variableName, new IntType());
        Type type1 = exp1.typeCheck(typeEnv);
        Type type2 = exp2.typeCheck(typeEnv);
        Type type3 = exp3.typeCheck(typeEnv);
//        if (!type0.equals(new IntType())){
//            throw new MyException("The variable is not an integer.");
//        }
        if (!type1.equals(new IntType())){
            throw new MyException("The first expression is not an integer.");
        }
        if (!type2.equals(new IntType())){
            throw new MyException("The second expression is not an integer.");
        }
        if (!type3.equals(new IntType())){
            throw new MyException("The third expression is not an integer.");
        }
        return typeEnv;
    }

    @Override
    public String toString() {
        return "ForStmt(" +
                variableName + "=" + exp1 +
                ";" + variableName + "<" + exp2 +
                ";" + variableName + "=" + exp3 +
                "{" + statement + "})";
    }
}
