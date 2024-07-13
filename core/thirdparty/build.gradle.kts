import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("feature")
    kuring("compose")
}

android {
    setNameSpace("thirdparty")
}

dependencies {
    implementation(projects.data.local)
    implementation(projects.core.util)
    implementation(projects.core.uiUtil)
    implementation(projects.core.preferences)
    implementation(projects.core.work)

    // firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)
}
