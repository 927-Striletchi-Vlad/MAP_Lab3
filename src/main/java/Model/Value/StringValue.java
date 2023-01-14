package Model.Value;

import Model.Type.StringType;
import Model.Type.Type;

import java.util.Objects;

public class StringValue implements Value{
    String val;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StringValue){
            return Objects.equals(this.val, ((StringValue) obj).val);
        }
        return false;
    }

    public String getVal() {
        return val;
    }

    public StringValue(String val) {
        this.val = val;
    }

    @Override
    public Type getType() {
        return new StringType();
    }

    @Override
    public Value deepCopy() {
        return new StringValue(val);
    }

    @Override
    public String toString() {
        return "StringValue{" +
                "val='" + val + '\'' +
                '}';
    }
}
