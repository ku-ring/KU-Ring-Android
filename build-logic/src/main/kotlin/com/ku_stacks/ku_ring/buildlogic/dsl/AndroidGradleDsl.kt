package com.ku_stacks.ku_ring.buildlogic.dsl

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

internal fun Project.androidExtension(): CommonExtension =
    extensions.getByType(CommonExtension::class)

fun LibraryExtension.setNameSpace(nameSpace: String) {
    namespace = "com.ku_stacks.ku_ring.${nameSpace}"
}
