import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("compose")
}

android {
    setNameSpace("ui")
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.data.domain)
    implementation(libs.bundles.compose.interop)
    implementation(libs.androidx.paging.compose)
    implementation(libs.kotlinx.datetime)
}
