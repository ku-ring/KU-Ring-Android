package com.ku_stacks.ku_ring.buildlogic.dsl

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

fun Project.configureAndroidLibrary() {
    androidExtension().apply {
        compileSdk = libs.version("compileSdk").toInt()
        defaultConfig.apply {
            minSdk = libs.version("minSdk").toInt()
        }
        compileOptions.apply {
            isCoreLibraryDesugaringEnabled = true
        }
    }

    dependencies {
        "coreLibraryDesugaring"(libs.findLibrary("desugarLibs").get())
    }
}