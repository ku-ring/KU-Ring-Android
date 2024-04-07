package com.ku_stacks.ku_ring.buildlogic.primitive

import com.ku_stacks.ku_ring.buildlogic.dsl.androidTestImplementation
import com.ku_stacks.ku_ring.buildlogic.dsl.bundle
import com.ku_stacks.ku_ring.buildlogic.dsl.libs
import com.ku_stacks.ku_ring.buildlogic.dsl.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class TestPlugin: Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        dependencies {
            testImplementation(libs.bundle("unit-test"))
            androidTestImplementation(libs.bundle("android-test"))
        }
    }
}
