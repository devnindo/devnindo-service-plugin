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


import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;


public class TaskTestResCheck extends DefaultTask {


    @TaskAction
    void checkTestResource() {

        String projectPath = getProject().getProjectDir().toString();
        Path testResourcePath = Paths.get(projectPath, "src", "test", "resources");
        File propFile = new File(testResourcePath.toFile().getAbsolutePath() + "/junit-platform.properties");

        Properties props = new Properties();
        testResourcePath.toFile().mkdirs();

        try {
            props.load(new FileInputStream(propFile));
        } catch (IOException e) {
            try {
                props.setProperty("junit.jupiter.execution.parallel.enabled", "true");
                props.setProperty("junit.jupiter.execution.parallel.mode.default", "same_thread");
                props.setProperty("junit.jupiter.execution.parallel.mode.classes.default", "concurrent");
                props.store(new FileOutputStream(propFile), null);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("junit parallel properties");
        System.out.println(props);


    }

}
