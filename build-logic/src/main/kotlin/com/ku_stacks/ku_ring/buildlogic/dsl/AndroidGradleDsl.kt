package com.ku_stacks.ku_ring.buildlogic.dsl

import com.android.build.gradle.TestedExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

fun Project.android(action: TestedExtension.() -> Unit) {
    extensions.configure(action)
}
