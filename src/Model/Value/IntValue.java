package Model.Value;

import Model.Type.IntType;
import Model.Type.Type;

public class IntValue implements Value{
    int val;

    public IntValue(int i) {
        val=i;
    }

    @Override
    public Type getType() {
        return new IntType();
    }

    public int getValue() {
        return val;
    }

    @Override
    public String toString() {
        return "IntValue{" +
                "val=" + val +
                '}';
    }
}
