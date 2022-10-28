package Model;

import Model.ADT.*;
import Model.Statement.IStmt;
import Model.Value.Value;

import javax.print.attribute.IntegerSyntax;

public class ProgramState {
    private MyStackInterface<IStmt> exeStack;
    private MyDictionaryInterface<String, Value> symTable;
    private MyListInterface<Value> output;
    private IStmt originalProgram;

    public ProgramState(MyStackInterface<IStmt> exeStack, MyDictionaryInterface<String, Value> symTable, MyListInterface<Value> output, IStmt originalProgram) {
        this.exeStack = exeStack;
        this.symTable = symTable;
        this.output = output;
        this.originalProgram = originalProgram;
        exeStack.push(originalProgram);
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
}
