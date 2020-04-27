package com.muabe.modelbinder.decl;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

public class ClassDecl extends BaseDecl {
    String packageName;
    TypeElement typeElement;
    JCTree.JCClassDecl classDecl;
    HashMap<String, FieldDecl> fields;

    public ClassDecl(ProcessingEnvironment processingEnv, TypeElement typeElement){
        super(processingEnv);
        this.typeElement = typeElement;
        if(elemUtils != null){
            classDecl = (JCTree.JCClassDecl)elemUtils.getTree(typeElement);
            if(classDecl != null) {
                getMaker().pos = classDecl.pos;
            }
        }
    }

    protected ClassDecl(ClassDecl decl){
        super(decl.environment);
        this.packageName = decl.packageName;
        this.typeElement = decl.typeElement;
        this.classDecl = decl.classDecl;
        this.fields = decl.fields;
    }

    public JCTree.JCClassDecl getJCClassDecl(){
        return classDecl;
    }

    public String getCanonicalName(){
        return getPackageName()+"."+classDecl.getSimpleName();
    }

    public String getSimpleName(){
        return classDecl.getSimpleName().toString();
    }

    public FieldDecl getField(String fieldName){
        if(fields == null){
            getFieldMap();
        }
        return fields.get(fieldName);
    }

    public HashMap<String, FieldDecl> getFieldMap(){
        if(fields == null){
            fields = new HashMap<>();
            for(Element member : typeElement.getEnclosedElements()){
                if(member.getKind().equals(ElementKind.FIELD)) { //필드일 경우
                    FieldDecl fieldDecl = new FieldDecl(this, (VariableElement)member);
                    fields.put(fieldDecl.getName(), fieldDecl);
                }else if(member.getKind().equals(ElementKind.METHOD)){ //메소드일 경우

                }
            }
        }
        return fields;
    }

    public String getPackageName(){
        if(packageName == null) {
            Element e = typeElement;
            while (!(e instanceof PackageElement)) {
                e = e.getEnclosingElement();
            }
            packageName = ((PackageElement) e).getQualifiedName().toString();
        }
        return packageName;
    }

    private JCTree.JCExpression returnType(String returnType){
        try {
            if("void".equals(returnType)) {
//                JCExpression returnType = mTreeMaker.TypeIdent(TypeTags.VOID);
                return maker.Type((Type) (Class.forName("com.sun.tools.javac.code.Type$JCVoidType").newInstance()));
            }else{
                return maker.Type((Type) (Class.forName(returnType).newInstance()));
            }
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new RuntimeException("returnType make error");
        }
    }

    public JCTree.JCVariableDecl createField(String modifierString, String typeName, String name){
        getMaker().pos = classDecl.pos;
        JCTree.JCModifiers modifier = getMaker()
                .Modifiers(stringToModifier(modifierString)| Flags.STATIC);
        JCTree.JCExpression type = makeSelectExpr(maker, typeName);
        Name fieldName = elemUtils.getName(name);

        JCTree.JCVariableDecl variableDecl = maker.VarDef(
                modifier,
                fieldName,
                type,
                null);

        classDecl.defs = classDecl.defs.appendList(List.of(
                variableDecl
        ));
        return variableDecl;
    }


    public void addSetterMethod(String packageName, String modifier, String methodName, String paramType, String paramName){
        ListBuffer<JCTree.JCStatement> code = new ListBuffer<>();
        // this.paramName=paramName; assign:값할당 코드
        code.append(
                maker.Exec(maker.Assign(
                                            maker.Select(
                                                    maker.Ident(names.fromString("this")),
                                                    name(paramName)
                                                ),
                                            maker.Ident(name(paramName))
                                        )
                            )
                        );


        //                .beginControlFlow("try")
//                .addStatement("notifyPropertyChanged((int)Class.forName($S).getDeclaredField(\"$N\").get(null))"
//                        ,"com.muabe.modelconvert.BR", fieldName)
//                .nextControlFlow("catch ($T e)", Exception.class)
//                .addStatement("throw new $T(e)", RuntimeException.class)

        //this.notifyPropertyChanged(BR.methodName);
        code.append(maker.Exec(maker.Apply(
                List.nil(),
                maker.Select(
                        maker.Ident(name("this")), name("notifyPropertyChanged")),
                List.of(
                        maker.Select(maker.Ident(name("BR")), name(methodName))
                )
        )));

        Map<String, String> params = new HashMap<>();
        params.put(paramName, paramType);

        createMethod(modifier, null, setterName(methodName), params, code);
    }

    public void addMethod(String modifier, String methodName, String paramType, String paramName){
        ListBuffer<JCTree.JCStatement> code = new ListBuffer<>();
        // this.paramName=paramName; assign:값할당 코드

        //this.notifyPropertyChanged(BR.methodName);
        code.append(maker.Exec(maker.Apply(
                List.nil(),
                maker.Select(
                        maker.Select(maker.Ident(name("System")), name("out")), name("println"))
                        ,
                        nil()
                )
        ));

        Map<String, String> params = new HashMap<>();
        params.put(paramName, paramType);

        createMethod(modifier, null, setterName(methodName), params, code);
    }

    public void createMethod(String modifier, String returnType, String methodName, Map<String, String> paramType, ListBuffer<JCTree.JCStatement> code){
        JCTree.JCBlock methodBody = maker.Block(0, code.toList());
        JCTree.JCMethodDecl result = maker.MethodDef(
                stringToJCModifier(modifier),
                name(methodName), // name
                null, //return type
                List.nil(), // genericParameters maker.TypeParams((List<Type>)typeList)
                makeParameterDecl(paramType), // parameters
                nil(), // throwables maker.Types(thrown)
                methodBody, // method body
                null);

        classDecl.defs = classDecl.defs.appendList(List.of(
                result
        ));
//        make.at(NOPOS).MethodDef((Symbol.MethodSymbol)element, (JCBlock)body);
    }

    private List<JCTree.JCVariableDecl> makeParameterDecl(Map<String, String> paramType){
        ListBuffer<JCTree.JCVariableDecl> paramList = new ListBuffer<>();

        for(String paramName : paramType.keySet()){
            JCTree.JCVariableDecl param = maker.VarDef(
                    maker.Modifiers(Flags.PARAMETER, List.nil()),
                    name(paramName),
                    makeSelectExpr(maker, paramType.get(paramName)), null);
            paramList.add(param);
        }
        return paramList.toList();
    }

    private JCTree.JCVariableDecl makeParameterDecl(JCTree.JCVariableDecl jcVariableDecl){
        JCTree.JCVariableDecl param = maker.VarDef(
                maker.Modifiers(Flags.PARAMETER, List.nil()),
                jcVariableDecl.getName(),
                jcVariableDecl.vartype, null);
        return param;
    }


    private Name getNewMethodName(Name name) {
        String s = name.toString();
        return names.fromString("get" + s.substring(0, 1).toUpperCase() + s.substring(1, name.length()));
    }


    public void createAnnotation(JCTree.JCVariableDecl variableDecl, String annotationClassName){
        maker.pos = classDecl.pos;
        JCTree.JCAnnotation an = maker.Annotation(makeSelectExpr(maker, annotationClassName), nil());
        variableDecl.mods.annotations = variableDecl.mods.annotations.append(an);
    }

    public int stringToModifier(String modifier){
        if("public".equals(modifier)){
            return Flags.PUBLIC;
        }else if("protected".equals(modifier)){
            return Flags.PROTECTED;
        }else{
            return Flags.PRIVATE;
        }
    }

    public JCTree.JCModifiers stringToJCModifier(String modifier){
        return maker.Modifiers(stringToModifier(modifier));
    }


    public void inputCode(Element member) {
        JCTree tree = elemUtils.getTree(member);
        JCTree.JCMethodDecl methodDecl = (JCTree.JCMethodDecl) tree;


        println("mDecl.name"+methodDecl.name);
        println("mDecl.pos"+methodDecl.pos);
        println("mDecl.getReturnType"+methodDecl.getReturnType());
        println("mDecl.mods"+methodDecl.mods);
        println("mDecl.getKind"+methodDecl.getKind());


        maker.pos = methodDecl.pos;
        List<JCTree.JCExpression> nil = nil();
        Name system = name("System");
        Name out = name("out");
        Name _println = name("println");
        JCTree.JCLiteral helloworld = maker.Literal("Hello world");

        methodDecl.body = maker.Block(0, List.of(
                methodDecl.body,
                maker.Exec(
                        maker.Apply(nil,
                                maker.Select(maker.Select(maker.Ident(system),out),_println),
                                List.<JCTree.JCExpression>of(helloworld)
                        )
                )
                )
        );
        println("mDecl.body"+methodDecl.body.toString());
    }

    private Name name(String name){
//        names.fromString("set" + s.substring(0, 1).toUpperCase() + s.substring(1, name.length()));
        return elemUtils.getName(name);
    }

    private String setterName(String name) {
        return ("set" + name.substring(0, 1).toUpperCase() + name.substring(1));
    }

}
