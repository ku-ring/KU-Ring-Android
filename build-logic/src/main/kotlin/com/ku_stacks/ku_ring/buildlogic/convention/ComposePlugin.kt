package com.ku_stacks.ku_ring.buildlogic.convention

import com.ku_stacks.ku_ring.buildlogic.primitive.CommonAndroidPlugin
import com.ku_stacks.ku_ring.buildlogic.primitive.ComposePlugin
import com.ku_stacks.ku_ring.buildlogic.primitive.FirebasePlugin
import com.ku_stacks.ku_ring.buildlogic.primitive.HiltPlugin
import com.ku_stacks.ku_ring.buildlogic.primitive.KotlinPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

class ComposePlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        apply<CommonAndroidPlugin>()
        apply<FirebasePlugin>()
        apply<HiltPlugin>()
        apply<ComposePlugin>()
        apply<KotlinPlugin>()
    }
}