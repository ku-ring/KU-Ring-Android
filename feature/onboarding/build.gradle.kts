import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("compose")
}

android {
    setNameSpace("onboarding")
}

dependencies {
    implementation(projects.core.util)
    implementation(projects.core.ui)
    implementation(projects.core.composeLocals)
    implementation(projects.core.firebaseAnalytics)
    implementation(projects.core.preferences)
    implementation(projects.core.designsystem)
    implementation(projects.data.domain)
    implementation(projects.data.department)
    implementation(projects.domain.navigation)

    implementation(libs.bundles.compose.interop)
}
