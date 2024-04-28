package net.infinitecoder.jplugin.lib;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static net.infinitecoder.jplugin.JPlugin.activeClasses;
import net.infinitecoder.jplugin.parser.statements.BlockStatement;
import net.infinitecoder.jplugin.parser.values.*;

public final class VariablesClass {

    public Map<String, Value> variables;
    public Map<String, String> defTypes;
    public Map<String, String> types;
    private final List<String> sVars;
    private final List<String> sTypes;
    private final List<Value> sVals;

    public VariablesClass() {
        variables = new HashMap<>();
        types = new HashMap<>();
        defTypes = new HashMap<>();
        sVars = new ArrayList<>();
        sTypes = new ArrayList<>();
        sVals = new ArrayList<>();
        put("PI", "double", new DoubleValue(3.14159265358979323));
        put("true", "bool", new BoolValue((byte) 1));
        put("false", "bool", new BoolValue((byte) 0));
        defTypes.put("auto", "void");
    }

    public void put(String name, String type, Value value) {
        if(defTypes.containsKey(type)) {
            type = defTypes.get(type);
        }
        if (variables.containsKey(name)) {
            storeVariable(name);
        }
        variables.put(name, value.cast(type));
        types.put(name, type);
    }

    public void put(String name, Value value) {
        variables.put(name, value.cast(types.get(name)));
    }

    public Value get(String name) {
        if (!variables.containsKey(name)) {
            throw new RuntimeException("Unknown variable: " + name);
        }
        return variables.get(name);
    }

    public String typeof(Value v) {
        if (v instanceof LongValue) {
            return "long";
        } else if (v instanceof IntValue) {
            return "int";
        } else if (v instanceof ShortValue) {
            return "short";
        } else if (v instanceof DoubleValue) {
            return "double";
        } else if (v instanceof FloatValue) {
            return "float";
        } else if (v instanceof CharValue) {
            return "char";
        } else if (v instanceof BoolValue) {
            return "bool";
        } else if (v instanceof StringValue) {
            return "String";
        } else if (v instanceof ObjectValue) {
            return ((ObjectValue) v).classname;
        }
        return "NON_TYPE";
    }

    public String typeof(String t) {
        if(defTypes.containsKey(t)) {
            t = defTypes.get(t);
        }
        switch(t) {
            case "long":
                return "long";
            case "int":
                return "int";
            case "short":
                return "short";
            case "double":
                return "double";
            case "float":
                return "float";
            case "char":
                return "char";
            case "bool":
                return "bool";
            case "String":
                return "String";
        }
        if (activeClasses.classes.containsKey(t) || t.contains("::")) {
            return t;
        }
        return "NON_TYPE";
    }

    public void storeVariable(String name) {
        sVars.add(name);
        sTypes.add(types.get(name));
        sVals.add(variables.get(name));
    }

    public void restoreVariable(String name) {
        int index = sVars.lastIndexOf(name);
        sVars.remove(name);
        types.put(name, sTypes.remove(index));
        variables.put(name, sVals.remove(index));
    }

    public Value remove(String name) {
        Value v = variables.remove(name);
        types.remove(name);
        if (sVars.contains(name)) {
            restoreVariable(name);
        }
        return v;
    }

    public String greatest(String type1, String type2) {
        if(defTypes.containsKey(type1)) {
            type1 = defTypes.get(type1);
        }
        if(defTypes.containsKey(type2)) {
            type2 = defTypes.get(type2);
        }
        if (type1.equals("float") || type1.equals("double") || type2.equals("float") || type2.equals("double")) {
            return (type1.equals("double") || type2.equals("double")) ? "double" : "float";
        }
        if (type1.equals("long") || type1.equals("int") || type1.equals("short") || type2.equals("long") || type2.equals("int") || type2.equals("short")) {
            return (type1.equals("long") || type2.equals("long")) ? "long" : (type1.equals("int") || type2.equals("int")) ? "int" : "short";
        }
        if (type1.equals("bool") && type2.equals("bool")) {
            return "bool";
        }
        return "char";
    }

    public List<Byte> getBytes(Value value) {
        List<Byte> bytes = new ArrayList<>();
        if (value instanceof ObjectValue) {
            BlockStatement body = ((ObjectValue) value).body;
            for (String name : body.localVars) {
                bytes.addAll(getBytes(body.localVarsData.get(name)));
            }
        } else {
            if (value instanceof LongValue) {
                bytes.addAll(bytesToList(ByteBuffer.allocate(8).putLong(((LongValue) value).value).array()));
            } else if (value instanceof IntValue) {
                bytes.addAll(bytesToList(ByteBuffer.allocate(4).putInt(((IntValue) value).value).array()));
            }  else if (value instanceof ShortValue) {
                bytes.addAll(bytesToList(ByteBuffer.allocate(2).putShort(((ShortValue) value).value).array()));
            } else if (value instanceof DoubleValue) {
                bytes.addAll(bytesToList(ByteBuffer.allocate(8).putDouble(((DoubleValue) value).value).array()));
            } else if (value instanceof FloatValue) {
                bytes.addAll(bytesToList(ByteBuffer.allocate(4).putFloat(((FloatValue) value).value).array()));
            } else if (value instanceof CharValue) {
                bytes.add(((CharValue) value).value);
            } else if (value instanceof BoolValue) {
                bytes.add(((BoolValue) value).value);
            } else if (value instanceof StringValue) {
                for (char ch : ((StringValue) value).value.toCharArray()) {
                    bytes.add((byte) ch);
                }
                bytes.add((byte) 0);
            } else if (value instanceof ArrayValue) {
                for (Value v : ((ArrayValue) value).values) {
                    bytes.addAll(getBytes(v));
                }
            } else {
                throw new RuntimeException("Can not identify the type of " + value + "!");
            }
        }
        return bytes;
    }

    private List<Byte> bytesToList(byte[] array) {
        List<Byte> bytes = new ArrayList<>();
        for (byte b : array) {
            bytes.add(b);
        }
        return bytes;
    }

    public Value fromBytes(String type, List<Byte> bytes) {
        return fromBytes(type, bytes, null);
    }

    public Value fromBytes(String type, List<Byte> bytes, Value template) {
        if(defTypes.containsKey(type)) {
            type = defTypes.get(type);
        }
        ByteBuffer wrap = ByteBuffer.wrap(toArray(bytes));
        if (activeClasses.classes.containsKey(type) || type.contains("::")) {
            BlockStatement body = (BlockStatement) activeClasses.get(type).clone();
            body.isStatic = true;
            body.execute();
            for (String name : body.localVars) {
                Value value = body.localVarsData.get(name);
                body.localVarsData.put(name, fromBytes(typeof(value), bytes, value));
                bytes = bytes.subList(getBytes(fromBytes(typeof(value), bytes, value)).size(), bytes.size());
            }
            return new ObjectValue(body, type);
        } else {
            switch (type) {
                case "long":
                    return new LongValue(wrap.getLong());
                case "int":
                    return new IntValue(wrap.getInt());
                case "short":
                    return new ShortValue(wrap.getShort());
                case "double":
                    return new DoubleValue(wrap.getDouble());
                case "float":
                    return new FloatValue(wrap.getFloat());
                case "char":
                    return new CharValue(wrap.get());
                case "bool":
                    return new BoolValue(wrap.get());
                case "String":
                    String str = "";
                    while (true) {
                        byte b = wrap.get();
                        if (b == 0) {
                            break;
                        }
                        str += (char) b;
                    }
                    return new StringValue(str);
                case "void":
                    return new IntValue(0);
                default:
                    if (template instanceof ArrayValue) {
                        Value[] values = new Value[((ArrayValue) template).values.length];
                        for (int i = 0; i < values.length; i++) {
                            values[i] = fromBytes(typeof(((ArrayValue) template).values[i]), bytes, ((ArrayValue) template).values[i]);
                            bytes = bytes.subList(getBytes(fromBytes(typeof(((ArrayValue) template).values[i]), bytes)).size(), bytes.size());
                        }
                        return new ArrayValue(values);
                    }
                    throw new RuntimeException("Can not identify the type " + type + "!");
            }
        }
    }

    private byte[] toArray(List<Byte> bytes) {
        byte[] arr = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); i++) {
            arr[i] = bytes.get(i);
        }
        return arr;
    }
}
