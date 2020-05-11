package com.muabe.modelbinder;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class Utils {

    public static List<Class<?>> getJarClasses(ClassLoader classLoader, String packageName){
        String path = packageName.replace('.', '/');

        try {
            Enumeration<URL> resources = classLoader.getResources(path);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                Map<String, List<Class<?>>> map = Utils.loadAndScanJar(classLoader, new File(resource.getPath().replaceAll("!/"+path, "").replaceAll("file:", "")));
                return map.get("classes");
            }
            throw new RuntimeException("Jar file 못찾음("+path+")");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public static Map<String, List<Class<?>>> loadAndScanJar(ClassLoader classLoader, File jarFile) {
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
        JarFile jar = null;
        try {
            jar = new JarFile(jarFile);
        } catch (IOException e) {
            e.printStackTrace();
            new RuntimeException(e);
        }
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


                try {
                    Class<?> clazz = classLoader.loadClass(className);
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
                } catch (Exception e) {
                    new RuntimeException(e);
                }
            }
        }

        System.out.println("Total: " + count);

        return classes;
    }

    public static Class[] getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }

    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException
     */
    public static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
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
