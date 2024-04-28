package net.infinitecoder.jplugin;

import java.util.ArrayList;
import java.util.List;
import net.infinitecoder.jplugin.lib.*;
import net.infinitecoder.jplugin.parser.Parser;
import net.infinitecoder.jplugin.parser.statements.*;
import net.infinitecoder.jplugin.parser.values.*;

public class JPlugin {

    public static final Value ZERO = new IntValue(0);
    public static VariablesClass activeVariables = new VariablesClass();
    public static FunctionsClass activeFunctions = new FunctionsClass();
    public static ClassesClass activeClasses = new ClassesClass();
    private List<JPluginError> errors;
    public final String code, path;
    public Statement statement;
    private final boolean test;

    public JPlugin(String pathToFile) {
        this.code = Preprocessor.readFile(pathToFile, false);
        this.path = pathToFile;
        this.test = false;
    }

    public JPlugin(String pathToFile, String code) {
        this.code = code;
        this.path = pathToFile;
        this.test = true;
    }

    public void parse() {
        //Preprocessor preproc = new Preprocessor(path, code, test);
        //errors = preproc.getErrors();
        errors = new ArrayList<>();
        Lexer lexer = new Lexer(Preprocessor.readFile(path, false));
        lexer.tokenize();
        if (lexer.hasErrors()) {
            errors.addAll(lexer.getErrors());
        }
        Parser parser = new Parser(lexer.getTokens());
        parser.parse();
        if (parser.hasErrors()) {
            errors.addAll(parser.getErrors());
        }
    }

    public String getAST() {
        return statement.toString();
    }

    public void execute() {
        statement.execute();
    }

//    private void addClass(String name, Class<?> c) {//INDEV stuff
//        BlockStatement bs = new BlockStatement();
//        for (Field f : c.getDeclaredFields()) {
//            bs.add(new ObjectCreationStatement(javaType(f.getGenericType().getTypeName()),
//                    Arrays.asList(new String[]{f.getName()}),
//                    Arrays.asList(new Expression[]{new ValueExpression(getValueFromField(f, c))})
//            ));
//        }
//        for (Method m : c.getDeclaredMethods()) {
//            bs.add(new FunctionRegisterStatement(m.getReturnType().getTypeName(), m.getName(), new ArrayList<String>(), new JCode("") {
//                @Override
//                public Value execute(Value... args) {
//                    try {
//                        Constructor<?> ctor = c.getConstructor();
//                        Object instance = ctor.newInstance(new Object[]{});
//                        return new IntValue((int) m.invoke(instance, (Object[]) args));
//                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | InstantiationException ex) {
////                        Logger.getLogger(JPlugin.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                    return null;
//                }
//            }
//            ));
//        }
//        activeClasses.put(name, bs);
//    }
//
//    public String javaType(String type) {
//        if (type.equals("boolean")) {
//            return "bool";
//        }
//        return type;
//    }
//
//    private Value getValueFromField(Field f, Class<?> c) {
//        try {
//            Constructor<?> ctor = c.getConstructor();
//            Object instance = ctor.newInstance(new Object[]{});
//            String type = f.getGenericType().getTypeName();
//            switch (type) {
//                case "long":
//                    return new LongValue(f.getLong(instance));
//                case "int":
//                    return new IntValue(f.getInt(instance));
//                case "double":
//                    return new DoubleValue(f.getDouble(instance));
//                case "float":
//                    return new FloatValue(f.getFloat(instance));
//                case "char":
//                    return new CharValue((byte) f.getChar(instance));
//                case "boolean":
//                    return new BoolValue((byte) (f.getBoolean(instance) ? 1 : 0));
//                case "String":
//                    return new StringValue((String) f.get(instance));
//            }
//        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
////            Logger.getLogger(JPlugin.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }

    public List<JPluginError> getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public void printErrors() {
        if (hasErrors()) {
            for (JPluginError error : errors) {
                System.err.println(error);
            }
        }
    }
}
