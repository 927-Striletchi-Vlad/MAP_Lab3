package com.example.lab6map;

import Controller.Controller;
import Exception.MyException;
import GuiClasses.HeapTableEntry;
import GuiClasses.LockTableEntry;
import Model.ADT.*;
import Model.Expression.*;
import Model.ProgramState;
import Model.Statement.*;
import Model.Type.BoolType;
import Model.Type.IntType;
import Model.Type.ReferenceType;
import Model.Type.StringType;
import Model.Type.Type;
import Model.Value.BoolValue;
import Model.Value.IntValue;
import Model.Value.StringValue;
import Model.Value.Value;
import Repository.Repository;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelloApplication extends Application {
    private Controller currController;
    int currProgramStateId = 0;

    public void setCurrProgramStateId(int currProgramStateId) {
        this.currProgramStateId = currProgramStateId;
    }

    public int getCurrProgramStateId() {
        return currProgramStateId;
    }

    public Controller getController() {
        return currController;
    }

    public void setController(Controller controller) {
        this.currController = controller;
    }

    public void oneStep(){
        currController.oneStep();
    }

    public void oneStepNoCleanup() {
        currController.oneStepNoCleanup();
    }

    public void cleanup() {
        currController.cleanup();
    }

    public boolean isCompleted() {
        return currController.isCompleted();
    }

    public boolean isActuallyCompleted() {
        return currController.isActuallyCompleted();
    }

    public ProgramState getCurrProgramState() throws MyException {
//        return currController.getProgramStateById(currProgramStateId);
        return currController.getProgramStates().get(0);
    }


    public ProgramState getProgramStateById(int id) throws MyException {
        return currController.getProgramStateById(id);
    }

    public List<ProgramState> getProgramStates() {
        return currController.getProgramStates();
    }

    @Override
    public void start(Stage stage) throws IOException {
        /*
         * Example1:
         *  int v; v=2;Print(v)  is represented as:
         * IStmt ex1= new CompoundStmt(new VariableDeclarationStmt("v",new IntType()),
         * new CompoundStmt(new AssignStmt("v",new ValueExpression(new IntValue(2))), new PrintStmt(new VariableExpression("v"))));
         * */
        IStmt ex1 = new CompoundStmt(new VariableDeclarationStmt("v", new IntType()),
                new CompoundStmt(new AssignStmt("v", new ValueExpression(new IntValue(2))), new PrintStmt(new VariableExpression("v"))));
        try{
            ex1.typeCheck(new MyDictionary<String, Type>());
        }
        catch(MyException e){
            System.out.println("ex1: "+e.getMessage());
            return;
        }
        ProgramState State1 = new ProgramState(new MyStack<IStmt>(), new MyDictionary<String, Value>(), new MyDictionary<StringValue, BufferedReader>(), new MyList<Value>(), new MyHeap<Integer, Value>(), ex1, new MyLockHeap<Integer, Value>());
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
        try{
            ex2.typeCheck(new MyDictionary<String, Type>());
        }
        catch(MyException e){
            System.out.println("ex2: "+e.getMessage());
            return;
        }
        ProgramState State2 = new ProgramState(new MyStack<IStmt>(), new MyDictionary<String, Value>(), new MyDictionary<StringValue, BufferedReader>(), new MyList<Value>(), new MyHeap<Integer, Value>(), ex2, new MyLockHeap<Integer, Value>());
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
        try{
            ex3.typeCheck(new MyDictionary<String, Type>());
        }
        catch(MyException e){
            System.out.println("ex3: "+e.getMessage());
            return;
        }
        ProgramState State3 = new ProgramState(new MyStack<IStmt>(), new MyDictionary<String, Value>(), new MyDictionary<StringValue, BufferedReader>(), new MyList<Value>(), new MyHeap<Integer, Value>(), ex3, new MyLockHeap<Integer, Value>());
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
        try{
            ex4.typeCheck(new MyDictionary<String, Type>());
        }
        catch(MyException e){
            System.out.println("ex4: "+e.getMessage());
            return;
        }
        ProgramState State4 = new ProgramState(new MyStack<IStmt>(), new MyDictionary<String, Value>(), new MyDictionary<StringValue, BufferedReader>(), new MyList<Value>(), new MyHeap<Integer, Value>(),ex4, new MyLockHeap<Integer, Value>());
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
        try{
            ex5.typeCheck(new MyDictionary<String, Type>());
        }
        catch(MyException e){
            System.out.println("ex5: "+e.getMessage());
            return;
        }
        ProgramState State5 = new ProgramState(new MyStack<IStmt>(), new MyDictionary<String, Value>(),new MyDictionary<StringValue, BufferedReader>(), new MyList<Value>(), new MyHeap<Integer, Value>(), ex5, new MyLockHeap<Integer, Value>());
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
        try{
            ex6.typeCheck(new MyDictionary<String, Type>());
        }
        catch(MyException e){
            System.out.println("ex6: "+e.getMessage());
            return;
        }
        ProgramState State6 = new ProgramState(new MyStack<IStmt>(), new MyDictionary<String, Value>(),new MyDictionary<StringValue, BufferedReader>(), new MyList<Value>(), new MyHeap<Integer, Value>(), ex6, new MyLockHeap<Integer, Value>());
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
        try{
            ex7.typeCheck(new MyDictionary<String, Type>());
        }
        catch(MyException e){
            System.out.println("ex7: "+e.getMessage());
            return;
        }
        ProgramState State7 = new ProgramState(new MyStack<IStmt>(), new MyDictionary<String, Value>(),new MyDictionary<StringValue, BufferedReader>(), new MyList<Value>(), new MyHeap<Integer, Value>(), ex7, new MyLockHeap<Integer, Value>());
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
        try{
            ex8.typeCheck(new MyDictionary<String, Type>());
        }
        catch(MyException e){
            System.out.println("ex8: "+e.getMessage());
            return;
        }
        ProgramState State8 = new ProgramState(new MyStack<IStmt>(), new MyDictionary<String, Value>(),new MyDictionary<StringValue, BufferedReader>(), new MyList<Value>(), new MyHeap<Integer, Value>(), ex8, new MyLockHeap<Integer, Value>());
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
                        //new CompoundStmt(new VariableDeclarationStmt("a", new ReferenceType(new BoolType())),
                        new CompoundStmt(new NewStmt("v", new ValueExpression(new IntValue(10))),
                                new CompoundStmt(new NewStmt("a", new ValueExpression(new IntValue(22))),
                                        new WhileStmt(new LogicExpression(new ReadHeapExpression(new VariableExpression("v")), new ValueExpression(new IntValue(0)), ">"),
                                                new CompoundStmt(new ThreadStmt(new CompoundStmt(new WriteHeapStmt("a", new ReadHeapExpression(new VariableExpression("v"))),
                                                        new PrintStmt(new ReadHeapExpression(new VariableExpression("a"))))),
                                                        new WriteHeapStmt("v", new ArithmeticExpression('-', new ReadHeapExpression(new VariableExpression("v")), new ValueExpression(new IntValue(1))))))))));
        try{
            ex9.typeCheck(new MyDictionary<String, Type>());
        }
        catch(MyException e){
            System.out.println("ex9: "+e.getMessage());
            return;
        }
        ProgramState State9 = new ProgramState(new MyStack<IStmt>(), new MyDictionary<String, Value>(),new MyDictionary<StringValue, BufferedReader>(), new MyList<Value>(), new MyHeap<Integer, Value>(), ex9, new MyLockHeap<Integer, Value>());
        Repository Repo9 = new Repository(State9, "log9.txt");
        Controller Controller9 = new Controller(Repo9);

        /*
        Ref int a; new(a,20);
        (for(v=0;v<3;v=v+1) fork(print(v);v=v*rh(a)));
        print(rh(a));
         */
        IStmt ex10 = new CompoundStmt(new VariableDeclarationStmt("a", new ReferenceType(new IntType())),
                new CompoundStmt(new NewStmt("a", new ValueExpression(new IntValue(20))),
                        new CompoundStmt(new ForStmt("v", new ValueExpression(new IntValue(0)), new ValueExpression(new IntValue(3)), new ArithmeticExpression('+', new VariableExpression("v"), new ValueExpression(new IntValue(1))),
                                new ThreadStmt(new CompoundStmt(new PrintStmt(new VariableExpression("v")),
                                        new AssignStmt("v", new ArithmeticExpression('*', new VariableExpression("v"), new ReadHeapExpression(new VariableExpression("a"))))))
                                        ),new PrintStmt(new ReadHeapExpression(new VariableExpression("a"))) )));
        try{
            ex10.typeCheck(new MyDictionary<String, Type>());
        }
        catch(MyException e){
            System.out.println("ex10: "+e.getMessage());
            return;
        }
        ProgramState State10 = new ProgramState(new MyStack<IStmt>(), new MyDictionary<String, Value>(),new MyDictionary<StringValue, BufferedReader>(), new MyList<Value>(), new MyHeap<Integer, Value>(), ex10, new MyLockHeap<Integer, Value>());
        Repository Repo10 = new Repository(State10, "log10.txt");
        Controller Controller10 = new Controller(Repo10);

        /*
        Ref int v1; Ref int v2; int x; int q;
        new(v1,20);new(v2,30);newLock(x);
        fork(
         fork(
         lock(x);wh(v1,rh(v1)-1);unlock(x)
         );
         lock(x);wh(v1,rh(v1)*10);unlock(x)
        );
        lock(x); print(rh(v1)); unlock(x);
        * */
        IStmt ex11 = new CompoundStmt(new VariableDeclarationStmt("v1", new ReferenceType(new IntType())),
                new CompoundStmt(new VariableDeclarationStmt("v2", new ReferenceType(new IntType())),
                        new CompoundStmt(new VariableDeclarationStmt("x", new IntType()),
                                new CompoundStmt(new VariableDeclarationStmt("q", new IntType()),
                                        new CompoundStmt(new NewStmt("v1", new ValueExpression(new IntValue(20))),
                                                new CompoundStmt(new NewStmt("v2", new ValueExpression(new IntValue(30))),
                                                        new CompoundStmt(new NewLockStmt("x"),
                                                                new CompoundStmt(new ThreadStmt(new CompoundStmt(new ThreadStmt(new CompoundStmt(new LockStmt("x"),
                                                                        new CompoundStmt(new WriteHeapStmt("v1", new ArithmeticExpression('-', new ReadHeapExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(1)))),
                                                                                new UnlockStmt("x")))),
                                                                        new CompoundStmt(new LockStmt("x"),
                                                                                new CompoundStmt(new WriteHeapStmt("v1", new ArithmeticExpression('*', new ReadHeapExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(10)))),
                                                                                        new UnlockStmt("x"))))),
                                                                        new CompoundStmt(new LockStmt("x"),
                                                                                new CompoundStmt(new PrintStmt(new ReadHeapExpression(new VariableExpression("v1"))),
                                                                                        new UnlockStmt("x")))))))))));
        try{
            ex11.typeCheck(new MyDictionary<String, Type>());
        }
        catch(MyException e){
            System.out.println("ex11: "+e.getMessage());
            return;
        }
        ProgramState State11 = new ProgramState(new MyStack<IStmt>(), new MyDictionary<String, Value>(),new MyDictionary<StringValue, BufferedReader>(), new MyList<Value>(), new MyHeap<Integer, Value>(), ex11, new MyLockHeap<Integer, Value>());
        Repository Repo11 = new Repository(State11, "log11.txt");
        Controller Controller11 = new Controller(Repo11);

        /*
        Ref int v1; Ref int v2; int x; int q;
        new(v1,20);new(v2,30);newLock(x);
        fork(
         fork(
         lock(x);wh(v1,rh(v1)-1);unlock(x)
         );
         lock(x);wh(v1,rh(v1)*10);unlock(x)
        );newLock(q);
        fork(
         fork(lock(q);wh(v2,rh(v2)+5);unlock(q));
         lock(q);wh(v2,rh(v2)*10);unlock(q)
        );
        nop;nop;nop;nop;
        lock(x); print(rh(v1)); unlock(x);
        lock(q); print(rh(v2)); unlock(q);
        * */
        IStmt ex12 = new CompoundStmt(new VariableDeclarationStmt("v1", new ReferenceType(new IntType())),
                new CompoundStmt(new VariableDeclarationStmt("v2", new ReferenceType(new IntType())),
                        new CompoundStmt(new VariableDeclarationStmt("x", new IntType()),
                                new CompoundStmt(new VariableDeclarationStmt("q", new IntType()),
                                        new CompoundStmt(new NewStmt("v1", new ValueExpression(new IntValue(20))),
                                                new CompoundStmt(new NewStmt("v2", new ValueExpression(new IntValue(30))),
                                                        new CompoundStmt(new NewLockStmt("x"),
                                                                new CompoundStmt(new ThreadStmt(new CompoundStmt(new ThreadStmt(new CompoundStmt(new LockStmt("x"),
                                                                        new CompoundStmt(new WriteHeapStmt("v1", new ArithmeticExpression('-', new ReadHeapExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(1)))),
                                                                                new UnlockStmt("x")))),
                                                                        new CompoundStmt(new LockStmt("x"),
                                                                                new CompoundStmt(new WriteHeapStmt("v1", new ArithmeticExpression('*', new ReadHeapExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(10)))),
                                                                                        new UnlockStmt("x"))))),
                                                                        new CompoundStmt(new NewLockStmt("q"),
                                                                                new CompoundStmt(new ThreadStmt(new CompoundStmt(new ThreadStmt(new CompoundStmt(new LockStmt("q"),
                                                                                        new CompoundStmt(new WriteHeapStmt("v2", new ArithmeticExpression('+', new ReadHeapExpression(new VariableExpression("v2")), new ValueExpression(new IntValue(5)))),
                                                                                                new UnlockStmt("q")))),
                                                                                        new CompoundStmt(new LockStmt("q"),
                                                                                                new CompoundStmt(new WriteHeapStmt("v2", new ArithmeticExpression('*', new ReadHeapExpression(new VariableExpression("v2")), new ValueExpression(new IntValue(10)))),
                                                                                                        new UnlockStmt("q"))))),
                                                                                        new CompoundStmt(new NoOperationStmt(),
                                                                                                new CompoundStmt(new NoOperationStmt(),
                                                                                                        new CompoundStmt(new NoOperationStmt(),
                                                                                                                new CompoundStmt(new NoOperationStmt(),
                                                                                                                        new CompoundStmt(new LockStmt("x"),
                                                                                                                                new CompoundStmt(new PrintStmt(new ReadHeapExpression(new VariableExpression("v1"))),
                                                                                                                                        new CompoundStmt(new UnlockStmt("x"),
                                                                                                                                                new CompoundStmt(new LockStmt("q"),
                                                                                                                                                        new CompoundStmt(new PrintStmt(new ReadHeapExpression(new VariableExpression("v2"))),
                                                                                                                                                                new UnlockStmt("q"))))))))))))))))))));
        try{
            ex12.typeCheck(new MyDictionary<String, Type>());
        }
        catch(MyException e){
            System.out.println("ex12: "+e.getMessage());
            return;
        }
        ProgramState State12 = new ProgramState(new MyStack<IStmt>(), new MyDictionary<String, Value>(),new MyDictionary<StringValue, BufferedReader>(), new MyList<Value>(), new MyHeap<Integer, Value>(), ex12, new MyLockHeap<Integer, Value>());
        Repository Repo12 = new Repository(State12, "log12.txt");
        Controller Controller12 = new Controller(Repo12);




        //========================================================================
        ObservableList<String> names = FXCollections.observableArrayList();
        Map<String, Controller> controllers = new HashMap<>();
        controllers.put("Ex1 " + ex1.toString(), Controller1);
        controllers.put("Ex2 " + ex2.toString(), Controller2);
        controllers.put("Ex3 " + ex3.toString(), Controller3);
        controllers.put("Ex4 " + ex4.toString(), Controller4);
        controllers.put("Ex5 " + ex5.toString(), Controller5);
        controllers.put("Ex6 " + ex6.toString(), Controller6);
        controllers.put("Ex7 " + ex7.toString(), Controller7);
        controllers.put("Ex8 " + ex8.toString(), Controller8);
        controllers.put("Ex9 " + ex9.toString(), Controller9);
        controllers.put("Ex10 " + ex10.toString(), Controller10);
        controllers.put("Ex11 " + ex11.toString(), Controller11);
        controllers.put("Ex12 " + ex12.toString(), Controller12);

        names.addAll(controllers.keySet());


        ListView<String> list = new ListView<String>(names);
        list.setPrefSize(200, 200);
        list.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    String name = list.getSelectionModel().getSelectedItem();
                    Controller controller = controllers.get(name);
                    //controllers.remove(name);
                    names.remove(name);
                    setController(controller);
                    setCurrProgramStateId(controller.getFirstProgramStateId());
                    try {
                        List<ProgramState> allStates = controller.getProgramStates();
                        ProgramState currState = allStates.get(0);
                        MyHeapInterface<Integer, Value> currHeap = currState.getHeap();

                        Stage stage2 = new Stage();
                        stage2.setTitle("Execution simulation");

                        Label label1 = new Label("no. of PrgStates:");
                        TextField noProgramStates = new TextField();

                        Label label2 = new Label("HeapTable:");



                        TableView<HeapTableEntry> heapTable = new TableView<>();
                        heapTable.setEditable(true);
                        ObservableList<HeapTableEntry> heapTableEntries = FXCollections.observableArrayList();
                        for (Map.Entry<Integer, Value> entry : currHeap.getContent().entrySet()) {
                            heapTableEntries.add(new HeapTableEntry(entry.getKey().toString(), entry.getValue().toString()));
                        }

                        TableColumn heapTableAddress = new TableColumn<>("Address");
                        TableColumn heapTableValue = new TableColumn<>("Value");
                        heapTableAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
                        heapTableValue.setCellValueFactory(new PropertyValueFactory<>("value"));
                        heapTable.setItems(heapTableEntries);
                        heapTable.getColumns().addAll(heapTableAddress, heapTableValue);

                        Label label3 = new Label("LockTable:");


                        TableView<LockTableEntry> lockTable = new TableView<>();
                        lockTable.setEditable(true);
                        ObservableList<LockTableEntry> lockTableEntries = FXCollections.observableArrayList();
                        for (Map.Entry<Integer, Value> entry : currState.getLockHeap().getContent().entrySet()) {
                            lockTableEntries.add(new LockTableEntry(entry.getKey().toString(), entry.getValue().toString()));
                        }

                        TableColumn lockTableAddress = new TableColumn<>("Address");
                        TableColumn lockTableValue = new TableColumn<>("Value");
                        lockTableAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
                        lockTableValue.setCellValueFactory(new PropertyValueFactory<>("value"));
                        lockTable.setItems(lockTableEntries);
                        lockTable.getColumns().addAll(lockTableAddress, lockTableValue);


                        Label label4 = new Label("Output:");
                        ListView<String> output = new ListView<>();
                        ObservableList<String> outputList = FXCollections.observableArrayList();

                        for (Value v : currState.getOutput().getContent()) {
                            outputList.add(v.toString());
                        }
                        output.setItems(outputList);

                        Label label5 = new Label("All Program States:");
                        ListView<String> programStates = new ListView<>();
                        ObservableList<String> programStatesList = FXCollections.observableArrayList();
                        for (ProgramState state : allStates) {
                            programStatesList.add(String.valueOf(state.getId()));
                        }
                        programStates.setItems(programStatesList);

                        ListView<String> exeStack = new ListView<>();
                        ObservableList<String> exeStackList = FXCollections.observableArrayList();
                        for (IStmt stmt : currState.getStack().getContent()) {
                            exeStackList.add(stmt.toString());
                        }

                        programStates.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                if (event.getClickCount() == 2) {
                                    String id = programStates.getSelectionModel().getSelectedItem();
                                    setCurrProgramStateId(Integer.parseInt(id));

                                    exeStackList.clear();
                                    try {
                                        for (IStmt stmt : getProgramStateById(currProgramStateId).getStack().getContent()) {
                                            exeStackList.add(stmt.toString());
                                        }
                                    } catch (MyException e) {
                                        throw new RuntimeException(e);
                                    }
                                    exeStack.setItems(exeStackList);
                                }
                            }
                        });


                        Button oneStep = new Button("One Step");
                        oneStep.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {

                                //cleanup();
                                oneStepNoCleanup();

                                if (isActuallyCompleted()) {
                                    outputList.clear();
                                    exeStackList.clear();
                                    heapTableEntries.clear();
                                    lockTableEntries.clear();
                                    programStatesList.clear();
                                    noProgramStates.clear();
                                    oneStep.setDisable(true);
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle("Information Dialog");
                                    alert.setHeaderText(null);
                                    alert.setContentText("No more states to execute!");
                                    alert.showAndWait();
                                    return;
                                }

                                noProgramStates.setText(String.valueOf(controller.getProgramStates().size()));

                                outputList.clear();
                                try {
                                    for (Value v : getCurrProgramState().getOutput().getContent()) {
                                        outputList.add(v.toString());
                                    }
                                } catch (MyException e) {
                                    throw new RuntimeException(e);
                                }
                                output.setItems(outputList);
                                programStatesList.clear();
                                for (ProgramState state : getProgramStates()) {
                                    programStatesList.add(String.valueOf(state.getId()));
                                }
                                programStates.setItems(programStatesList);
                                exeStackList.clear();
                                try {
                                    for (IStmt stmt : getCurrProgramState().getStack().getContent()) {
                                        exeStackList.add(stmt.toString());
                                    }
                                } catch (MyException e) {
                                    throw new RuntimeException(e);
                                }
                                exeStack.setItems(exeStackList);
                                heapTableEntries.clear();
                                try {
                                    for (Map.Entry<Integer, Value> entry : getCurrProgramState().getHeap().getContent().entrySet()) {
                                        heapTableEntries.add(new HeapTableEntry(entry.getKey().toString(), entry.getValue().toString()));
                                    }
                                } catch (MyException e) {
                                    throw new RuntimeException(e);
                                }
                                heapTable.setItems(heapTableEntries);

                                lockTableEntries.clear();
                                try {
                                    for (Map.Entry<Integer, Value> entry : getCurrProgramState().getLockHeap().getContent().entrySet()) {
                                        lockTableEntries.add(new LockTableEntry(entry.getKey().toString(), entry.getValue().toString()));
                                    }
                                } catch (MyException e) {
                                    throw new RuntimeException(e);
                                }

                                cleanup();
                            }
                        });

                        VBox layout2 = new VBox(20);
                        layout2.getChildren().addAll(label1, noProgramStates, label2, heapTable, label3, lockTable, label4, output, label5, programStates, exeStack, oneStep);
                        Scene scene2 = new Scene(layout2, 600, 600);
                        stage2.setScene(scene2);
                        stage2.show();

                } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        VBox layout = new VBox(20);
        layout.getChildren().addAll(list);
        Scene scene = new Scene(layout, 300, 250);

        stage.setTitle("Interpreter GUI");
        stage.setScene(scene);
        stage.show();
    }

                    public static void main(String[] args) {
        launch();
    }
}