import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("feature")
    kuringPrimitive("test")
    `java-test-fixtures`
}

android {
    setNameSpace("local")

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
