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
    implementation(projects.common.util)
    implementation(projects.common.uiUtil)
    implementation(projects.common.preferences)
    implementation(projects.common.work)

    // firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)
}
