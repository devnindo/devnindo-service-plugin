package io.devnindo.serviceplugin.write;

import com.squareup.javapoet.ClassName;
import io.devnindo.service.deploy.components.ActionKey;
import io.devnindo.service.exec.action.BizAction;

public interface PoetClz{
    ClassName ACTION_KEY = ClassName.get(ActionKey.class);
    ClassName BIZ_ACTION = ClassName.get(BizAction.class);
    ClassName DAGGER_MODULE = ClassName.get("dagger", "Module");
    ClassName DAGGER_BIND = ClassName.get("dagger", "Binds");
    ClassName DAGGER_INTO_MAP = ClassName.get("dagger.multibindings", "IntoMap");

}