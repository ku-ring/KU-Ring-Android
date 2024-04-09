import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("compose")
}

android {
    setNameSpace("onboarding")
}

dependencies {
    implementation(projects.common.uiUtil)
    implementation(projects.common.thirdparty)
    implementation(projects.common.preferences)
    implementation(projects.common.designsystem)
    implementation(projects.data.domain)
    implementation(projects.data.department)

    implementation(libs.bundles.compose.interop)
}
