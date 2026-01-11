import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("compose")
}

android {
    setNameSpace("compose_locals")
}

dependencies {
    implementation(projects.core.preferences)
    implementation(projects.core.firebaseAnalytics)
    implementation(projects.domain.navigation)
    implementation(libs.bundles.compose)
}
