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
    implementation(projects.core.preferences)
    implementation(projects.data.notice)
    implementation(projects.domain.user)
    implementation(projects.domain.navigation)
    implementation(projects.domain.academicevent)

    implementation(libs.kotlinx.datetime)

    // WorkManager
    api(libs.bundles.androidx.work)
    androidTestImplementation(libs.androidx.work.testing)
}
