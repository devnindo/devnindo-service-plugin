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

package io.devnindo.serviceplugin.utils;

import com.thoughtworks.qdox.model.JavaAnnotation;
import com.thoughtworks.qdox.model.JavaClass;

import java.lang.annotation.Annotation;
import java.util.List;

public final class QdoxUtil
{
    private QdoxUtil(){}

    public static final boolean hasAnnotation(JavaClass clz, Class<? extends Annotation> annot$){

        return hasAnnotation(clz.getAnnotations(), annot$);

    }
    public static final boolean hasAnnotation(List<JavaAnnotation> annotationList, Class<? extends Annotation> annot$){
        for(JavaAnnotation annotation : annotationList)
        {
            String fullName = annotation.getType().getFullyQualifiedName();
            if(annot$.getName().equals(fullName))
                return true;
        }
        return false;

    }
}
