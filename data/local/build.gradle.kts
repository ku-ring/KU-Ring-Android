import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("feature")
    kuringPrimitive("room")
    kuringPrimitive("test")
}

android {
    setNameSpace("local")

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    testImplementation(projects.data.local.test)

    implementation(libs.bundles.paging)
    implementation(libs.androidx.room.paging)
    testImplementation(libs.kotlinx.coroutines.test)
}
