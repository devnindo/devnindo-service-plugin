package io.devnindo.serviceplugin.write;

import com.squareup.javapoet.*;
import com.thoughtworks.qdox.model.JavaClass;
import io.devnindo.serviceplugin.parse.ActionModuleGraph;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DaggerActionModuleWriter
{

    final ActionModuleGraph moduleGraph;
    final Path srcPath;
    final String packageRoot;

    public DaggerActionModuleWriter(Path srcPath, String packageRoot, ActionModuleGraph moduleGraph) {
        this.moduleGraph = moduleGraph;
        this.srcPath = srcPath;
        this.packageRoot = packageRoot;
    }

    public void write() throws IOException
    {
     //   Path deployPath = srcPath.resolve("deploy");
        TypeSpec actionModuleSpec = buildSpec();
        JavaFile javaFile = JavaFile.builder(packageRoot+".deploy", actionModuleSpec).build();
        javaFile.writeTo(srcPath.toFile());

    }

    private TypeSpec buildSpec(){
        List<MethodSpec> providerMethodList = buildProviderMethodSpecs(moduleGraph.actionClzList);

        AnnotationSpec moduleAntSpec = buildDaggerModuleAnnotationSpec(moduleGraph.vldProviderList, moduleGraph.authProviderList);

        return TypeSpec.interfaceBuilder("ActionModule")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(moduleAntSpec)
                .addMethods(providerMethodList)
                .build();
    }
    private AnnotationSpec buildDaggerModuleAnnotationSpec(List<JavaClass> vldModuleList, List<JavaClass> authModuleList){
        List<ClassName> modulePoemList = Stream
                .concat(vldModuleList.stream(), authModuleList.stream())
                .map(clz -> ClassName.get(clz.getPackageName(), clz.getSimpleName()))
                .collect(Collectors.toList());

        StringBuilder formatBuilder = new StringBuilder("{");
        modulePoemList.forEach(poem -> formatBuilder.append("$T.class, "));

        Integer lastCommaIdx = formatBuilder.lastIndexOf(",");
        String format = formatBuilder.replace(lastCommaIdx, lastCommaIdx+1, "}")
                .toString();//substring(0, formatBuilder.lastIndexOf(","));

        return AnnotationSpec.builder(PoetClz.DAGGER_MODULE)
                .addMember("includes", format, modulePoemList.toArray())
                .build();
    }
    private List<MethodSpec> buildProviderMethodSpecs(List<JavaClass> actionClzList) {
        List<MethodSpec> methodSpecList = new ArrayList<>();
        for (int i = 0; i < actionClzList.size(); i++) {
            ClassName actionClassName = ClassName.get(actionClzList.get(i).getPackageName(), actionClzList.get(i).getName());
            MethodSpec methodSpec = MethodSpec.methodBuilder("bind_" + i)
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .addAnnotation(PoetClz.DAGGER_BIND)
                    .addAnnotation(PoetClz.DAGGER_INTO_MAP)
                    .addAnnotation(AnnotationSpec
                            .builder(PoetClz.ACTION_KEY)
                            .addMember("value", actionClassName.simpleName() + ".class")
                            .build())
                    .returns(PoetClz.BIZ_ACTION)
                    .addParameter(ParameterSpec.builder(actionClassName, "action$").build())
                    .build();
            methodSpecList.add(methodSpec);
        }
        return methodSpecList;
    }
}
