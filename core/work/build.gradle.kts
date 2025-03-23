import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("feature")
}

android {
    setNameSpace("work")
}

dependencies {
    implementation(projects.core.util)
    implementation(projects.core.uiUtil)
    implementation(projects.domain.user)

    // WorkManager
    api(libs.bundles.androidx.work)
    androidTestImplementation(libs.androidx.work.testing)
}
