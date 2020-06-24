package com.muabe.modelbinder;

import com.google.auto.service.AutoService;
import com.muabe.modelbinder.annotation.PackageClassBinder;
import com.muabe.modelbinder.annotation.ViewModel;
import com.muabe.modelbinder.code.Coder;
import com.muabe.modelbinder.decl.BaseDecl;
import com.muabe.modelbinder.decl.ClassDecl;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;

@SupportedAnnotationTypes({
        "com.muabe.modelbinder.annotation.PackageClassBinder"
        ,"com.muabe.modelbinder.annotation.ViewModel"
})
@AutoService(Processor.class)
public class VoToBindProcessor extends AbstractProcessor {
    BaseDecl util;
    String genPackage;
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        util = new BaseDecl(processingEnv);
        genPackage = processingEnvironment.getOptions().get("outputPackage");
    }


    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (!roundEnvironment.processingOver()) {
            for (Element element : roundEnvironment.getRootElements()) {
                if (element.getKind().equals(ElementKind.CLASS)) {
                    if(element.getAnnotation(PackageClassBinder.class) !=null ){
                        util.println("------------ Start Processor : -------------");
                        System.out.println("PackageClassBinder : "+ element.getSimpleName());
                        ClassDecl classDecl = new ClassDecl(processingEnv, (TypeElement) element);

                        List<? extends AnnotationMirror> annotationMirrors = element.getAnnotationMirrors();
                        for (AnnotationMirror mirror : annotationMirrors) {
                            if (PackageClassBinder.class.getCanonicalName().equals(mirror.getAnnotationType().toString())) {
                                Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues = mirror.getElementValues();
                                com.sun.tools.javac.util.List<?> valueList = null;
                                com.sun.tools.javac.util.List<?> ignoreList = null;
                                for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : elementValues.entrySet()) {
                                    String key = entry.getKey().getSimpleName().toString();
                                    if(key.equals("ignore")){
                                        ignoreList = (com.sun.tools.javac.util.List<?>)(entry.getValue().getValue());
                                    }
                                    if(key.equals("value")){
                                        valueList = (com.sun.tools.javac.util.List<?>)(entry.getValue().getValue());
                                    }
                                }
                                if(valueList != null){
                                    jarBind(classDecl,valueList, ignoreList);
                                }
                            }
                        }
                    }
                } else if (element.getKind().equals(ElementKind.INTERFACE)) {
                    //Interface일 경
                }
            }
            return true;
        }
        return false;
    }

    private void jarBind(ClassDecl classDecl, com.sun.tools.javac.util.List<?> values, com.sun.tools.javac.util.List<?> ignores){
        for(Object value : values){
            String loadPackage = value.toString().replaceAll("\"", "");
            List<Class<?>> classes = Utils.getJarClasses(getClass().getClassLoader(), loadPackage);

            for(Class<?> clazz : classes){
                if(ignores != null) {
                    boolean isIgnore = false;
                    for (Object ignore : ignores) {
                        System.out.println(clazz.getSimpleName()+"/"+ignore+" "+clazz.getSimpleName().equals(ignore.toString().replace("\"","")));
                        if (clazz.getSimpleName().equals(ignore.toString().replace("\"",""))) {
                            isIgnore = true;
                            System.out.println("ignore:"+clazz.getSimpleName());
                            break;
                        }
                    }
                    if (isIgnore) {
                        continue;
                    }
                }
                String packageName = genPackage +".vo"+clazz.getCanonicalName().replaceAll("."+clazz.getSimpleName(), "").replaceAll(loadPackage, "");
                TypeName extendsType = null;

                if(!clazz.getSuperclass().equals(Object.class)){
                    String superType = clazz.getSuperclass().getCanonicalName();
                    String pack = superType.substring(0,superType.lastIndexOf("."));
                    String name = superType.replaceAll(pack+".","");
                    pack = genPackage +".vo"+pack.replaceAll(loadPackage, "");
                    extendsType = ClassName.get(pack, name);
                }

                Coder.makeBindable(classDecl.getFiler(),
                        packageName,
                        clazz.getSimpleName(),
                        extendsType,
                        clazz.getDeclaredFields());
            }
        }
    }


    private void bind(ClassDecl classDecl, com.sun.tools.javac.util.List<?> values){
        for(Object value : values){
            String loadPackage = value.toString().replaceAll("\"", "");
            System.out.println("load package : "+ loadPackage);
            PackageElement pg = util.getElemUtils().getPackageElement(loadPackage);
            String delGenPath = "com.skt.invites.tdna"; //loadPackage;
            makeBind(classDecl, pg, delGenPath);
        }
    }

    private void makeBind(ClassDecl classDecl, PackageElement pg, String delGenPath){
        for(Element element : pg.getEnclosedElements()){
            if(element.getKind().equals(ElementKind.CLASS)) {
                ClassDecl newClassDecl = new ClassDecl(processingEnv, (TypeElement) element);
                String packageName = genPackage +".vo"+newClassDecl.getPackageName().replaceAll(delGenPath, "");
                System.out.println("load : "+ packageName+" "+element.getSimpleName());

                //상속 검사
                TypeName extendType = null;
                String superType = ((TypeElement) element).getSuperclass().toString();
                if(!"java.lang.Object".equals(superType)){
                    String pack = superType.substring(0,superType.lastIndexOf("."));
                    String name = superType.replaceAll(pack+".","");
                    pack = genPackage +".vo"+pack.replaceAll(delGenPath, "");
                    extendType = ClassName.get(pack, name);
                }
                Coder.makeBindable(classDecl.getFiler(),
                        packageName,
                        element.getSimpleName().toString(),
                        extendType,
                        newClassDecl.getFieldMap().values());

            }else if(element.getKind().equals(ElementKind.PACKAGE)){
                util.println("요기 : "+pg.getQualifiedName().toString());
                makeBind(classDecl, (PackageElement)element, delGenPath);
            }
        }
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new TreeSet<>(Arrays.asList(PackageClassBinder.class.getCanonicalName()
                , ViewModel.class.getCanonicalName()
        ));
    }

    @Override
    public Set<String> getSupportedOptions() {
        Set<String> set = new HashSet<>();
        set.add("outputPackage");
        return set;

    }
}
