import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("feature")
    kuringPrimitive("room")
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
    testImplementation(projects.data.local.test)

    implementation(libs.bundles.paging)
    testImplementation(libs.kotlinx.coroutines.test)
}
