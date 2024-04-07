package com.ku_stacks.ku_ring.buildlogic.dsl

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

fun Project.configureAndroidLibrary() {
    android {
        setCompileSdkVersion(libs.version("compileSdk").toInt())

        defaultConfig {
            minSdk = libs.version("minSdk").toInt()
            targetSdk = libs.version("targetSdk").toInt()
        }

        compileOptions {
            isCoreLibraryDesugaringEnabled = true
        }

        dependencies {
            "coreLibraryDesugaring"(libs.findLibrary("desugarLibs").get())
        }
    }
}