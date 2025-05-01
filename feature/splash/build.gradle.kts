import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("compose")
    kuringPrimitive("test")
}

android {
    setNameSpace("splash")
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.core.util)
    implementation(projects.core.uiUtil)
    implementation(projects.core.thirdparty)
    implementation(projects.core.preferences)
    implementation(projects.core.work)
    implementation(projects.data.space)
    implementation(projects.data.department)
    implementation(projects.data.user)
    implementation(projects.domain.user)

    implementation(libs.semver)

    implementation(libs.bundles.compose.interop)

    // WorkManager
    implementation(libs.bundles.androidx.work)
}
