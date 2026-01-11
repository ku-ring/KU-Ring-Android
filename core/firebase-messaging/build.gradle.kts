import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("feature")
}

android {
    setNameSpace("firebase.messaging")
}

dependencies {
    implementation(projects.core.util)
    implementation(projects.core.preferences)
    implementation(projects.core.work)
    implementation(projects.core.designsystem)
    implementation(projects.data.local)
    implementation(projects.domain.navigation)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging.ktx)
}
