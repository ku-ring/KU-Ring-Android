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
    implementation(projects.core.util)
    implementation(projects.core.uiUtil)
    implementation(projects.core.thirdparty)
    implementation(projects.core.preferences)
    implementation(projects.data.domain)
    implementation(projects.data.push)
    testImplementation(testFixtures(projects.core.util))
    testImplementation(testFixtures(projects.data.local))

    implementation(libs.holdableswipehandler)
    implementation(libs.androidtagview)
}
