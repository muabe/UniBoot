package com.muabe.modelbinder.decl;

import com.sun.tools.javac.tree.JCTree;

import javax.lang.model.element.VariableElement;

public class FieldDecl extends BaseDecl {
    ClassDecl classDecl;
    VariableElement variableElement;
    JCTree.JCVariableDecl variableDecl;


    public FieldDecl(ClassDecl classDecl, VariableElement variableElement) {
        super(classDecl.environment);
        this.classDecl = classDecl;
        this.variableElement = variableElement;
        variableDecl = (JCTree.JCVariableDecl)elemUtils.getTree(variableElement);
    }

    public JCTree.JCVariableDecl getJCVariableDecl(){
        return variableDecl;
    }


    public ClassDecl getClassDecl(){
        return classDecl;
    }

    public String getName(){
        return variableDecl.getName().toString();
    }

    public void addAnnotation(String annotationClassName){
        maker.pos = classDecl.classDecl.pos;
        JCTree.JCAnnotation an = maker.Annotation(makeSelectExpr(maker, annotationClassName), nil());
        variableDecl.mods.annotations = variableDecl.mods.annotations.append(an);
    }

    public JCTree.JCExpression getType(){
        return variableDecl.vartype;
    }
}
