package com.cuh.inject.util

import org.objectweb.asm.Opcodes

class AndroidUtils implements Opcodes {

    def C_View = "android/view/View"
    def D_View = "L$C_View;"

    def C_View_OnClickListener = "$C_View\$OnClickListener"
    def D_View_OnClickListener = "L$C_View_OnClickListener;"

    static boolean isPrivate(int access) {
        return (access & ACC_PRIVATE) != 0;
    }

    static boolean isPublic(int access) {
        return (access & ACC_PUBLIC) != 0;
    }

    static boolean isStatic(int access) {
        return (access & ACC_STATIC) != 0;
    }

    static boolean isAbstract(int access) {
        return (access & ACC_ABSTRACT) != 0;
    }

    static boolean isbridge(int access) {
        return (access & ACC_BRIDGE) != 0;
    }

    static boolean isSynthetic(int access) {
        return (access & ACC_SYNTHETIC) != 0;
    }

    static boolean isViewOnclickMethod(int access, String name, String desc) {
        return (isPublic(access) && !isStatic(access) && !isAbstract(access) //
                && name == "onClick" //
                && desc == "(Landroid/view/View;)V");
    }
}