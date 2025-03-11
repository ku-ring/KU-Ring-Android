import com.ku_stacks.ku_ring.buildlogic.dsl.implementation

plugins {
    kuring("view")
    kuring("compose")
}

android {
    namespace = "feature.auth"
}

dependencies {
    implementation(projects.core.util)
    implementation(projects.core.designsystem)

    implementation(libs.bundles.compose.interop)
}