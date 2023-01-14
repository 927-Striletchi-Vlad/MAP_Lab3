package Model.Value;

import Model.Type.ReferenceType;
import Model.Type.Type;
// A reference value consists of a heap address
// (that is an int) and the Type of the location
// having that address.
public class ReferenceValue implements Value{
    int address;
    Type locationType;

    public ReferenceValue(int address, Type locationType) {
        this.address = address;
        this.locationType = locationType;
    }

    @Override
    public Type getType() {
        return new ReferenceType(locationType);
    }

    public int getAddress() {
        return address;
    }

    public Type getLocationType() {
        return locationType;
    }

    @Override
    public String toString() {
        return "ReferenceValue{" +
                "address=" + address +
                ", locationType=" + locationType +
                '}';
    }

    @Override
    public Value deepCopy() {
        return new ReferenceValue(address, locationType);
    }
}
