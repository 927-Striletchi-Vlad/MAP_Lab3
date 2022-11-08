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

    @Override
    public Value deepCopy() {
        return new IntValue(val);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IntValue){
            return this.val == ((IntValue) obj).val;
        }
        return false;
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
