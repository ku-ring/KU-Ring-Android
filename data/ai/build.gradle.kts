import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("feature")
    kuringPrimitive("room")
    kuringPrimitive("ktor")
    kuringPrimitive("test")
}

android {
    setNameSpace("ai")

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(projects.data.domain)
    implementation(projects.data.remote)
    implementation(projects.data.local)

    testImplementation(libs.kotlinx.coroutines.test)
}