/*
 * Copyright 2023 devnindo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
