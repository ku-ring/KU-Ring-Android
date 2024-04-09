import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("compose")
}

android {
    setNameSpace("edit_departments")
    packaging.resources.excludes.add("META-INF/{AL2.0,LGPL2.1}")
}

dependencies {
    implementation(projects.common.util)
    implementation(projects.common.uiUtil)
    implementation(projects.common.designsystem)
    implementation(projects.common.thirdparty)
    implementation(projects.common.preferences)
    implementation(projects.data.domain)
    implementation(projects.data.department)

    implementation(libs.bundles.compose.interop)
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)
    implementation(libs.bundles.rxjava)
    testImplementation(libs.kotlinx.coroutines.test)
}