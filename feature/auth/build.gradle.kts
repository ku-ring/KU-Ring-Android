import com.ku_stacks.ku_ring.buildlogic.dsl.implementation
import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("view")
    kuring("compose")
}

android {
    setNameSpace("feature.auth")
}

dependencies {
    implementation(projects.core.util)
    implementation(projects.core.designsystem)

    implementation(libs.bundles.compose.interop)
}