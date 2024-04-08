package com.ku_stacks.ku_ring.buildlogic.convention

import com.ku_stacks.ku_ring.buildlogic.dsl.configureAndroidLibrary
import com.ku_stacks.ku_ring.buildlogic.primitive.CommonAndroidPlugin
import com.ku_stacks.ku_ring.buildlogic.primitive.HiltPlugin
import com.ku_stacks.ku_ring.buildlogic.primitive.KotlinPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

class FeaturePlugin: Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("com.android.library")
        }

        apply<KotlinPlugin>()
        apply<CommonAndroidPlugin>()
        apply<HiltPlugin>()
        configureAndroidLibrary()
    }
}
