import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("feature")
    kuringPrimitive("test")
}

android {
    setNameSpace("club")

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
    implementation(projects.domain.club)

    implementation(libs.kotlinx.datetime)
    testImplementation(libs.kotlinx.coroutines.test)
}
