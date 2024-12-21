import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("view")
    kuring("compose")
}

android {
    setNameSpace("feature.library")
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.core.util)
    implementation(projects.data.domain)
    implementation(projects.data.library)
    testImplementation(projects.core.testUtil)

    implementation(libs.bundles.compose.interop)
}