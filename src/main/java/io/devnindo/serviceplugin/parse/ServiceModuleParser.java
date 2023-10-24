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

package io.devnindo.serviceplugin.parse;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import dagger.Module;
import io.devnindo.datatype.tuples.Pair;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.Violation;
import io.devnindo.service.deploy.base.ActionAuth;
import io.devnindo.service.deploy.base.ActionValidation;
import io.devnindo.service.exec.action.Active;
import io.devnindo.service.exec.action.BizAction;
import io.devnindo.serviceplugin.utils.QdoxUtil;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class ServiceModuleParser
{
    // srcPath should be src/main/java/${group}/
    // group => com.${company}.service

    final Path srcPath;
    final String packageRoot;
    final JavaProjectBuilder qdoxBuilder;

    public ServiceModuleParser(String packageRoot, String dirStr)
    {
        this(packageRoot, Paths.get(dirStr));

    }
    public ServiceModuleParser(String packageRoot$, Path srcPath$){

         String[] pathParts = packageRoot$.split("\.");
         this.srcPath = Paths.get(srcPath$.toString(), pathParts);
         this.packageRoot = packageRoot$;
         this.qdoxBuilder = new JavaProjectBuilder();
         this.qdoxBuilder.addSourceTree(srcPath$.toFile());


    }

    public ActionModuleGraph parse(){

        File[] moduleDirArr = srcPath.resolve("actions").toFile().listFiles();
        List<String> actionModuleName = Arrays.stream(moduleDirArr)
                .filter(file -> file.isDirectory())
                .map(file -> file.getName())
                .collect(Collectors.toList());

        ActionModuleGraph moduleGraph = new ActionModuleGraph(actionModuleName);

        filterActiveActions0(moduleGraph);
        filterValidationModules0(moduleGraph);
        filterAuthModules0(moduleGraph);


        return moduleGraph;
    }
    private void filterActiveActions0(ActionModuleGraph moduleGraph$){

       qdoxBuilder
            .getClasses()
            .stream()
            .filter(clz -> clz.isA(BizAction.class.getName())
                    && QdoxUtil.hasAnnotation(clz, Active.class))
            .forEach(clz -> {
                    Either<Violation, Pair<String, String>> pkgEither = parseModulePackage0(clz.getPackageName());
                    if(pkgEither.isRight()){
                        Pair<String, String> pkgPair = pkgEither.right();
                        if("command".equals(pkgPair.second) || "query".equals(pkgPair.second))
                        {
                            moduleGraph$.addAction(pkgPair.first, clz);
                        }
                    }
                    else{

                        String msg = "Invalid package layout for action: "+clz.getFullyQualifiedName();
                        msg += "
  package format should be actions.$module_name.{command/query}";
                        throw new IllegalStateException(msg);
                    }
                } // peeking ends here
            );

    }

    private void filterValidationModules0(ActionModuleGraph moduleGraph$){
        qdoxBuilder
            .getClasses()
            .stream()
            .filter(clz -> QdoxUtil.hasAnnotation(clz, Module.class)
                    && QdoxUtil.hasAnnotation(clz, ActionValidation.class)
            )
            .forEach(clz -> {
                Either<Violation, Pair<String, String>> pkgEither = parseModulePackage0(clz.getPackageName());
                if(pkgEither.isRight()){
                    Pair<String, String> pkgPair = pkgEither.right();
                    if("validation".equals(pkgPair.second))
                    {
                        moduleGraph$.addVldProvider(pkgPair.first, clz);
                    }
                }
                else{

                    String msg = "Invalid package layout for validation: "+clz.getFullyQualifiedName();
                    msg += "
  package format should be actions.$module_name.validation";
                    throw new IllegalStateException(msg);
                }
            });

        for(ActionModuleGraph.ModuleMeta meta : moduleGraph$.moduleMetaMap.values()){
            if(meta.hasAction() && !meta.hasVldProvider()){
                String msg = String.format("module %s contains %d actions but has no validation", meta.name, meta.actionCount);
                msg += "
  NOTE: validation providing clz must have @Module and @ActionValidation annotations";
                throw new IllegalStateException(msg);
            }
        }
    }

    private void filterAuthModules0(ActionModuleGraph moduleGraph$){
        qdoxBuilder
            .getClasses()
            .stream()
            .filter(clz ->  QdoxUtil.hasAnnotation(clz, Module.class)
                    && QdoxUtil.hasAnnotation(clz, ActionAuth.class)
            )
            .forEach(clz -> moduleGraph$.addAuthProvider(clz)); ;

        String authDefinedPkg = packageRoot+".deploy";


        if(moduleGraph$.authProviderList.size() == 1){
            JavaClass clz = moduleGraph$.authProviderList.get(0);
            if(clz.getPackageName().equals(authDefinedPkg))
                return;
        }
        // well then auth providing clz has invalid package layout

        String msg = "missing auth provider clz in: "+packageRoot+".deploy";
        msg += "
  auth providing clz must be annotated with @Module and @ActionAuth";
        throw new IllegalStateException(msg);

    }


    private Either<Violation, Pair<String, String>> parseModulePackage0(String actionPkgName$){
        String actionRoot = packageRoot+".actions.";
        if(actionPkgName$.startsWith(actionRoot) == false)
            return Either.left(Violation.of("ACTION_PACKAGE_ROOT"));
        Integer prefixIdx = (packageRoot+".actions.").length();
        String modulePkg = actionPkgName$.substring(prefixIdx);
        String[] splits = modulePkg.split("\.");
        if(splits.length != 2)
            return Either.left(Violation.of("VALID_MODULE_HIERARCHY"));

        return Either.right(Pair.of(splits[0], splits[1]));

    }


}
