import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("compose")
    kuring("view")
    kuringPrimitive("test")
}

android {
    setNameSpace("splash")
}

dependencies {
    implementation(projects.common.designsystem)
    implementation(projects.common.util)
    implementation(projects.common.uiUtil)
    implementation(projects.common.thirdparty)
    implementation(projects.common.preferences)
    implementation(projects.common.work)
    implementation(projects.data.space)

    implementation(libs.androidx.constraintlayout)
    implementation(libs.semver)

    implementation(libs.bundles.compose.interop)

    // WorkManager
    implementation(libs.bundles.androidx.work)
}
