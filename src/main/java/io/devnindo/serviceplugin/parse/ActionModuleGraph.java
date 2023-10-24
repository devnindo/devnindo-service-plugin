package io.devnindo.serviceplugin.parse;

import com.thoughtworks.qdox.model.JavaClass;

import java.util.*;

public class ActionModuleGraph
{
    public  final List<JavaClass> actionClzList;
    public final List<JavaClass> vldProviderList; // auth/validation
    public final List<JavaClass> authProviderList;
    public final Map<String, ModuleMeta> moduleMetaMap;

    public ActionModuleGraph(List<String> actionModuleList) {
        actionClzList = new ArrayList<>();
        vldProviderList = new ArrayList<>();
        authProviderList = new ArrayList<>();
        moduleMetaMap = new HashMap<>();
        actionModuleList.forEach(item -> moduleMetaMap.put(item, new ModuleMeta(item)));
    }

    /*public ActionModuleGraph(List<JavaClass> actionClzList, List<JavaClass> vldModuleList, List<JavaClass> authModule) {
        this.actionClzList = actionClzList;
        this.vldProviderList = vldModuleList;
        this.authProviderList = authModule;
    }*/


    public void addAction(String moduleName, JavaClass actionClz)
    {
        ModuleMeta moduleMeta = moduleMetaMap.get(moduleName);
        moduleMeta.incrementActionCount();
        actionClzList.add(actionClz);

    }

    public void addVldProvider(String moduleName, JavaClass vldProviderClz){
        ModuleMeta moduleMeta = moduleMetaMap.get(moduleName);
        moduleMeta.incrementVldProviderCount();
        vldProviderList.add(vldProviderClz);
    }

    public void addAuthProvider(JavaClass authProviderClz){
        authProviderList.add(authProviderClz);
    }

    public class ModuleMeta{
        final String name;
        Integer actionCount;
        Integer vldProviderCount;

        ModuleMeta(String name$){
            name = name$;
            actionCount = 0;
            vldProviderCount = 0;
        }

        void incrementActionCount(){
            actionCount++;
        }
        void incrementVldProviderCount(){
            vldProviderCount++;
        }
        public boolean hasAction(){
            return actionCount > 0;
        }
        public boolean hasVldProvider(){
            return vldProviderCount > 0;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Service Active Action, Validation and Auth provider Graph: ");
        moduleMetaMap.values().forEach(meta -> {
            String moduleInfo = String.format("module %s has %d actions and %d validation providers", meta.name, meta.actionCount, meta.vldProviderCount);
            builder.append("\n  ")
                    .append(moduleInfo);
        });

        builder.append("\n  service auth provider: "+authProviderList.get(0).getFullyQualifiedName());

        return builder.toString();
    }
}
