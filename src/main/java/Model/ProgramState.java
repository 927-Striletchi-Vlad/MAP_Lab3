package Model;

import Model.ADT.*;
import Model.Statement.IStmt;
import Model.Value.StringValue;
import Model.Value.Value;
import Exception.MyException;
import java.io.BufferedReader;

public class ProgramState {
    private MyStackInterface<IStmt> exeStack;
    private MyDictionaryInterface<String, Value> symTable;
    private MyDictionaryInterface<StringValue, BufferedReader> fileTable;
    private MyHeapInterface<Integer, Value> heap;
    private MyListInterface<Value> output;
    private IStmt originalProgram;
    private static int lastId = -1;
    private int id;

    public ProgramState(MyStackInterface<IStmt> exeStack, MyDictionaryInterface<String, Value> symTable,
                        MyDictionaryInterface<StringValue, BufferedReader> fileTable, MyListInterface<Value> output,
                        MyHeapInterface<Integer, Value> heap ,IStmt originalProgram) {
        this.exeStack = exeStack;
        this.symTable = symTable;
        this.fileTable = fileTable;
        this.heap = heap;
        this.output = output;
        this.originalProgram = originalProgram.deepCopy();
        exeStack.push(originalProgram);
        if(lastId == -1){
            lastId = 0;
        }
        else{
            lastId++;
        }
        this.id = lastId;
    }

    public MyHeapInterface<Integer, Value> getHeap() {
        return heap;
    }

    public int getId() {
        return id;
    }

    public MyListInterface<Value> getOutput() {
        return output;
    }

    public void setOutput(MyListInterface<Value> output) {
        this.output = output;
    }

    public MyStack<IStmt> getStack() {
        return (MyStack<IStmt>) exeStack;
    }

    public MyDictionary<String, Value> getSymbolTable() {
        return (MyDictionary<String, Value>) symTable;
    }

    public void setExeStack(MyStackInterface<IStmt> exeStack) {
        this.exeStack = exeStack;
    }

    public void setSymTable(MyDictionaryInterface<String, Value> symTable) {
        this.symTable = symTable;
    }

    public MyDictionaryInterface<StringValue, BufferedReader> getFileTable() {
        return fileTable;
    }

    public Boolean isNotCompleted() {
        return !exeStack.isEmpty();
    }

    public ProgramState oneStep() throws MyException{
        if (exeStack.isEmpty()){
//            throw new MyException("execution stack is empty.");
            return null;
        }
        IStmt currentStatement = exeStack.pop();
        return currentStatement.execute(this);
    }

    @Override
    public String toString() {
        String res = new String();

        res += "\n" + "ID: " + id;  
        res=res.concat("\n-----EXECUTION STACK:-----\n");
        res=res.concat(exeStack.toString());

        res=res.concat("\n-----SYMBOL TABLE:-----\n");
        res=res.concat(symTable.toString());

        res=res.concat("\n-----FILE TABLE:-----\n");
        res=res.concat(fileTable.toString());

        res=res.concat("\n-----HEAP:-----\n");
        res=res.concat(heap.toString());

        res=res.concat("\n-----OUTPUT:-----\n");
        res=res.concat(output.toString());

        return res;
    }

    public boolean isCompleted() {
        return exeStack.isEmpty();
    }
}
