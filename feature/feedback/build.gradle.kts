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
    implementation(projects.common.util)
    implementation(projects.common.uiUtil)
    implementation(projects.common.designsystem)
    implementation(projects.common.thirdparty)
    implementation(projects.common.preferences)
    implementation(projects.data.domain)
    implementation(projects.data.remote)
    implementation(projects.data.user)
    testImplementation(projects.common.testUtil)

    implementation(libs.bundles.compose.interop)
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)
    implementation(libs.bundles.rxjava)
    implementation(libs.keyboardvisibilityevent)
    testImplementation(libs.kotlinx.coroutines.test)
}
