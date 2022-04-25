package com.cuh.inject;

import org.gradle.internal.impldep.org.objectweb.asm.Label;
import org.gradle.internal.impldep.org.objectweb.asm.MethodVisitor;
import org.gradle.internal.impldep.org.objectweb.asm.commons.AdviceAdapter;

public class FastMethodVisitor extends AdviceAdapter {
    FastMethodVisitor(int api, MethodVisitor methodVisitor, int access, String name, String descriptor) {
        super(api, methodVisitor, access, name, descriptor);
    }

    //方法进入
    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();
//        mv.visitMethodInsn(INVOKESTATIC, "com/cuh/inject/FastClickUtil", "isFastDoubleClick", "()Z", false);
//        Label label = new Label();
//        mv.visitJumpInsn(IFEQ, label);
//        mv.visitInsn(RETURN);
//        mv.visitLabel(label);
    }
}
