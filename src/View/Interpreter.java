package View;

import Controller.Controller;
import Model.ADT.*;
import Model.Command.ExitCommand;
import Model.Command.RunExample;
import Model.Expression.*;
import Model.ProgramState;
import Model.Statement.*;
import Model.Type.BoolType;
import Model.Type.IntType;
import Model.Type.ReferenceType;
import Model.Type.StringType;
import Model.Value.BoolValue;
import Model.Value.IntValue;
import Model.Value.StringValue;
import Model.Value.Value;
import Repository.Repository;

import java.io.BufferedReader;

public class Interpreter {
    public static void main(String[] args){
        /*
         * Example1:
         *  int v; v=2;Print(v)  is represented as:
         * IStmt ex1= new CompoundStmt(new VariableDeclarationStmt("v",new IntType()),
         * new CompoundStmt(new AssignStmt("v",new ValueExpression(new IntValue(2))), new PrintStmt(new VariableExpression("v"))));
         * */
        IStmt ex1 = new CompoundStmt(new VariableDeclarationStmt("v", new IntType()),
                new CompoundStmt(new AssignStmt("v", new ValueExpression(new IntValue(2))), new PrintStmt(new VariableExpression("v"))));
        ProgramState State1 = new ProgramState(new MyStack<IStmt>(), new MyDictionary<String, Value>(),new MyDictionary<StringValue, BufferedReader>(), new MyList<Value>(), new MyHeap<Integer, Value>(), ex1);
        Repository Repo1 = new Repository(State1, "log1.txt");
        Controller Controller1 = new Controller(Repo1);
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
        ProgramState State2 = new ProgramState(new MyStack<IStmt>(), new MyDictionary<String, Value>(),new MyDictionary<StringValue, BufferedReader>(), new MyList<Value>(), new MyHeap<Integer, Value>(), ex2);
        Repository Repo2 = new Repository(State2, "log2.txt");
        Controller Controller2 = new Controller(Repo2);
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
        ProgramState State3 = new ProgramState(new MyStack<IStmt>(), new MyDictionary<String, Value>(), new MyDictionary<StringValue, BufferedReader>(), new MyList<Value>(), new MyHeap<Integer, Value>(),ex3);
        Repository Repo3 = new Repository(State3, "log3.txt");
        Controller Controller3 = new Controller(Repo3);
        /*
        Example 4:
        string varf;
        varf="test.in";
        openRFile(varf);
        int varc;
        readFile(varf,varc);
        print(varc);
        readFile(varf,varc);
        print(varc);
        closeRFile(varf);
         */

        IStmt ex4 = new CompoundStmt(new VariableDeclarationStmt("varf", new StringType()),
                new CompoundStmt(new AssignStmt("varf", new ValueExpression(new StringValue("test.in"))),
                new CompoundStmt(new OpenReadFileStmt(new VariableExpression("varf")),
                new CompoundStmt(new VariableDeclarationStmt("varc", new IntType()),
                new CompoundStmt(new ReadFileStmt(new VariableExpression("varf"), new StringValue("varc")),
                new CompoundStmt(new PrintStmt(new VariableExpression("varc")),
                new CompoundStmt(new ReadFileStmt(new VariableExpression("varf"), new StringValue("varc")),
                new CompoundStmt(new PrintStmt(new VariableExpression("varc")),
                new CloseReadFileStmt(new VariableExpression("varf"))))))))));
        ProgramState State4 = new ProgramState(new MyStack<IStmt>(), new MyDictionary<String, Value>(), new MyDictionary<StringValue, BufferedReader>(), new MyList<Value>(), new MyHeap<Integer, Value>(),ex4);
        Repository Repo4 = new Repository(State4, "log4.txt");
        Controller Controller4 = new Controller(Repo4);


        /*
        Example 5:
         Ref int v;new(v,20);
         Ref Ref int a;
          new(a,v);
          print(rH(v));
          print(rH(rH(a))+5)

         */
        IStmt ex5 = new CompoundStmt(new VariableDeclarationStmt("v", new ReferenceType(new IntType())),
                new CompoundStmt(new NewStmt("v", new ValueExpression(new IntValue(20))),
                new CompoundStmt(new VariableDeclarationStmt("a", new ReferenceType(new ReferenceType(new IntType()))),
                new CompoundStmt(new NewStmt("a", new VariableExpression("v")),
                new CompoundStmt(new PrintStmt(new ReadHeapExpression(new VariableExpression("v"))),
                new PrintStmt(new ArithmeticExpression('+', new ReadHeapExpression(new ReadHeapExpression(new VariableExpression("a"))), new ValueExpression(new IntValue(5)))))))));
        ProgramState State5 = new ProgramState(new MyStack<IStmt>(), new MyDictionary<String, Value>(),new MyDictionary<StringValue, BufferedReader>(), new MyList<Value>(), new MyHeap<Integer, Value>(), ex5);
        Repository Repo5 = new Repository(State5, "log5.txt");
        Controller Controller5 = new Controller(Repo5);

        /*
         Ref int v;
         new(v,20);
         print(rH(v));
         wH(v,30);
         print(rH(v)+5);
        */
        IStmt ex6 = new CompoundStmt(new VariableDeclarationStmt("v", new ReferenceType(new IntType())),
                new CompoundStmt(new NewStmt("v", new ValueExpression(new IntValue(20))),
                new CompoundStmt(new PrintStmt(new ReadHeapExpression(new VariableExpression("v"))),
                new CompoundStmt(new WriteHeapStmt("v", new ValueExpression(new IntValue(30))),
                new PrintStmt(new ArithmeticExpression('+', new ReadHeapExpression(new VariableExpression("v")), new ValueExpression(new IntValue(5))))))));
        ProgramState State6 = new ProgramState(new MyStack<IStmt>(), new MyDictionary<String, Value>(),new MyDictionary<StringValue, BufferedReader>(), new MyList<Value>(), new MyHeap<Integer, Value>(), ex6);
        Repository Repo6 = new Repository(State6, "log6.txt");
        Controller Controller6 = new Controller(Repo6);
        /*
         int v;
         v=4;
         (while (v>0) print(v);v=v-1);
         print(v);
        */
        IStmt ex7 = new CompoundStmt(new VariableDeclarationStmt("v", new IntType()),
                new CompoundStmt(new AssignStmt("v", new ValueExpression(new IntValue(4))),
                new CompoundStmt(new WhileStmt(new LogicExpression(new VariableExpression("v"), new ValueExpression(new IntValue(0)), ">"),
                new CompoundStmt(new PrintStmt(new VariableExpression("v")), new AssignStmt("v", new ArithmeticExpression('-', new VariableExpression("v"), new ValueExpression(new IntValue(1)))))),
                new PrintStmt(new VariableExpression("v")))));
        ProgramState State7 = new ProgramState(new MyStack<IStmt>(), new MyDictionary<String, Value>(),new MyDictionary<StringValue, BufferedReader>(), new MyList<Value>(), new MyHeap<Integer, Value>(), ex7);
        Repository Repo7 = new Repository(State7, "log7.txt");
        Controller Controller7 = new Controller(Repo7);

        /* 
        int v;
        Ref int a;
        v=10;
        new(a,22);
        thread(wH(a,30);v=32;print(v);print(rH(a)));
        print(v);
        print(rH(a));
        */
        IStmt ex8 = new CompoundStmt(new VariableDeclarationStmt("v", new IntType()),
                new CompoundStmt(new VariableDeclarationStmt("a", new ReferenceType(new IntType())),
                new CompoundStmt(new AssignStmt("v", new ValueExpression(new IntValue(10))),
                new CompoundStmt(new NewStmt("a", new ValueExpression(new IntValue(22))),
                new CompoundStmt(new ThreadStmt(new CompoundStmt(new WriteHeapStmt("a", new ValueExpression(new IntValue(30))),
                new CompoundStmt(new AssignStmt("v", new ValueExpression(new IntValue(32))),
                new CompoundStmt(new PrintStmt(new VariableExpression("v")),
                new PrintStmt(new ReadHeapExpression(new VariableExpression("a"))))))),
                new CompoundStmt(new PrintStmt(new VariableExpression("v")),
                new PrintStmt(new ReadHeapExpression(new VariableExpression("a")))))))));
        ProgramState State8 = new ProgramState(new MyStack<IStmt>(), new MyDictionary<String, Value>(),new MyDictionary<StringValue, BufferedReader>(), new MyList<Value>(), new MyHeap<Integer, Value>(), ex8);
        Repository Repo8 = new Repository(State8, "log8.txt");
        Controller Controller8 = new Controller(Repo8);


        /*
        Ref int v;
        Ref int a;
        new(v,10);
        new(a,22);
        while( (rH(v)>0) fork(wH(a,v);print(rH(a)));wH(v,rH(v)-1) );

        */
        IStmt ex9 = new CompoundStmt(new VariableDeclarationStmt("v", new ReferenceType(new IntType())),
                new CompoundStmt(new VariableDeclarationStmt("a", new ReferenceType(new IntType())),
                new CompoundStmt(new NewStmt("v", new ValueExpression(new IntValue(10))),
                new CompoundStmt(new NewStmt("a", new ValueExpression(new IntValue(22))),
                new WhileStmt(new LogicExpression(new ReadHeapExpression(new VariableExpression("v")), new ValueExpression(new IntValue(0)), ">"),
                new CompoundStmt(new ThreadStmt(new CompoundStmt(new WriteHeapStmt("a", new ReadHeapExpression(new VariableExpression("v"))),
                new PrintStmt(new ReadHeapExpression(new VariableExpression("a"))))),
                new WriteHeapStmt("v", new ArithmeticExpression('-', new ReadHeapExpression(new VariableExpression("v")), new ValueExpression(new IntValue(1))))))))));
        ProgramState State9 = new ProgramState(new MyStack<IStmt>(), new MyDictionary<String, Value>(),new MyDictionary<StringValue, BufferedReader>(), new MyList<Value>(), new MyHeap<Integer, Value>(), ex9);
        Repository Repo9 = new Repository(State9, "log9.txt");
        Controller Controller9 = new Controller(Repo9);
        

        TextMenu menu = new TextMenu();
        menu.addCommand(new ExitCommand("0", "exit"));
        menu.addCommand(new RunExample("1",ex1.toString(),Controller1));
        menu.addCommand(new RunExample("2",ex2.toString(),Controller2));
        menu.addCommand(new RunExample("3",ex3.toString(),Controller3));
        menu.addCommand(new RunExample("4",ex4.toString(),Controller4));
        menu.addCommand(new RunExample("5",ex5.toString(),Controller5));
        menu.addCommand(new RunExample("6",ex6.toString(),Controller6));
        menu.addCommand(new RunExample("7",ex7.toString(),Controller7));
        menu.addCommand(new RunExample("8",ex8.toString(),Controller8));
        menu.addCommand(new RunExample("9",ex9.toString(),Controller9));
        //TODO TEST 8
        menu.show();
    }
}

