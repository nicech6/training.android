package com.cuihai.inject_internal

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class AutoClassVisitor(api: Int, classVisitor: ClassVisitor?) : ClassVisitor(api, classVisitor) {
    private lateinit var interfacesArray: Array<String>
    private var className: String? = null
    private var superName: String? = null
    override fun visit(
        version: Int,
        access: Int,
        name: String,
        signature: String,
        superName: String,
        interfaces: Array<String>
    ) {
        this.interfacesArray = interfaces
        className = name
        this.superName = superName
        super.visit(version, access, name, signature, superName, interfaces)
    }

    override fun visitMethod(
        access: Int,
        name: String,
        desc: String,
        signature: String,
        exceptions: Array<String>
    ): MethodVisitor {
        val methodVisitor = super.visitMethod(access, name, desc, signature, exceptions)
        if (name == "onClick" && desc == "(Landroid/view/View;)V" && interfacesArray.contains("android/view/View\$OnClickListener")) {
//            println className +" " + superName + " " + access + " " + name + " " + desc
//            Log.i("visitMethod", "$className $superName $access $name $desc")
            methodVisitor.visitLdcInsn("HookLog")
            methodVisitor.visitVarInsn(Opcodes.ALOAD, 1)
            methodVisitor.visitMethodInsn(
                Opcodes.INVOKEVIRTUAL,
                "android/view/View",
                "toString",
                "()Ljava/lang/String;",
                false
            )
            methodVisitor.visitMethodInsn(
                Opcodes.INVOKESTATIC,
                "android/util/Log",
                'e'.toString(),
                "(Ljava/lang/String;Ljava/lang/String;)I",
                false
            )
        }
        return methodVisitor
    }
}