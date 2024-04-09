import com.ku_stacks.ku_ring.buildlogic.dsl.implementation

plugins {
    kuring("view")
    kuring("compose")
}

android {
    namespace = "com.ku_stacks.ku_ring.edit_subscription"
}

dependencies {
    implementation(projects.common.util)
    implementation(projects.common.designsystem)
    implementation(projects.common.uiUtil)
    implementation(projects.common.thirdparty)
    implementation(projects.common.preferences)
    implementation(projects.data.domain)
    implementation(projects.data.department)
    implementation(projects.data.notice)

    implementation(libs.bundles.compose.interop)
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)
    implementation(libs.bundles.rxjava)
    testImplementation(libs.kotlinx.coroutines.test)
}
