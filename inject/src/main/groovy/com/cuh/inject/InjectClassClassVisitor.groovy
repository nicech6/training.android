package com.cuh.inject

import org.gradle.internal.impldep.org.objectweb.asm.Label
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class InjectClassClassVisitor extends ClassVisitor {

    InjectClassClassVisitor(int api, ClassVisitor classVisitor) {
        super(api, classVisitor)
    }

    @Override
    void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces)
//        print("------name" + name + "--" + interfaces)
    }

//    @Override
//    MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
//        print("----------visitMethod++++++++" + name + '-----' + descriptor + "\n")
//        if (methodVisitor != null && !name.equals("<init>")) {
//            MethodVisitor newMethodVisitor = new MethodVisitor(api, methodVisitor) {
//                @Override
//                public void visitCode() {
//                    mv.visitCode();
//
//                    mv.visitFieldInsn(Opcodes.GETSTATIC, mOwner, "timer", "J");
//                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System",
//                            "currentTimeMillis", "()J");
//                    mv.visitInsn(Opcodes.LSUB);
//                    mv.visitFieldInsn(Opcodes.PUTSTATIC, mOwner, "timer", "J");
//
//                }
//
//                @Override
//                public void visitInsn(int opcode) {
//
//                    if ((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) || opcode == Opcodes.ATHROW) {
//                        mv.visitFieldInsn(Opcodes.GETSTATIC, mOwner, "timer", "J");
//                        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System",
//                                "currentTimeMillis", "()J");
//                        mv.visitInsn(Opcodes.LADD);
//                        mv.visitFieldInsn(Opcodes.PUTSTATIC, mOwner, "timer", "J");
//                    }
//                    mv.visitInsn(opcode);
//
//                }
//            };
//            return newMethodVisitor;
//        }
//        return super.visitMethod(access, name, descriptor, signature, exceptions)
}