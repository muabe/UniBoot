package com.muabe.modelbinder;

import com.muabe.modelbinder.annotation.PackageClassBinder;

@PackageClassBinder({
        "com.skt.invites.tdna"
})
public class Example {
}

/** build.gradle



apply plugin: 'java'

//configurations.all {
//    resolutionStrategy.cacheChangingModulesFor 0, 'seconds'
//}


task customClean(type: Delete) {
    delete rootProject.buildDir
}
clean.dependsOn customClean

compileJava {

    def moduleName = "bindtest"
    def packageName = "com.muabe.bindtest"


    def genFolder = "../${moduleName}/src/main/java/"
    options.compilerArgs += "-AoutputPackage=${packageName}"
    File generatedSourceDir = project.file(genFolder)
    options.annotationProcessorGeneratedSourcesDirectory = generatedSourceDir
    doFirst {
        project.delete(genFolder+"/"+packageName.replaceAll(".","/")+"/vo")
    }
}

dependencies {
    implementation files('libs/testlib.jar')
    annotationProcessor project(':ModelBinder')
    implementation project(path: ':ModelBinder')
    implementation "com.skt.invites.tdna:tdnaVo:+"
}


 **/
