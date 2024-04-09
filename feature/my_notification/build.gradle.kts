import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("view")
    kuringPrimitive("test")
    kuringPrimitive("retrofit")
}

android {
    setNameSpace("my_notification")
}

dependencies {
    implementation(projects.common.util)
    implementation(projects.common.uiUtil)
    implementation(projects.common.thirdparty)
    implementation(projects.common.preferences)
    implementation(projects.data.domain)
    implementation(projects.data.push)
    testImplementation(testFixtures(projects.common.util))
    testImplementation(testFixtures(projects.data.local))

    implementation(libs.holdableswipehandler)
    implementation(libs.androidtagview)
}
