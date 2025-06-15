import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("feature")
    kuringPrimitive("test")
    kuringPrimitive("retrofit")
}

android {
    setNameSpace("report")
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
    implementation(projects.domain.report)

    testImplementation(libs.kotlinx.coroutines.test)
}
