package com.ku_stacks.ku_ring.buildlogic.primitive

import com.ku_stacks.ku_ring.buildlogic.dsl.implementation
import com.ku_stacks.ku_ring.buildlogic.dsl.implementationPlatform
import com.ku_stacks.ku_ring.buildlogic.dsl.library
import com.ku_stacks.ku_ring.buildlogic.dsl.libs
import com.ku_stacks.ku_ring.buildlogic.dsl.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class OkHttpPlugin: Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        dependencies {
            implementationPlatform(libs.library("okhttp-bom"))
            implementation(libs.library("okhttp"))
            implementation(libs.library("okhttp-logging-interceptor"))
            testImplementation(libs.library("okhttp-mockwebserver"))
        }
    }
}
