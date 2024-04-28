package ast;

import java.util.Objects;

public class Type {
    public String typename;

    public Type(String typename) {
        this.typename = typename;
    }

    @Override
    public String toString() {
        return typename;
    }

    public static boolean castPossible(Type from, Type to) { // TODO: castPossible
        if (from == null || to == null) return false;
        return true;
    }

    public static Type max(Type a, Type b) { // TODO: find max type
        if (a == null || b == null) throw new RuntimeException(String.format("Can't mix types '%s' and '%s'!", a, b));
        return b;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        Type type = (Type) other;
        return Objects.equals(typename, type.typename);
    }
}
