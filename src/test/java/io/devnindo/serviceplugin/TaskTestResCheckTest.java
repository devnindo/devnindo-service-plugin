package io.devnindo.serviceplugin;


import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;


@Disabled
class TaskTestResCheckTest {

    @Test
    public void testResourceCheck() {


        Project project = ProjectBuilder.builder().build();
        project.getPluginManager().apply("java");
        project.getPluginManager().apply("devnindo-service");
        assertTrue(project.getPluginManager().hasPlugin("devnindo-service"));


    }
}