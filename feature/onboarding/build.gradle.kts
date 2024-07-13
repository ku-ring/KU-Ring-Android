import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("compose")
}

android {
    setNameSpace("onboarding")
}

dependencies {
    implementation(projects.core.uiUtil)
    implementation(projects.core.thirdparty)
    implementation(projects.core.preferences)
    implementation(projects.core.designsystem)
    implementation(projects.data.domain)
    implementation(projects.data.department)

    implementation(libs.bundles.compose.interop)
}
