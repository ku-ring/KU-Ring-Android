import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("feature")
    kuringPrimitive("retrofit")
    kuringPrimitive("okhttp")
    kuringPrimitive("test")
    kuringPrimitive("room")
}

android {
    setNameSpace("notice")
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
    testImplementation(projects.data.notice.test)

    implementation(libs.bundles.paging)
    testImplementation(libs.kotlinx.coroutines.test)
}
