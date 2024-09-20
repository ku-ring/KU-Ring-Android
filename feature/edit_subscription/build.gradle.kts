import com.ku_stacks.ku_ring.buildlogic.dsl.implementation

plugins {
    kuring("view")
    kuring("compose")
}

android {
    namespace = "com.ku_stacks.ku_ring.edit_subscription"
}

dependencies {
    implementation(projects.core.util)
    implementation(projects.core.designsystem)
    implementation(projects.core.uiUtil)
    implementation(projects.core.thirdparty)
    implementation(projects.core.preferences)
    implementation(projects.data.domain)
    implementation(projects.data.department)
    implementation(projects.data.notice)

    implementation(libs.bundles.compose.interop)
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)
    testImplementation(libs.kotlinx.coroutines.test)
}
