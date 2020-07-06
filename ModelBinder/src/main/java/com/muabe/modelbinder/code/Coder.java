package com.muabe.modelbinder.code;

import com.muabe.modelbinder.decl.FieldDecl;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;

public class Coder {

    public static void makeBindable(Filer filer, String packageName, String className, TypeName extend, Collection<FieldDecl> fieldNames){
        if(extend == null){
            extend = ClassName.get("androidx.databinding", "BaseObservable");
        }
        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .superclass(extend);
        for(FieldDecl fieldDecl : fieldNames) {
            String fieldName = fieldDecl.getName();
            String methodName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());
            FieldSpec field = FieldSpec.builder(fieldDecl.getTypeClass(), fieldName)
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(ClassName.get("androidx.databinding", "Bindable"))
                    .build();

            MethodSpec getter = MethodSpec.methodBuilder("get" + methodName)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(fieldDecl.getTypeClass())
                    .addStatement("return this." + fieldName)
                    .build();


            MethodSpec setter = MethodSpec.methodBuilder("set" + methodName)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(void.class)
                    .addParameter(fieldDecl.getTypeClass(), fieldName)
                    .addStatement("this." + fieldName + " = " + fieldName)
                    .beginControlFlow("try")
                    .addStatement("notifyPropertyChanged((int)Class.forName($S).getDeclaredField(\"$N\").get(null))"
                            , "com.inviteshealth.tdna.BR", fieldName)
                    .nextControlFlow("catch ($T e)", Exception.class)
                    .addStatement("throw new $T(e)", RuntimeException.class)
                    .endControlFlow()
//                .addStatement("notifyPropertyChanged($T.$N)", ClassName.get("com.inviteshealth.tdna", "BR"), fieldName)
                    .build();


            typeBuilder
                    .addField(field)
                    .addMethod(setter)
                    .addMethod(getter);
        }
        JavaFile javaFile = JavaFile.builder(packageName, typeBuilder.build())
                .build();

        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean makeBindable(Filer filer, String packageName, String className, TypeName extend, Field[] fields){
        if(extend == null){
            extend = ClassName.get("androidx.databinding", "BaseObservable");
        }else{
//            System.out.println("상속이 있네:"+extend.toString());
            return false;
        }
        System.out.println(className+" 작업");
        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .superclass(extend);
        for(Field memberField : fields) {
            String fieldName = memberField.getName();
            String methodName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());
            FieldSpec field = FieldSpec.builder(memberField.getType(), fieldName)
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(ClassName.get("androidx.databinding", "Bindable"))
                    .build();

            MethodSpec getter = MethodSpec.methodBuilder("get" + methodName)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(memberField.getType())
                    .addStatement("return this." + fieldName)
                    .build();


            MethodSpec setter = MethodSpec.methodBuilder("set" + methodName)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(void.class)
                    .addParameter(memberField.getType(), fieldName)
                    .addStatement("this." + fieldName + " = " + fieldName)
                    .beginControlFlow("try")
                    .addStatement("notifyPropertyChanged((int)Class.forName($S).getDeclaredField(\"$N\").get(null))"
                            , "com.inviteshealth.tdna.BR", fieldName)
                    .nextControlFlow("catch ($T e)", Exception.class)
                    .addStatement("throw new $T(e)", RuntimeException.class)
                    .endControlFlow()
//                .addStatement("notifyPropertyChanged($T.$N)", ClassName.get("com.inviteshealth.tdna", "BR"), fieldName)
                    .build();
            typeBuilder
                    .addField(field)
                    .addMethod(setter)
                    .addMethod(getter);
        }
        JavaFile javaFile = JavaFile.builder(packageName, typeBuilder.build())
                .build();

        try {
            javaFile.writeTo(filer);
            System.out.println(className+" 종료");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
