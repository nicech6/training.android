package com.cuh.inject.mv

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.LocalVariablesSorter

class FastClickMethodVisitor extends LocalVariablesSorter implements Opcodes {

    protected FastClickMethodVisitor(int api, int access, String descriptor, MethodVisitor methodVisitor) {
        super(api, access, descriptor, methodVisitor)
    }

    @Override
    void visitCode() {
        super.visitCode()
        AnnotationVisitor av =
                mv.visitAnnotation("Lcom/cuh/inject/annotation/Debounced;", false)
        if (av != null) {
            av.visitEnd()
        }
        mv.visitVarInsn(ALOAD, 1)
        mv.visitMethodInsn(INVOKESTATIC, "com/cuihai/inject_runtime/FastClickUtil",
                "isGoClick", "(Landroid/view/View;)Z", false)
        Label label = new Label()
        mv.visitJumpInsn(IFNE, label)
        mv.visitInsn(RETURN)
        mv.visitLabel(label)
    }
}