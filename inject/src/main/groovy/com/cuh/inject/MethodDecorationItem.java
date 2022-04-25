package com.cuh.inject;

public class MethodDecorationItem {
    String desc;
    String annotationClass;
    String targetClass;
    String targetMethodName;

    @Override
    public String toString() {
        return "MethodDecorationItem{" +
                "desc='" + desc + '\'' +
                ", annotationClass='" + annotationClass + '\'' +
                ", targetClass='" + targetClass + '\'' +
                ", targetMethodName='" + targetMethodName + '\'' +
                '}';
    }
}
