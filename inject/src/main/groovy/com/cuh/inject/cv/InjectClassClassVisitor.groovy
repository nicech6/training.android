package com.cuh.inject.cv

import com.cuh.inject.mv.FastClickMethodVisitor
import com.cuh.inject.util.AndroidUtils
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
    }

    @Override
    MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        print("----------visitMethod++++++++" + "access-" + access + "name-" + name + 'descriptor-' + descriptor + "signature-" + signature + "exceptions-" + exceptions + "\n")
        def originalVm = cv.visitMethod(access, name, descriptor, signature, exceptions)
        if (AndroidUtils.isViewOnclickMethod(access, name, descriptor)) {
            print("name"+name+access)
//            return new CostTimeMethodVisitor(Opcodes.ASM7, originalVm, access, name, descriptor)
            return new FastClickMethodVisitor(Opcodes.ASM7, access, descriptor, originalVm)
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions)
    }
}