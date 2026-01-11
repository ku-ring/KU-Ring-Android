import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("compose")
}

android {
    setNameSpace("edit_departments")
    packaging.resources.excludes.add("META-INF/{AL2.0,LGPL2.1}")
}

dependencies {
    implementation(projects.core.util)
    implementation(projects.core.ui)
    implementation(projects.core.designsystem)
    implementation(projects.core.composeLocals)
    implementation(projects.core.firebaseAnalytics)
    implementation(projects.core.preferences)
    implementation(projects.data.domain)
    implementation(projects.data.department)

    implementation(libs.bundles.compose.interop)
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)
    testImplementation(libs.kotlinx.coroutines.test)
}