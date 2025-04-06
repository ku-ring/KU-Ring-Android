import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("feature")
    kuringPrimitive("retrofit")
    kuringPrimitive("test")
}

android {
    setNameSpace("library")

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(projects.core.util)
    implementation(projects.data.domain)
    implementation(projects.data.remote)

    testImplementation(libs.kotlinx.coroutines.test)
}