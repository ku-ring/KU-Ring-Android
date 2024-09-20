import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("view")
    kuring("compose")
    kuringPrimitive("test")
}

android {
    setNameSpace("feedback")
}

dependencies {
    implementation(projects.core.util)
    implementation(projects.core.uiUtil)
    implementation(projects.core.designsystem)
    implementation(projects.core.thirdparty)
    implementation(projects.core.preferences)
    implementation(projects.data.domain)
    implementation(projects.data.remote)
    implementation(projects.data.user)
    testImplementation(projects.core.testUtil)

    implementation(libs.bundles.compose.interop)
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)
    implementation(libs.keyboardvisibilityevent)
    testImplementation(libs.kotlinx.coroutines.test)
}
