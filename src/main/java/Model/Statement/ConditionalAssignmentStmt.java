package Model.Statement;

import Model.ADT.MyDictionaryInterface;
import Model.ADT.MyStackInterface;
import Model.Expression.Expression;
import Model.Expression.LogicExpression;
import Model.ProgramState;
import Model.Type.BoolType;
import Model.Type.Type;
import Exception.MyException;
import Model.Value.Value;
import Model.Statement.AssignStmt;

public class ConditionalAssignmentStmt implements IStmt{
    private String variableName;
    private Expression thenValue;
    private Expression elseValue;
    private Expression condition;

    public ConditionalAssignmentStmt(String variableName, Expression thenValue, Expression elseValue, Expression condition) {
        this.variableName = variableName;
        this.thenValue = thenValue;
        this.elseValue = elseValue;
        this.condition = condition;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        MyStackInterface<IStmt> exeStack = state.getExeStack();
        AssignStmt thenStmt = new AssignStmt(variableName, thenValue);
        AssignStmt elseStmt = new AssignStmt(variableName, elseValue);
        exeStack.push(new IfStmt(condition, thenStmt, elseStmt));

        return state;
    }

    @Override
    public IStmt deepCopy() {
        return new ConditionalAssignmentStmt(variableName, condition.deepCopy(), thenValue.deepCopy(), elseValue.deepCopy());
    }

    @Override
    public MyDictionaryInterface<String, Type> typeCheck(MyDictionaryInterface<String, Type> typeEnv) throws MyException {
//        if (!(condition instanceof LogicExpression))
//            throw new MyException("The condition of IF is not a logic expression";

        //Type typeCondition = condition.typeCheck(typeEnv);
        if(condition instanceof LogicExpression){
            Type typeThen = thenValue.typeCheck(typeEnv.deepCopy());
            Type typeElse = elseValue.typeCheck(typeEnv.deepCopy());

            if (!typeThen.equals(typeElse))
                throw new MyException("The types of the then and else expressions are different");

            if (!typeEnv.isDefined(variableName))
                throw new MyException("The variable " + variableName + " is not defined");

            if (!typeEnv.lookup(variableName).equals(typeThen))
                throw new MyException("The type of the variable " + variableName + " is different from the type of the then expression");

            typeEnv.add(variableName, typeThen);

            return typeEnv;
        }
        else{
            throw new MyException("The condition of IF is not bool type");
        }
    }

    @Override
    public String toString() {
        return "ConditionalAssignmentStmt{" +
                "variableName='" + variableName + '\'' +
                ", thenValue=" + thenValue +
                ", elseValue=" + elseValue +
                ", condition=" + condition +
                '}';
    }
}
