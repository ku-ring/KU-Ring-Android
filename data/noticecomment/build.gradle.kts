import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("feature")
    kuringPrimitive("retrofit")
    kuringPrimitive("okhttp")
    kuringPrimitive("test")
}

android {
    setNameSpace("noticecomment")
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
    implementation(projects.domain.noticecomment)

    implementation(libs.bundles.paging)
    testImplementation(libs.kotlinx.coroutines.test)
}
