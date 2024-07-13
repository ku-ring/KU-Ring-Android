import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("feature")
    kuringPrimitive("retrofit")
    kuringPrimitive("okhttp")
    kuringPrimitive("test")
}

android {
    setNameSpace("remote")
    buildTypes {
        release {
            buildConfigField("String", "API_BASE_URL", "\"https://ku-ring.com/api/\"")
        }
        debug {
            buildConfigField("String", "API_BASE_URL", "\"https://kuring.herokuapp.com/api/\"")
        }
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(projects.core.util)

    testImplementation(libs.kotlinx.coroutines.test)
}
