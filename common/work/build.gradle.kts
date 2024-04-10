import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("feature")
}

android {
    setNameSpace("work")
}

dependencies {
    implementation(projects.common.util)
    implementation(projects.common.uiUtil)
    implementation(projects.data.user)

    // WorkManager
    api(libs.bundles.androidx.work)
    androidTestImplementation(libs.androidx.work.testing)
}
