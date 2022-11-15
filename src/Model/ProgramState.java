package Model;

import Model.ADT.*;
import Model.Statement.IStmt;
import Model.Value.StringValue;
import Model.Value.Value;

import javax.print.attribute.IntegerSyntax;
import java.io.BufferedReader;

public class ProgramState {
    private MyStackInterface<IStmt> exeStack;
    private MyDictionaryInterface<String, Value> symTable;
    private MyDictionaryInterface<StringValue, BufferedReader> fileTable;
    private MyHeapInterface<Integer, Value> heap;
    private MyListInterface<Value> output;
    private IStmt originalProgram;

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
    }

    public MyHeapInterface<Integer, Value> getHeap() {
        return heap;
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

    @Override
    public String toString() {
        String res = new String();
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
}
