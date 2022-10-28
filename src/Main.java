import Controller.Controller;
import Model.ADT.*;
import Model.Expression.ArithmeticExpression;
import Model.Expression.ValueExpression;
import Model.Expression.VariableExpression;
import Model.ProgramState;
import Model.Statement.*;
import Model.Type.BoolType;
import Model.Type.IntType;
import Model.Value.BoolValue;
import Model.Value.IntValue;
import Model.Value.Value;
import Repository.Repository;
import View.View;

public class Main {
    public static void main(String[] args) {
        /*
        * Example1:
        *  int v; v=2;Print(v)  is represented as:
        * IStmt ex1= new CompoundStmt(new VariableDeclarationStmt("v",new IntType()),
        * new CompoundStmt(new AssignStmt("v",new ValueExpression(new IntValue(2))), new PrintStmt(new VariableExpression("v"))));
        * */
        IStmt ex1 = new CompoundStmt(new VariableDeclarationStmt("v", new IntType()),
                new CompoundStmt(new AssignStmt("v", new ValueExpression(new IntValue(2))), new PrintStmt(new VariableExpression("v"))));

        /*
        * Example2:
        * int a;int b; a=2+3*5;b=a+1;Print(b) is represented as:
        * IStmt ex2 = new CompoundStmt( new VariableDeclarationStmt("a",new IntType()), new CompoundStmt(new VariableDeclarationStmt("b",new IntType()),
        * new CompoundStmt(new AssignStmt("a", new ArithmeticExpression('+',new ValueExpression(new IntValue(2)),
        * new ArithmeticExpression('*',new ValueExpression(new IntValue(3)), new ValueExpression(new IntValue(5))))),
        * new CompoundStmt(new AssignStmt("b",new ArithmeticExpression('+',new VariableExpression("a"), new ValueExpression(new IntValue(1)))),
        * new PrintStmt(new VariableExpression("b"))))) ;
        * */

         IStmt ex2 = new CompoundStmt( new VariableDeclarationStmt("a",new IntType()), new CompoundStmt(new VariableDeclarationStmt("b",new IntType()),
                new CompoundStmt(new AssignStmt("a", new ArithmeticExpression('+',new ValueExpression(new IntValue(2)),
                new ArithmeticExpression('*',new ValueExpression(new IntValue(3)), new ValueExpression(new IntValue(5))))),
                new CompoundStmt(new AssignStmt("b",new ArithmeticExpression('+',new VariableExpression("a"), new ValueExpression(new IntValue(1)))),
                new PrintStmt(new VariableExpression("b"))))));

         /*
         * Example3:
         * bool a; int v; a=true;(If a Then v=2 Else v=3);Print(v)   is represented as:
         * Stmt ex3 = new CompoundStmt(new VariableDeclarationStmt("a",new BoolType()),
         * new CompoundStmt(new VariableDeclarationStmt("v", new IntType()),new CompoundStmt(new AssignStmt("a", new ValueExpression(new BoolValue(true))),
         * new CompoundStmt(new IfStmt(new VariableExpression("a"),new AssignStmt("v",new ValueExpression(new IntValue(2))),
         * new AssignStmt("v", new ValueExpression(new IntValue(3)))), new PrintStmt(new VariableExpression("v")))))));
         * */

        IStmt ex3 = new CompoundStmt(new VariableDeclarationStmt("a",new BoolType()),
                new CompoundStmt(new VariableDeclarationStmt("v", new IntType()),new CompoundStmt(new AssignStmt("a", new ValueExpression(new BoolValue(true))),
                new CompoundStmt(new IfStmt(new VariableExpression("a"),new AssignStmt("v",new ValueExpression(new IntValue(2))),
                new AssignStmt("v", new ValueExpression(new IntValue(3)))), new PrintStmt(new VariableExpression("v"))))));

        ProgramState testState = new ProgramState(new MyStack<IStmt>(), new MyDictionary<String,Value>(), new MyList<Value>(), ex3);
        Repository testRepo = new Repository(testState);
        Controller testController = new Controller(testRepo);
        View testView = new View(testController);

        testView.debug();
        testView.run();
    }
}