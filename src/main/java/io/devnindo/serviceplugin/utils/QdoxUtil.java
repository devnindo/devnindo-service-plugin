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
