import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("feature")
    kuringPrimitive("room")
    kuringPrimitive("retrofit")
    kuringPrimitive("test")
}

android {
    setNameSpace("department")

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(projects.common.util)
    implementation(projects.common.preferences)
    implementation(projects.data.domain)
    implementation(projects.data.local)
    implementation(projects.data.remote)

    testImplementation(projects.data.department.test)
    testImplementation(libs.kotlinx.coroutines.test)
}
