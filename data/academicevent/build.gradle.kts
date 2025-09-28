import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("feature")
    kuringPrimitive("room")
    kuringPrimitive("retrofit")
    kuringPrimitive("test")
}

android {
    setNameSpace("academicevent")

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(projects.core.util)
    implementation(projects.data.domain)
    implementation(projects.data.local)
    implementation(projects.data.remote)
}