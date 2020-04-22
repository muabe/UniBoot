package com.muabe.modelbinder;

import com.google.auto.service.AutoService;
import com.muabe.modelbinder.annotation.ModelBinder;
import com.muabe.modelbinder.decl.BaseDecl;
import com.muabe.modelbinder.decl.ClassDecl;
import com.muabe.modelbinder.decl.FieldDecl;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

@SupportedAnnotationTypes("com.muabe.modelbinder.annotation.ModelBinder")
@AutoService(Processor.class)
public class VoToBindProcessor extends AbstractProcessor {
    BaseDecl util;
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
          util = new BaseDecl(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        util.println("Start");
        if (!roundEnvironment.processingOver()) {
            for (Element element : roundEnvironment.getRootElements()) {
                if (element.getKind().equals(ElementKind.CLASS)) {
                    CLoader c = new CLoader();

                    if(((TypeElement)element).getAnnotation(ModelBinder.class) !=null ){
                        ClassDecl classDecl = new ClassDecl(processingEnv, (TypeElement) element);

                    }else if(c.getAnnotationFilter() == null || element.getAnnotation(c.getAnnotationFilter()) !=null) {
                        ClassDecl classDecl = new ClassDecl(processingEnv, (TypeElement) element);
                            util.println("Load Class(" + c.getAnnotationFilter() + ") = " + classDecl.getCanonicalName());
                        if(classDecl.getJCClassDecl().extending != null){
                            util.println(classDecl.getJCClassDecl().extending.toString());
                        }else{
                            util.println("None Extends");
                        }
                        c.onProcess(classDecl);
                    }
                } else if (element.getKind().equals(ElementKind.INTERFACE)) {
                    //Interface일 경
                }
            }
            return true;
        }
        return false;
    }

    private void modelBinder(Element element){
        ClassDecl classDecl = new ClassDecl(processingEnv, (TypeElement) element);


    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new TreeSet<>(Arrays.asList(ModelBinder.class.getCanonicalName()));
    }


    interface CompileClassLoader {
        <T extends Annotation>T getAnnotationFilter();
        void onProcess(ClassDecl classDecl);
    }

    interface PackageClassLoader{
        <T extends Annotation>T getAnnotationFilter();
        void onProcess(ClassDecl classDecl);
    }

    class CLoader implements CompileClassLoader {

        @Override
        public <T extends Annotation> T getAnnotationFilter() {
            return null;
        }

        @Override
        public void onProcess(ClassDecl classDecl) {
            for(FieldDecl fieldDecl : classDecl.getFieldMap().values()){
//                classDecl.addMethod("public", fieldDecl.getName()+"ddd", fieldDecl.getType().toString(), fieldDecl.getName());
//                classDecl.addSetterMethod("public", fieldDecl.getName(), fieldDecl.getType().toString(), fieldDecl.getName());
            }
        }
    }
}
