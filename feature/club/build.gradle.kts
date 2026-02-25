import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("view")
    kuring("compose")
    kuringPrimitive("retrofit")
    kuringPrimitive("junit5")
}

android {
    setNameSpace("club")
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.core.preferences)
    implementation(projects.core.ui)
    implementation(projects.core.util)
    implementation(projects.data.domain)
    implementation(projects.domain.club)

    implementation(libs.bundles.compose.interop)
    implementation(libs.kotlinx.immutable)
    implementation(libs.kotlinx.datetime)
    implementation(libs.bundles.naver.map)
    implementation(libs.bundles.coil)
}