import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("view")
    kuring("compose")
    kuringPrimitive("retrofit")
    kuringPrimitive("junit5")
}

android {
    setNameSpace("club")
    namespace = "com.ku_stacks.ku_ring.club"
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.core.preferences)
    implementation(projects.core.ui)
    implementation(projects.data.domain)

    implementation(libs.bundles.compose.interop)
    implementation(libs.kotlinx.immutable)
}