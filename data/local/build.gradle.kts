plugins {
    kuring("feature")
    kuringPrimitive("test")
    id("java-test-fixtures")
}

android {
    namespace = "com.ku_stacks.ku_ring.local"

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
    testFixtures {
        enable = true
    }
}

dependencies {
    implementation(libs.bundles.paging)
    testImplementation(libs.kotlinx.coroutines.test)
}
