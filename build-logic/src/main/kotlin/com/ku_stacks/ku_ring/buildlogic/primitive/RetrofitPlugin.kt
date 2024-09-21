package com.ku_stacks.ku_ring.buildlogic.primitive

import com.ku_stacks.ku_ring.buildlogic.dsl.implementation
import com.ku_stacks.ku_ring.buildlogic.dsl.implementationPlatform
import com.ku_stacks.ku_ring.buildlogic.dsl.library
import com.ku_stacks.ku_ring.buildlogic.dsl.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class RetrofitPlugin: Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        dependencies {
            implementationPlatform(libs.library("retrofit-bom"))
            implementation(libs.library("retrofit"))
            implementation(libs.library("retrofit-converter-gson"))
        }
    }
}
