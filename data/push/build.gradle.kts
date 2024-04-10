import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("feature")
    kuringPrimitive("room")
    kuringPrimitive("retrofit")
    kuringPrimitive("test")
}

android {
    setNameSpace("push")
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(projects.common.util)
    implementation(projects.data.domain)
    implementation(projects.data.local)
    implementation(projects.data.remote)
    testImplementation(testFixtures(projects.data.local))

    testImplementation(libs.kotlinx.coroutines.test)
}
