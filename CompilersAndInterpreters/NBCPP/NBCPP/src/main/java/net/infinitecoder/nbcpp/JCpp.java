package net.infinitecoder.nbcpp;
import java.io.*;
import java.util.*;

public class JCpp {
    public static void main(String[] args) throws Exception {
        FileObject fo = FileUtil.toFileObject(new File(args[0]));
        CsmStandaloneFileProvider fp = CsmStandaloneFileProvider.getDefault();
        CsmModel model = CsmModelAccessor.getModel();
        CsmModelState modelState = CsmModelAccessor.getModelState();

        CsmFile cf = fp.getCsmFile(fo);
        cf.scheduleParsing(true);
        Collection<CsmOffsetableDeclaration> c = cf.getDeclarations();
        c = ((CsmNamespaceDefinition)c.toArray()[0]).getDeclarations();
        for (CsmOffsetableDeclaration d : c) {
            if (d instanceof CsmFunction) {
                CsmFunction f = (CsmFunction)d;
                System.out.print(f.getQualifiedName() + " " + f.getName() + "(");
                Collection<CsmParameter> pp = f.getParameters();
                for (CsmParameter p : pp) {
                    System.out.print(p.getType().getClassifierText());
                }
                System.out.println(")");
            } else if (d instanceof ClassImpl) {
                ClassImpl cls = (ClassImpl)d;
                System.out.println("Got template? " + cls.isTemplate());
                List<CsmTemplateParameter> lt = cls.getTemplateParameters();
                for (CsmTemplateParameter t : lt) {
                    System.out.println(t.getQualifiedName() + " " + t.getName());
                }
                Collection<CsmMember> cm = cls.getMembers();
                for (CsmMember m : cm) {
                    CsmFunction f = (CsmFunction)m;
                    System.out.print(f.getQualifiedName() + " " + f.getName() + "(");
                    Collection<CsmParameter> pp = f.getParameters();
                    for (CsmParameter p : pp) {
                        System.out.print(p.getType().getClassifierText());
                    }
                    System.out.println(")");
                }
            }
        }
    }
}