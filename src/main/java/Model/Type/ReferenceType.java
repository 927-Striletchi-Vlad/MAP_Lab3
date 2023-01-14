package Model.Type;

import Model.Value.ReferenceValue;
import Model.Value.Value;

public class ReferenceType implements Type{
    Type inner;
    public ReferenceType(Type inner){
        this.inner = inner;
    }
    public Type getInner(){
        return inner;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ReferenceType;
    }

    public String toString(){
        return "Reference(" + inner.toString() + ")";
    }

    @Override
    public Value defaultValue() {
        return new ReferenceValue(0, inner);
    }

    @Override
    public Type deepCopy() {
        return new ReferenceType(inner.deepCopy());
    }
}
