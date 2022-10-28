package Model.Type;

import Model.Value.BoolValue;
import Model.Value.IntValue;
import Model.Value.Value;

public class IntType implements Type{
    @Override
    public boolean equals(Object other) {
        return other instanceof IntType;
    }

    @Override
    public String toString() {
        return "int";
    }

    @Override
    public Value defaultValue() {
        int i=0;
        return new IntValue(0);
    }
}
