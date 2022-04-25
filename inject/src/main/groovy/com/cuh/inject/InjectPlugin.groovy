package com.cuh.inject

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

//import com.cuh.inject.VariantConfig

class InjectPlugin implements Plugin<Project> {
    boolean isApp

    @Override
    void apply(Project target) {
        print '---------------apply-' + '\n'
        isApp = target.getPlugins().hasPlugin(AppPlugin.class)
        print '---------------isApp-' + isApp + '\n'
//        def extension
        Map<String, String> map = new HashMap<>()

        if (isApp) {
            def extension = target.getExtensions().getByType(ApplicationExtension.class) as BaseExtension
            print '---------------ApplicationExtension-' + extension
//            target.android.registerTransform(MyTransformKt.newTransform(target))
            def transform = new InjectTransform(target)
            extension.registerTransform(transform)
//            extension.assetPacks
        } else {
            LibraryExtension extension = target.getExtensions().getByType(LibraryExtension.class)
            print '---------------LibraryExtension-' + extension
        }
//        target.android.registerTransform(this)
//        if (extension != null) {
//            extension.registerTransform(this)
//        }
        target.task("aaa") {
            doLast {
                print '---------------hello first plugin'
//                print ("---------------hello first plugin: ${extension.applicationId}")
            }
        }
    }
}

