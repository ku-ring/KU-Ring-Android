import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("view")
    kuring("compose")
    kuringPrimitive("retrofit")
    kuringPrimitive("junit5")
}

android {
    setNameSpace("notification")
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.core.preferences)
    implementation(projects.core.ui)
    implementation(projects.data.domain)
    implementation(projects.domain.notification)

    implementation(libs.bundles.compose.interop)
    implementation(libs.kotlinx.immutable)
    implementation(libs.androidx.paging.compose)
}
