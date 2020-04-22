package com.muabe.modelbinder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

public class Utils {

    public Map<String, List<Class<?>>> loadAndScanJar(File jarFile)
            throws ClassNotFoundException, ZipException, IOException {
        Map<String, List<Class<?>>> classes = new HashMap<String, List<Class<?>>>();

        List<Class<?>> interfaces = new ArrayList<Class<?>>();
        List<Class<?>> clazzes = new ArrayList<Class<?>>();
        List<Class<?>> enums = new ArrayList<Class<?>>();
        List<Class<?>> annotations = new ArrayList<Class<?>>();

        classes.put("interfaces", interfaces);
        classes.put("classes", clazzes);
        classes.put("annotations", annotations);
        classes.put("enums", enums);

        // Count the classes loaded
        int count = 0;

        // Your jar file
        JarFile jar = new JarFile(jarFile);
        // Getting the files into the jar
        Enumeration<? extends JarEntry> enumeration = jar.entries();

        // Iterates into the files in the jar file
        while (enumeration.hasMoreElements()) {
            ZipEntry zipEntry = enumeration.nextElement();

            // Is this a class?
            if (zipEntry.getName().endsWith(".class")) {

                // Relative path of file into the jar.
                String className = zipEntry.getName();

                // Complete class name
                className = className.replace(".class", "").replace("/", ".");
                // Load class definition from JVM
                Class<?> clazz = getClass().getClassLoader().loadClass(className);

                try {
                    // Verify the type of the "class"
                    if (clazz.isInterface()) {
                        interfaces.add(clazz);
                    } else if (clazz.isAnnotation()) {
                        annotations.add(clazz);
                    } else if (clazz.isEnum()) {
                        enums.add(clazz);
                    } else {
                        clazzes.add(clazz);
                    }

                    count++;
                } catch (ClassCastException e) {

                }
            }
        }

        System.out.println("Total: " + count);

        return classes;
    }


//    private void create(String className, String fieldName){
//        util.note("생성시");
//        FieldSpec field = FieldSpec.builder(String.class, fieldName)
//                .addModifiers(Modifier.PRIVATE)
//                .build();
//
//        MethodSpec setter = MethodSpec.methodBuilder("set"+fieldName)
//                .addModifiers(Modifier.PUBLIC)
//                .returns(void.class)
//                .addParameter(fieldName.getClass(), fieldName)
//                .addStatement("this."+fieldName+" = "+fieldName)
//                .build();
//
//        MethodSpec getter = MethodSpec.methodBuilder("get"+fieldName)
//                .addModifiers(Modifier.PUBLIC)
//                .returns(fieldName.getClass())
//                .addStatement("return this."+fieldName )
//                .build();
//
//        TypeSpec helloWorld = TypeSpec.classBuilder(className)
//                .addModifiers(Modifier.PUBLIC)
//                .addField(field)
//                .addMethod(setter)
//                .addMethod(getter)
//                .build();
//
//        JavaFile javaFile = JavaFile.builder("com.muabe.mylibrary", helloWorld)
//                .build();
//        util.note(javaFile.toString());
//
//        try {
//            javaFile.writeTo(util.filer);
//            util.note("생성완료");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    //create Filed
//    JCTree.JCVariableDecl variableDecl = createField(Flags.PRIVATE, "java.lang.String", "good", maker, elemUtils, el);
//    //create Annotation
//    createAnnotation(variableDecl, "androidx.databinding.Bindable", maker, elemUtils, el);
//
//    //클래스에서 field, method 리스트 가져오기
//    List<? extends Element> list = el.getEnclosedElements();
//            for (Element member : list) {
//        util.note(member.getKind() + " " + member.getSimpleName());
//        if(member.getKind().equals(ElementKind.FIELD)){ //필드일 경우
//            util.note("필드명 : "+member.getSimpleName());
//        }else if(member.getKind().equals(ElementKind.METHOD)){ //메소드일 경우
//            //code 삽입
//            inputCode(maker, elemUtils, member);
//        }
//    }


}
