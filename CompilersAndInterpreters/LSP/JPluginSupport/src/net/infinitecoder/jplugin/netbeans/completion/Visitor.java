package net.infinitecoder.jplugin.netbeans.completion;

import java.util.ArrayList;
import java.util.List;
import net.infinitecoder.jplugin.parser.statements.BlockStatement;
import net.infinitecoder.jplugin.parser.statements.ClassRegisterStatement;
import net.infinitecoder.jplugin.parser.statements.FunctionRegisterStatement;
import net.infinitecoder.jplugin.parser.statements.ObjectCreationStatement;
import net.infinitecoder.jplugin.parser.statements.Statement;

class Visitor {

    private final Statement body;

    public Visitor(Statement body) {
        this.body = body;
    }

    public List<String> getFunctions() {
        List<String> statements = new ArrayList<>();
        if (body instanceof FunctionRegisterStatement) {
            statements.add(((FunctionRegisterStatement) body).name);
            if (((FunctionRegisterStatement) body).body instanceof BlockStatement) {
                for (Statement st
                        : ((BlockStatement) ((FunctionRegisterStatement) body).body).statements) {
                    statements.addAll(new Visitor(st).getFunctions());
                }
            }
        } else if (body instanceof ClassRegisterStatement) {
            if (((ClassRegisterStatement) body).body instanceof BlockStatement) {
                for (Statement st
                        : ((BlockStatement) ((ClassRegisterStatement) body).body).statements) {
                    statements.addAll(new Visitor(st).getFunctions());
                }
            }
        } else if (body instanceof BlockStatement) {
            for (Statement st : ((BlockStatement) body).statements) {
                statements.addAll(new Visitor(st).getFunctions());
            }
        }
        return statements;
    }

    public List<String> getClasses() {
        List<String> statements = new ArrayList<>();
        if (body instanceof ClassRegisterStatement) {
            statements.add(((ClassRegisterStatement) body).name);
            if (((ClassRegisterStatement) body).body instanceof BlockStatement) {
                for (Statement st
                        : ((BlockStatement) ((ClassRegisterStatement) body).body).statements) {
                    for (String classname : new Visitor(st).getClasses()) {//TODO: scan my point
                        if (false) {
                            statements.add(((ClassRegisterStatement) body).name + "::" + classname);
                        } else {
                            statements.add(classname);
                        }
                    }
                }
            }
        } else if (body instanceof FunctionRegisterStatement) {
            if (((FunctionRegisterStatement) body).body instanceof BlockStatement) {
                for (Statement st
                        : ((BlockStatement) ((FunctionRegisterStatement) body).body).statements) {
                    statements.addAll(new Visitor(st).getClasses());
                }
            }
        } else if (body instanceof BlockStatement) {
            for (Statement st : ((BlockStatement) body).statements) {
                statements.addAll(new Visitor(st).getClasses());
            }
        }
        return statements;
    }

    public List<String> getVariables() {
        List<String> statements = new ArrayList<>();
        if (body instanceof ObjectCreationStatement) {
            statements.addAll(((ObjectCreationStatement) body).names);
        } else if (body instanceof FunctionRegisterStatement) {
            if (((FunctionRegisterStatement) body).body instanceof BlockStatement) {
                for (Statement st
                        : ((BlockStatement) ((FunctionRegisterStatement) body).body).statements) {
                    statements.addAll(new Visitor(st).getVariables());
                }
            }
        } else if (body instanceof ClassRegisterStatement) {
            if (((ClassRegisterStatement) body).body instanceof BlockStatement) {
                for (Statement st
                        : ((BlockStatement) ((ClassRegisterStatement) body).body).statements) {
                    statements.addAll(new Visitor(st).getVariables());
                }
            }
        } else if (body instanceof BlockStatement) {
            for (Statement st : ((BlockStatement) body).statements) {
                statements.addAll(new Visitor(st).getVariables());
            }
        }
        return statements;
    }
}
