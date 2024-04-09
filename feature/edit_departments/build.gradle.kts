import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("compose")
}

android {
    setNameSpace("edit_departments")
    packaging.resources.excludes.add("META-INF/{AL2.0,LGPL2.1}")
}

dependencies {
    implementation(project(":common:util"))
    implementation(project(":common:designsystem"))
    implementation(project(":common:ui_util"))
    implementation(project(":common:thirdparty"))
    implementation(project(":common:preferences"))
    implementation(project(":data:domain"))
    implementation(project(":data:department"))

    implementation(libs.bundles.compose.interop)
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)
    implementation(libs.bundles.rxjava)
    testImplementation(libs.kotlinx.coroutines.test)
}