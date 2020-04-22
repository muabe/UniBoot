package com.muabe.modelbinder.annotation;

import com.muabe.modelbinder.decl.ClassDecl;

import java.lang.annotation.Annotation;

public interface PackageClassProcess {
    <T extends Annotation>T getAnnotationFilter();
    void onProcess(ClassDecl classDecl);
}
