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
