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
