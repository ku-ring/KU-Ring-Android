package com.ku_stacks.ku_ring.buildlogic.convention

import com.android.build.gradle.BaseExtension
import com.ku_stacks.ku_ring.buildlogic.dsl.debugImplementation
import com.ku_stacks.ku_ring.buildlogic.dsl.implementation
import com.ku_stacks.ku_ring.buildlogic.dsl.library
import com.ku_stacks.ku_ring.buildlogic.dsl.libs
import com.ku_stacks.ku_ring.buildlogic.primitive.CommonAndroidPlugin
import com.ku_stacks.ku_ring.buildlogic.primitive.FirebasePlugin
import com.ku_stacks.ku_ring.buildlogic.primitive.HiltPlugin
import com.ku_stacks.ku_ring.buildlogic.primitive.KotlinPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class ViewBasedFeaturePlugin: Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        apply<CommonAndroidPlugin>()
        apply<FirebasePlugin>()
        apply<HiltPlugin>()
        apply<KotlinPlugin>()

        extensions.getByType<BaseExtension>().apply {
            buildFeatures.apply {
                viewBinding = true
            }
            dataBinding {
                enable = true
            }
        }
        dependencies {
            implementation(libs.library("androidx-fragment-ktx"))
            debugImplementation(libs.library("androidx-fragment-testing"))
        }
    }
}
