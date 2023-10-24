/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.devnindo.serviceplugin;


import io.devnindo.serviceplugin.parse.ActionModuleGraph;
import io.devnindo.serviceplugin.parse.ServiceModuleParser;
import io.devnindo.serviceplugin.write.DaggerActionModuleWriter;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.tasks.TaskAction;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author prevy-sage
 */
public class TaskBindAction extends DefaultTask {

    private void bindAction0(Path srcPath, String packageRoot) {

        try {
            ActionModuleGraph moduleGraph = new ServiceModuleParser(packageRoot, srcPath).parse();
            //new ServiceLayoutValidator(moduleGraph, packageRoot).validate();
            System.out.println(moduleGraph);
            new DaggerActionModuleWriter(srcPath, packageRoot, moduleGraph).write();
        }catch (Exception excp){

            throw new GradleException("generating ActionModule failed", excp);
        }
    }

    @TaskAction
    void bindAction() {

        String serviceRoot = getProject().getGroup().toString();
        System.out.println("PACKAGE ROOT(GROUP): " + serviceRoot);


        String projectPath = getProject().getProjectDir().toString();

        Path srcPath = Paths.get(projectPath, "src", "main", "java");
        this.bindAction0(srcPath, serviceRoot);
        // builder.addSourceTree(new File("src/main/java/"));


    }

}
