package com.ku_stacks.ku_ring.buildlogic.primitive

import com.ku_stacks.ku_ring.buildlogic.dsl.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidTestPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        dependencies {
           "androidTestImplementation"(libs.findBundle("android-test").get())
            "androidTestImplementation"(platform(libs.findLibrary("compose-bom").get()))
            "androidTestImplementation"(libs.findLibrary("compose-ui-test").get())
            "androidTestImplementation"(libs.findLibrary("compose-ui-test-junit4").get())
            "debugImplementation"(libs.findLibrary("compose-ui-test-manifest").get())
        }
    }
}
