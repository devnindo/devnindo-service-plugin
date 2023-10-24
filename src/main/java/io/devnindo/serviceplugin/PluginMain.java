package io.devnindo.serviceplugin;


import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;


public class PluginMain implements Plugin<Project> {


    @Override
    public void apply(Project project) {


        Task actionBinding = project.getTasks().create("genActionBindings", TaskBindAction.class, (task$) ->
        {

        });

        Task testResourceCheck = project.getTasks().create("checkTestResource", TaskTestResCheck.class, (task$) ->
        {

        });

        project.afterEvaluate(project$ -> {
            project.getTasks().getByName("compileJava")
                    .dependsOn(actionBinding)
                    .dependsOn(testResourceCheck);
        });
    }


}
