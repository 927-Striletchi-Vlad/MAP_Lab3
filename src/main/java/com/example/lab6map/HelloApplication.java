package com.example.lab6map;

import Controller.Controller;
import Exception.MyException;
import GuiClasses.HeapTableEntry;
import GuiClasses.LatchTableEntry;
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
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import Parser.*;

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

    private void spawnInterpretWindow(Controller controller){
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

            Label label8 = new Label("LatchTable:");
            TableView<LatchTableEntry> latchTable = new TableView<>();
            latchTable.setEditable(true);
            ObservableList<LatchTableEntry> latchTableEntries = FXCollections.observableArrayList();
            for (Map.Entry<Integer, Integer> entry : currState.getLatchTable().getContent().entrySet()) {
                latchTableEntries.add(new LatchTableEntry(entry.getKey().toString(), entry.getValue().toString()));
            }
            latchTable.setItems(latchTableEntries);
            latchTable.getColumns().addAll(lockTableAddress, lockTableValue);


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
                        latchTableEntries.clear();
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
                    lockTable.setItems(lockTableEntries);

                    latchTableEntries.clear();
                    try {
                        for (Map.Entry<Integer, Integer> entry : getCurrProgramState().getLatchTable().getContent().entrySet()) {
                            latchTableEntries.add(new LatchTableEntry(entry.getKey().toString(), entry.getValue().toString()));
                        }
                    } catch (MyException e) {
                        throw new RuntimeException(e);
                    }
                    latchTable.setItems(latchTableEntries);

                    cleanup();
                }
            });

            VBox layout2 = new VBox(20);
            layout2.getChildren().addAll(label1, noProgramStates, label2, heapTable, label3, lockTable, label8, latchTable, label4, output, label5, programStates, exeStack, oneStep);
            Scene scene2 = new Scene(layout2, 600, 600);
            stage2.setScene(scene2);
            stage2.show();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }




    @Override
    public void start(Stage stage) throws IOException {
//        /*
//         * Example1:
//         *  int v; v=2;Print(v)  is represented as:
//         * IStmt ex1= new CompoundStmt(new VariableDeclarationStmt("v",new IntType()),
//         * new CompoundStmt(new AssignStmt("v",new ValueExpression(new IntValue(2))), new PrintStmt(new VariableExpression("v"))));
//         * */
//        IStmt ex1 = new CompoundStmt(new VariableDeclarationStmt("v", new IntType()),
//                new CompoundStmt(new AssignStmt("v", new ValueExpression(new IntValue(2))), new PrintStmt(new VariableExpression("v"))));
//        try{
//            ex1.typeCheck(new MyDictionary<String, Type>());
//        }
//        catch(MyException e){
//            System.out.println("ex1: "+e.getMessage());
//            return;
//        }
//        ProgramState State1 = new ProgramState(new MyStack<IStmt>(), new MyDictionary<String, Value>(), new MyDictionary<StringValue, BufferedReader>(), new MyList<Value>(), new MyHeap<Integer, Value>(), ex1, new MyLockHeap<Integer, Value>());
//        Repository Repo1 = new Repository(State1, "log1.txt");
//        Controller Controller1 = new Controller(Repo1);
//        /*
//         * Example2:
//         * int a;int b; a=2+3*5;b=a+1;Print(b) is represented as:
//         * IStmt ex2 = new CompoundStmt( new VariableDeclarationStmt("a",new IntType()), new CompoundStmt(new VariableDeclarationStmt("b",new IntType()),
//         * new CompoundStmt(new AssignStmt("a", new ArithmeticExpression('+',new ValueExpression(new IntValue(2)),
//         * new ArithmeticExpression('*',new ValueExpression(new IntValue(3)), new ValueExpression(new IntValue(5))))),
//         * new CompoundStmt(new AssignStmt("b",new ArithmeticExpression('+',new VariableExpression("a"), new ValueExpression(new IntValue(1)))),
//         * new PrintStmt(new VariableExpression("b"))))) ;
//         * */
//
//        IStmt ex2 = new CompoundStmt( new VariableDeclarationStmt("a",new IntType()), new CompoundStmt(new VariableDeclarationStmt("b",new IntType()),
//                new CompoundStmt(new AssignStmt("a", new ArithmeticExpression('+',new ValueExpression(new IntValue(2)),
//                        new ArithmeticExpression('*',new ValueExpression(new IntValue(3)), new ValueExpression(new IntValue(5))))),
//                        new CompoundStmt(new AssignStmt("b",new ArithmeticExpression('+',new VariableExpression("a"), new ValueExpression(new IntValue(1)))),
//                                new PrintStmt(new VariableExpression("b"))))));
//        try{
//            ex2.typeCheck(new MyDictionary<String, Type>());
//        }
//        catch(MyException e){
//            System.out.println("ex2: "+e.getMessage());
//            return;
//        }
//        ProgramState State2 = new ProgramState(new MyStack<IStmt>(), new MyDictionary<String, Value>(), new MyDictionary<StringValue, BufferedReader>(), new MyList<Value>(), new MyHeap<Integer, Value>(), ex2, new MyLockHeap<Integer, Value>());
//        Repository Repo2 = new Repository(State2, "log2.txt");
//        Controller Controller2 = new Controller(Repo2);
//        /*
//         * Example3:
//         * bool a; int v; a=true;(If a Then v=2 Else v=3);Print(v)   is represented as:
//         * Stmt ex3 = new CompoundStmt(new VariableDeclarationStmt("a",new BoolType()),
//         * new CompoundStmt(new VariableDeclarationStmt("v", new IntType()),new CompoundStmt(new AssignStmt("a", new ValueExpression(new BoolValue(true))),
//         * new CompoundStmt(new IfStmt(new VariableExpression("a"),new AssignStmt("v",new ValueExpression(new IntValue(2))),
//         * new AssignStmt("v", new ValueExpression(new IntValue(3)))), new PrintStmt(new VariableExpression("v")))))));
//         * */
//
//        IStmt ex3 = new CompoundStmt(new VariableDeclarationStmt("a",new BoolType()),
//                new CompoundStmt(new VariableDeclarationStmt("v", new IntType()),new CompoundStmt(new AssignStmt("a", new ValueExpression(new BoolValue(true))),
//                        new CompoundStmt(new IfStmt(new VariableExpression("a"),new AssignStmt("v",new ValueExpression(new IntValue(2))),
//                                new AssignStmt("v", new ValueExpression(new IntValue(3)))), new PrintStmt(new VariableExpression("v"))))));
//        try{
//            ex3.typeCheck(new MyDictionary<String, Type>());
//        }
//        catch(MyException e){
//            System.out.println("ex3: "+e.getMessage());
//            return;
//        }
//        ProgramState State3 = new ProgramState(new MyStack<IStmt>(), new MyDictionary<String, Value>(), new MyDictionary<StringValue, BufferedReader>(), new MyList<Value>(), new MyHeap<Integer, Value>(), ex3, new MyLockHeap<Integer, Value>());
//        Repository Repo3 = new Repository(State3, "log3.txt");
//        Controller Controller3 = new Controller(Repo3);
//        /*
//        Example 4:
//        string varf;
//        varf="test.in";
//        openRFile(varf);
//        int varc;
//        readFile(varf,varc);
//        print(varc);
//        readFile(varf,varc);
//        print(varc);
//        closeRFile(varf);
//         */
//
//        IStmt ex4 = new CompoundStmt(new VariableDeclarationStmt("varf", new StringType()),
//                new CompoundStmt(new AssignStmt("varf", new ValueExpression(new StringValue("test.in"))),
//                        new CompoundStmt(new OpenReadFileStmt(new VariableExpression("varf")),
//                                new CompoundStmt(new VariableDeclarationStmt("varc", new IntType()),
//                                        new CompoundStmt(new ReadFileStmt(new VariableExpression("varf"), new StringValue("varc")),
//                                                new CompoundStmt(new PrintStmt(new VariableExpression("varc")),
//                                                        new CompoundStmt(new ReadFileStmt(new VariableExpression("varf"), new StringValue("varc")),
//                                                                new CompoundStmt(new PrintStmt(new VariableExpression("varc")),
//                                                                        new CloseReadFileStmt(new VariableExpression("varf"))))))))));
//        try{
//            ex4.typeCheck(new MyDictionary<String, Type>());
//        }
//        catch(MyException e){
//            System.out.println("ex4: "+e.getMessage());
//            return;
//        }
//        ProgramState State4 = new ProgramState(new MyStack<IStmt>(), new MyDictionary<String, Value>(), new MyDictionary<StringValue, BufferedReader>(), new MyList<Value>(), new MyHeap<Integer, Value>(),ex4, new MyLockHeap<Integer, Value>());
//        Repository Repo4 = new Repository(State4, "log4.txt");
//        Controller Controller4 = new Controller(Repo4);
//
//
//        /*
//        Example 5:
//         Ref int v;new(v,20);
//         Ref Ref int a;
//          new(a,v);
//          print(rH(v));
//          print(rH(rH(a))+5)
//
//         */
//        IStmt ex5 = new CompoundStmt(new VariableDeclarationStmt("v", new ReferenceType(new IntType())),
//                new CompoundStmt(new NewStmt("v", new ValueExpression(new IntValue(20))),
//                        new CompoundStmt(new VariableDeclarationStmt("a", new ReferenceType(new ReferenceType(new IntType()))),
//                                new CompoundStmt(new NewStmt("a", new VariableExpression("v")),
//                                        new CompoundStmt(new PrintStmt(new ReadHeapExpression(new VariableExpression("v"))),
//                                                new PrintStmt(new ArithmeticExpression('+', new ReadHeapExpression(new ReadHeapExpression(new VariableExpression("a"))), new ValueExpression(new IntValue(5)))))))));
//        try{
//            ex5.typeCheck(new MyDictionary<String, Type>());
//        }
//        catch(MyException e){
//            System.out.println("ex5: "+e.getMessage());
//            return;
//        }
//        ProgramState State5 = new ProgramState(new MyStack<IStmt>(), new MyDictionary<String, Value>(),new MyDictionary<StringValue, BufferedReader>(), new MyList<Value>(), new MyHeap<Integer, Value>(), ex5, new MyLockHeap<Integer, Value>());
//        Repository Repo5 = new Repository(State5, "log5.txt");
//        Controller Controller5 = new Controller(Repo5);
//
//        /*
//         Ref int v;
//         new(v,20);
//         print(rH(v));
//         wH(v,30);
//         print(rH(v)+5);
//        */
//        IStmt ex6 = new CompoundStmt(new VariableDeclarationStmt("v", new ReferenceType(new IntType())),
//                new CompoundStmt(new NewStmt("v", new ValueExpression(new IntValue(20))),
//                        new CompoundStmt(new PrintStmt(new ReadHeapExpression(new VariableExpression("v"))),
//                                new CompoundStmt(new WriteHeapStmt("v", new ValueExpression(new IntValue(30))),
//                                        new PrintStmt(new ArithmeticExpression('+', new ReadHeapExpression(new VariableExpression("v")), new ValueExpression(new IntValue(5))))))));
//        try{
//            ex6.typeCheck(new MyDictionary<String, Type>());
//        }
//        catch(MyException e){
//            System.out.println("ex6: "+e.getMessage());
//            return;
//        }
//        ProgramState State6 = new ProgramState(new MyStack<IStmt>(), new MyDictionary<String, Value>(),new MyDictionary<StringValue, BufferedReader>(), new MyList<Value>(), new MyHeap<Integer, Value>(), ex6, new MyLockHeap<Integer, Value>());
//        Repository Repo6 = new Repository(State6, "log6.txt");
//        Controller Controller6 = new Controller(Repo6);
//        /*
//         int v;
//         v=4;
//         (while (v>0) print(v);v=v-1);
//         print(v);
//        */
//        IStmt ex7 = new CompoundStmt(new VariableDeclarationStmt("v", new IntType()),
//                new CompoundStmt(new AssignStmt("v", new ValueExpression(new IntValue(4))),
//                        new CompoundStmt(new WhileStmt(new LogicExpression(new VariableExpression("v"), new ValueExpression(new IntValue(0)), ">"),
//                                new CompoundStmt(new PrintStmt(new VariableExpression("v")), new AssignStmt("v", new ArithmeticExpression('-', new VariableExpression("v"), new ValueExpression(new IntValue(1)))))),
//                                new PrintStmt(new VariableExpression("v")))));
//        try{
//            ex7.typeCheck(new MyDictionary<String, Type>());
//        }
//        catch(MyException e){
//            System.out.println("ex7: "+e.getMessage());
//            return;
//        }
//        ProgramState State7 = new ProgramState(new MyStack<IStmt>(), new MyDictionary<String, Value>(),new MyDictionary<StringValue, BufferedReader>(), new MyList<Value>(), new MyHeap<Integer, Value>(), ex7, new MyLockHeap<Integer, Value>());
//        Repository Repo7 = new Repository(State7, "log7.txt");
//        Controller Controller7 = new Controller(Repo7);
//
//        /*
//        int v;
//        Ref int a;
//        v=10;
//        new(a,22);
//        thread(wH(a,30);v=32;print(v);print(rH(a)));
//        print(v);
//        print(rH(a));
//        */
//        IStmt ex8 = new CompoundStmt(new VariableDeclarationStmt("v", new IntType()),
//                new CompoundStmt(new VariableDeclarationStmt("a", new ReferenceType(new IntType())),
//                        new CompoundStmt(new AssignStmt("v", new ValueExpression(new IntValue(10))),
//                                new CompoundStmt(new NewStmt("a", new ValueExpression(new IntValue(22))),
//                                        new CompoundStmt(new ThreadStmt(new CompoundStmt(new WriteHeapStmt("a", new ValueExpression(new IntValue(30))),
//                                                new CompoundStmt(new AssignStmt("v", new ValueExpression(new IntValue(32))),
//                                                        new CompoundStmt(new PrintStmt(new VariableExpression("v")),
//                                                                new PrintStmt(new ReadHeapExpression(new VariableExpression("a"))))))),
//                                                new CompoundStmt(new PrintStmt(new VariableExpression("v")),
//                                                        new PrintStmt(new ReadHeapExpression(new VariableExpression("a")))))))));
//        try{
//            ex8.typeCheck(new MyDictionary<String, Type>());
//        }
//        catch(MyException e){
//            System.out.println("ex8: "+e.getMessage());
//            return;
//        }
//        ProgramState State8 = new ProgramState(new MyStack<IStmt>(), new MyDictionary<String, Value>(),new MyDictionary<StringValue, BufferedReader>(), new MyList<Value>(), new MyHeap<Integer, Value>(), ex8, new MyLockHeap<Integer, Value>());
//        Repository Repo8 = new Repository(State8, "log8.txt");
//        Controller Controller8 = new Controller(Repo8);
//
//
//        /*
//        Ref int v;
//        Ref int a;
//        new(v,10);
//        new(a,22);
//        while( (rH(v)>0) fork(wH(a,v);print(rH(a)));wH(v,rH(v)-1) );
//
//        */
//        IStmt ex9 = new CompoundStmt(new VariableDeclarationStmt("v", new ReferenceType(new IntType())),
//                new CompoundStmt(new VariableDeclarationStmt("a", new ReferenceType(new IntType())),
//                        //new CompoundStmt(new VariableDeclarationStmt("a", new ReferenceType(new BoolType())),
//                        new CompoundStmt(new NewStmt("v", new ValueExpression(new IntValue(10))),
//                                new CompoundStmt(new NewStmt("a", new ValueExpression(new IntValue(22))),
//                                        new WhileStmt(new LogicExpression(new ReadHeapExpression(new VariableExpression("v")), new ValueExpression(new IntValue(0)), ">"),
//                                                new CompoundStmt(new ThreadStmt(new CompoundStmt(new WriteHeapStmt("a", new ReadHeapExpression(new VariableExpression("v"))),
//                                                        new PrintStmt(new ReadHeapExpression(new VariableExpression("a"))))),
//                                                        new WriteHeapStmt("v", new ArithmeticExpression('-', new ReadHeapExpression(new VariableExpression("v")), new ValueExpression(new IntValue(1))))))))));
//        try{
//            ex9.typeCheck(new MyDictionary<String, Type>());
//        }
//        catch(MyException e){
//            System.out.println("ex9: "+e.getMessage());
//            return;
//        }
//        ProgramState State9 = new ProgramState(new MyStack<IStmt>(), new MyDictionary<String, Value>(),new MyDictionary<StringValue, BufferedReader>(), new MyList<Value>(), new MyHeap<Integer, Value>(), ex9, new MyLockHeap<Integer, Value>());
//        Repository Repo9 = new Repository(State9, "log9.txt");
//        Controller Controller9 = new Controller(Repo9);
//
//        /*
//        Ref int a; new(a,20);
//        (for(v=0;v<3;v=v+1) fork(print(v);v=v*rh(a)));
//        print(rh(a));
//         */
//        IStmt ex10 = new CompoundStmt(new VariableDeclarationStmt("a", new ReferenceType(new IntType())),
//                new CompoundStmt(new NewStmt("a", new ValueExpression(new IntValue(20))),
//                        new CompoundStmt(new ForStmt("v", new ValueExpression(new IntValue(0)), new ValueExpression(new IntValue(3)), new ArithmeticExpression('+', new VariableExpression("v"), new ValueExpression(new IntValue(1))),
//                                new ThreadStmt(new CompoundStmt(new PrintStmt(new VariableExpression("v")),
//                                        new AssignStmt("v", new ArithmeticExpression('*', new VariableExpression("v"), new ReadHeapExpression(new VariableExpression("a"))))))
//                                        ),new PrintStmt(new ReadHeapExpression(new VariableExpression("a"))) )));
//        try{
//            ex10.typeCheck(new MyDictionary<String, Type>());
//        }
//        catch(MyException e){
//            System.out.println("ex10: "+e.getMessage());
//            return;
//        }
//        ProgramState State10 = new ProgramState(new MyStack<IStmt>(), new MyDictionary<String, Value>(),new MyDictionary<StringValue, BufferedReader>(), new MyList<Value>(), new MyHeap<Integer, Value>(), ex10, new MyLockHeap<Integer, Value>());
//        Repository Repo10 = new Repository(State10, "log10.txt");
//        Controller Controller10 = new Controller(Repo10);
//
//        /*
//        Ref int v1; Ref int v2; int x; int q;
//        new(v1,20);new(v2,30);newLock(x);
//        fork(
//         fork(
//         lock(x);wh(v1,rh(v1)-1);unlock(x)
//         );
//         lock(x);wh(v1,rh(v1)*10);unlock(x)
//        );
//        lock(x); print(rh(v1)); unlock(x);
//        * */
//        IStmt ex11 = new CompoundStmt(new VariableDeclarationStmt("v1", new ReferenceType(new IntType())),
//                new CompoundStmt(new VariableDeclarationStmt("v2", new ReferenceType(new IntType())),
//                        new CompoundStmt(new VariableDeclarationStmt("x", new IntType()),
//                                new CompoundStmt(new VariableDeclarationStmt("q", new IntType()),
//                                        new CompoundStmt(new NewStmt("v1", new ValueExpression(new IntValue(20))),
//                                                new CompoundStmt(new NewStmt("v2", new ValueExpression(new IntValue(30))),
//                                                        new CompoundStmt(new NewLockStmt("x"),
//                                                                new CompoundStmt(new ThreadStmt(new CompoundStmt(new ThreadStmt(new CompoundStmt(new LockStmt("x"),
//                                                                        new CompoundStmt(new WriteHeapStmt("v1", new ArithmeticExpression('-', new ReadHeapExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(1)))),
//                                                                                new UnlockStmt("x")))),
//                                                                        new CompoundStmt(new LockStmt("x"),
//                                                                                new CompoundStmt(new WriteHeapStmt("v1", new ArithmeticExpression('*', new ReadHeapExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(10)))),
//                                                                                        new UnlockStmt("x"))))),
//                                                                        new CompoundStmt(new LockStmt("x"),
//                                                                                new CompoundStmt(new PrintStmt(new ReadHeapExpression(new VariableExpression("v1"))),
//                                                                                        new UnlockStmt("x")))))))))));
//        try{
//            ex11.typeCheck(new MyDictionary<String, Type>());
//        }
//        catch(MyException e){
//            System.out.println("ex11: "+e.getMessage());
//            return;
//        }
//        ProgramState State11 = new ProgramState(new MyStack<IStmt>(), new MyDictionary<String, Value>(),new MyDictionary<StringValue, BufferedReader>(), new MyList<Value>(), new MyHeap<Integer, Value>(), ex11, new MyLockHeap<Integer, Value>());
//        Repository Repo11 = new Repository(State11, "log11.txt");
//        Controller Controller11 = new Controller(Repo11);
//
//        /*
//        Ref int v1; Ref int v2; int x; int q;
//        new(v1,20);new(v2,30);newLock(x);
//        fork(
//         fork(
//         lock(x);wh(v1,rh(v1)-1);unlock(x)
//         );
//         lock(x);wh(v1,rh(v1)*10);unlock(x)
//        );newLock(q);
//        fork(
//         fork(lock(q);wh(v2,rh(v2)+5);unlock(q));
//         lock(q);wh(v2,rh(v2)*10);unlock(q)
//        );
//        nop;nop;nop;nop;
//        lock(x); print(rh(v1)); unlock(x);
//        lock(q); print(rh(v2)); unlock(q);
//        * */
//        IStmt ex12 = new CompoundStmt(new VariableDeclarationStmt("v1", new ReferenceType(new IntType())),
//                new CompoundStmt(new VariableDeclarationStmt("v2", new ReferenceType(new IntType())),
//                        new CompoundStmt(new VariableDeclarationStmt("x", new IntType()),
//                                new CompoundStmt(new VariableDeclarationStmt("q", new IntType()),
//                                        new CompoundStmt(new NewStmt("v1", new ValueExpression(new IntValue(20))),
//                                                new CompoundStmt(new NewStmt("v2", new ValueExpression(new IntValue(30))),
//                                                        new CompoundStmt(new NewLockStmt("x"),
//                                                                new CompoundStmt(new ThreadStmt(new CompoundStmt(new ThreadStmt(new CompoundStmt(new LockStmt("x"),
//                                                                        new CompoundStmt(new WriteHeapStmt("v1", new ArithmeticExpression('-', new ReadHeapExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(1)))),
//                                                                                new UnlockStmt("x")))),
//                                                                        new CompoundStmt(new LockStmt("x"),
//                                                                                new CompoundStmt(new WriteHeapStmt("v1", new ArithmeticExpression('*', new ReadHeapExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(10)))),
//                                                                                        new UnlockStmt("x"))))),
//                                                                        new CompoundStmt(new NewLockStmt("q"),
//                                                                                new CompoundStmt(new ThreadStmt(new CompoundStmt(new ThreadStmt(new CompoundStmt(new LockStmt("q"),
//                                                                                        new CompoundStmt(new WriteHeapStmt("v2", new ArithmeticExpression('+', new ReadHeapExpression(new VariableExpression("v2")), new ValueExpression(new IntValue(5)))),
//                                                                                                new UnlockStmt("q")))),
//                                                                                        new CompoundStmt(new LockStmt("q"),
//                                                                                                new CompoundStmt(new WriteHeapStmt("v2", new ArithmeticExpression('*', new ReadHeapExpression(new VariableExpression("v2")), new ValueExpression(new IntValue(10)))),
//                                                                                                        new UnlockStmt("q"))))),
//                                                                                        new CompoundStmt(new NoOperationStmt(),
//                                                                                                new CompoundStmt(new NoOperationStmt(),
//                                                                                                        new CompoundStmt(new NoOperationStmt(),
//                                                                                                                new CompoundStmt(new NoOperationStmt(),
//                                                                                                                        new CompoundStmt(new LockStmt("x"),
//                                                                                                                                new CompoundStmt(new PrintStmt(new ReadHeapExpression(new VariableExpression("v1"))),
//                                                                                                                                        new CompoundStmt(new UnlockStmt("x"),
//                                                                                                                                                new CompoundStmt(new LockStmt("q"),
//                                                                                                                                                        new CompoundStmt(new PrintStmt(new ReadHeapExpression(new VariableExpression("v2"))),
//                                                                                                                                                                new UnlockStmt("q"))))))))))))))))))));
//        try{
//            ex12.typeCheck(new MyDictionary<String, Type>());
//        }
//        catch(MyException e){
//            System.out.println("ex12: "+e.getMessage());
//            return;
//        }
//        ProgramState State12 = new ProgramState(new MyStack<IStmt>(), new MyDictionary<String, Value>(),new MyDictionary<StringValue, BufferedReader>(), new MyList<Value>(), new MyHeap<Integer, Value>(), ex12, new MyLockHeap<Integer, Value>());
//        Repository Repo12 = new Repository(State12, "log12.txt");
//        Controller Controller12 = new Controller(Repo12);

        /*
        Ref int a; Ref int b; int v;
        new(a,0); new(b,0);
        wh(a,1); wh(b,2);
        v=(rh(a)<rh(b))?100:200;
        print(v);
        v= ((rh(b)-2)>rh(a))?100:200;
        print(v);
        * */
        IStmt ex13 = new CompoundStmt(new VariableDeclarationStmt("a", new ReferenceType(new IntType())),
                new CompoundStmt(new VariableDeclarationStmt("b", new ReferenceType(new IntType())),
                        new CompoundStmt(new VariableDeclarationStmt("v", new IntType()),
                                new CompoundStmt(new NewStmt("a", new ValueExpression(new IntValue(0))),
                                        new CompoundStmt(new NewStmt("b", new ValueExpression(new IntValue(0))),
                                                new CompoundStmt(new WriteHeapStmt("a", new ValueExpression(new IntValue(1))),
                                                        new CompoundStmt(new WriteHeapStmt("b", new ValueExpression(new IntValue(2))),
                                                                new CompoundStmt(new VariableDeclarationStmt("v", new IntType()),
                                                                        new CompoundStmt(new ConditionalAssignmentStmt("v", new ValueExpression(new IntValue(100)), new ValueExpression(new IntValue(200)), new LogicExpression(new ReadHeapExpression(new VariableExpression("a")), new ReadHeapExpression(new VariableExpression("b")), "<")),
                                                                                new CompoundStmt(new PrintStmt(new VariableExpression("v")),
                                                                                        new CompoundStmt(new ConditionalAssignmentStmt("v", new ValueExpression(new IntValue(100)), new ValueExpression(new IntValue(200)), new LogicExpression(new ArithmeticExpression('-', new ReadHeapExpression(new VariableExpression("b")), new ValueExpression(new IntValue(2))), new ReadHeapExpression(new VariableExpression("a")), ">")),
                                                                                                new PrintStmt(new VariableExpression("v")))))))))))));
        try {
            ex13.typeCheck(new MyDictionary<String, Type>());
        } catch (MyException e) {
            System.out.println("ex13: " + e.getMessage());
            return;
        }
        ProgramState State13 = new ProgramState(new MyStack<IStmt>(), new MyDictionary<String, Value>(), new MyDictionary<StringValue, BufferedReader>(), new MyList<Value>(), new MyHeap<Integer, Value>(), ex13, new MyLockHeap<Integer, Value>(), new MyLockHeap<Integer, Integer>());

        Repository Repo13 = new Repository(State13, "log13.txt");
        Controller Controller13 = new Controller(Repo13);

        /*
        Ref int v1; Ref int v2; Ref int v3; int cnt;
        new(v1,2);new(v2,3);new(v3,4);newLatch(cnt,rH(v2));
        fork(wh(v1,rh(v1)*10);print(rh(v1));countDown(cnt);
         fork(wh(v2,rh(v2)*10);print(rh(v2));countDown(cnt);
         fork(wh(v3,rh(v3)*10);print(rh(v3));countDown(cnt))));
        await(cnt);
        print(100);
        countDown(cnt);
        print(100);
        */
        IStmt ex14 = new CompoundStmt(new VariableDeclarationStmt("v1", new ReferenceType(new IntType())),
                new CompoundStmt(new VariableDeclarationStmt("v2", new ReferenceType(new IntType())),
                        new CompoundStmt(new VariableDeclarationStmt("v3", new ReferenceType(new IntType())),
                                new CompoundStmt(new VariableDeclarationStmt("cnt", new IntType()),
                                        new CompoundStmt(new NewStmt("v1", new ValueExpression(new IntValue(2))),
                                                new CompoundStmt(new NewStmt("v2", new ValueExpression(new IntValue(3))),
                                                        new CompoundStmt(new NewStmt("v3", new ValueExpression(new IntValue(4))),
                                                                new CompoundStmt(new NewLatchStmt("cnt", new ReadHeapExpression(new VariableExpression("v2"))),
                                                                        new CompoundStmt(new ThreadStmt(new CompoundStmt(new WriteHeapStmt("v1", new ArithmeticExpression('*', new ReadHeapExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(10)))),
                                                                                new CompoundStmt(new PrintStmt(new ReadHeapExpression(new VariableExpression("v1"))),
                                                                                        new CompoundStmt(new CountDownStmt("cnt"),
                                                                                                new NoOperationStmt())))),
                                                                                new CompoundStmt(new ThreadStmt(new CompoundStmt(new WriteHeapStmt("v2", new ArithmeticExpression('*', new ReadHeapExpression(new VariableExpression("v2")), new ValueExpression(new IntValue(10)))),
                                                                                        new CompoundStmt(new PrintStmt(new ReadHeapExpression(new VariableExpression("v2"))),
                                                                                                new CompoundStmt(new CountDownStmt("cnt"),
                                                                                                        new NoOperationStmt())))),
                                                                                        new CompoundStmt(new ThreadStmt(new CompoundStmt(new WriteHeapStmt("v3", new ArithmeticExpression('*', new ReadHeapExpression(new VariableExpression("v3")), new ValueExpression(new IntValue(10)))),
                                                                                                new CompoundStmt(new PrintStmt(new ReadHeapExpression(new VariableExpression("v3"))),
                                                                                                        new CompoundStmt(new CountDownStmt("cnt"),
                                                                                                                new NoOperationStmt())))),
                                                                                                new CompoundStmt(new AwaitStmt("cnt"),
                                                                                                        new CompoundStmt(new PrintStmt(new ValueExpression(new IntValue(100))),
                                                                                                                new CompoundStmt(new CountDownStmt("cnt"),
                                                                                                                        new PrintStmt(new ValueExpression(new IntValue(100)))))))))))))))));
        try {
            ex14.typeCheck(new MyDictionary<String, Type>());
        } catch (MyException e) {
            System.out.println("ex14: " + e.getMessage());
            return;
        }
        ProgramState State14 = new ProgramState(new MyStack<IStmt>(), new MyDictionary<String, Value>(), new MyDictionary<StringValue, BufferedReader>(), new MyList<Value>(), new MyHeap<Integer, Value>(), ex14, new MyLockHeap<Integer, Value>(), new MyLockHeap<Integer, Integer>());
        Repository Repo14 = new Repository(State14, "log14.txt");
        Controller Controller14 = new Controller(Repo14);

        //========================================================================
        ObservableList<String> names = FXCollections.observableArrayList();
        Map<String, Controller> controllers = new HashMap<>();
//        controllers.put("Ex1 " + ex1.toString(), Controller1);
//        controllers.put("Ex2 " + ex2.toString(), Controller2);
//        controllers.put("Ex3 " + ex3.toString(), Controller3);
//        controllers.put("Ex4 " + ex4.toString(), Controller4);
//        controllers.put("Ex5 " + ex5.toString(), Controller5);
//        controllers.put("Ex6 " + ex6.toString(), Controller6);
//        controllers.put("Ex7 " + ex7.toString(), Controller7);
//        controllers.put("Ex8 " + ex8.toString(), Controller8);
//        controllers.put("Ex9 " + ex9.toString(), Controller9);
//        controllers.put("Ex10 " + ex10.toString(), Controller10);
//        controllers.put("Ex11 " + ex11.toString(), Controller11);
//        controllers.put("Ex12 " + ex12.toString(), Controller12);
        controllers.put("Ex13 " + ex13.toString(), Controller13);
        controllers.put("Ex14 " + ex14.toString(), Controller14);

        names.addAll(controllers.keySet());


        ListView<String> list = new ListView<String>(names);
        list.setPrefSize(200, 200);

        EventHandler<MouseEvent> listHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    String name = list.getSelectionModel().getSelectedItem();
                    Controller controller = controllers.get(name);
                    //controllers.remove(name);
                    names.remove(name);
                    spawnInterpretWindow(controller);
                }
            }
        };
        list.setOnMouseClicked(listHandler);
        VBox layout = new VBox(20);
        layout.getChildren().addAll(list);
        Scene scene = new Scene(layout, 300, 250);

        Stage stage3 = new Stage();
        stage3.setTitle("Try it yourself!");
        VBox layout3 = new VBox(20);
        TextArea codeArea = new TextArea();
        codeArea.setPrefSize(300, 200);
        codeArea.setStyle("-fx-control-inner-background:#000000; -fx-font-family: Consolas; -fx-highlight-fill: #00ff00; -fx-highlight-text-fill: #000000; -fx-text-fill: #00ff00; ");
        Button runButton = new Button("Run");
        runButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Tokenizer tokenizer = new Tokenizer();
                Parser parser = new Parser(tokenizer);
                String code = codeArea.getText();
                IStmt stmt = null;
                try {
                    stmt = parser.parse(code);
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Parsing error");
                    alert.setContentText("Make sure you've entered a valid program. Click the 'Help' button for guidance.");
                    alert.showAndWait();
                    return;
                }
                try {
                    stmt.typeCheck(new MyDictionary<String, Type>());
                } catch (MyException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Type checking error");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                    return;
                }
                ProgramState state = new ProgramState(new MyStack<IStmt>(), new MyDictionary<String, Value>(), new MyDictionary<StringValue, BufferedReader>(), new MyList<Value>(), new MyHeap<Integer, Value>(), stmt, new MyLockHeap<Integer, Value>(), new MyLockHeap<Integer, Integer>());
                Repository repo = new Repository(state, "log.txt");
                Controller controller = new Controller(repo);
                spawnInterpretWindow(controller);
            }
        });

        Button helpButton = new Button("Help");
        helpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Help");
                alert.setHeaderText("How to use the interpreter");
                alert.setContentText("Enter a program in the text area and click the 'Run' button to run it. The program must be a valid program, otherwise an error will be displayed. The program must be written in the following format:\n" +
                        "1. The program must be a sequence of statements, separated by semicolons.\n" +
                        "2. The statements must be written in the following format:\n" +
                        "   a. Assignment statement: variableName = expression\n" +
                        "   b. Print statement: print(expression)\n" +
                        "   c. Conditional statement: if (expression then (statement) else (statement))\n" +
                        "   d. While statement: while (expression (statement))\n" +
                        "   e. Compound statement: (statement; statement; ...)\n" +
                        "   f. Open read file statement: openRFile(variableName)\n" +
                        "   g. Read file statement: readFile(variableName, variableName)\n" +
                        "   h. Close file statement: closeFile(variableName)\n" +
                        "   i. New statement: new(variableName, expression)\n" +
                        "   j. Write heap statement: wH(variableName, expression)\n" +
                        "   k. Read heap statement: rH(variableName, variableName)\n" +
                        "   l. Thread statement: thread(statement)\n" +
                        "   m. Lock statement: lock(variableName)\n" +
                        "   n. Unlock statement: unlock(variableName)\n" +
                        "3. The expressions must be written in the following format:\n" +
                        "   a. Arithmetic expression: expression + expression\n" +
                        "   b. Arithmetic expression: expression - expression\n" +
                        "   c. Arithmetic expression: expression * expression\n" +
                        "   d. Arithmetic expression: expression / expression\n" +
                        "   e. Boolean expression : expression [< | > | <= | >= | != | == ] expression\n");
                alert.showAndWait();
}
        });
        HBox buttons = new HBox(20);
        buttons.getChildren().addAll(runButton, helpButton);
        layout3.getChildren().addAll(codeArea, buttons);
        Scene scene3 = new Scene(layout3, 300, 250);
        stage3.setScene(scene3);
        stage3.show();



        stage.setTitle("Interpreter GUI");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}