import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace


plugins {
    kuring("compose")
}

android {
    setNameSpace("calendar")
}


dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.core.util)
    implementation(projects.data.domain)

    implementation(libs.bundles.compose.interop)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.immutable)
}