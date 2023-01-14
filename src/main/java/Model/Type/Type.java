package Model.Type;

import Model.Value.Value;

public interface Type {
    boolean equals(Object other);
    String toString();

    Value defaultValue();
    Type deepCopy();
}
