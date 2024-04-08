import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("feature")
}

android {
    setNameSpace("thirdparty")

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(projects.data.local)
    implementation(projects.common.util)
    implementation(projects.common.uiUtil)
    implementation(projects.common.preferences)
    implementation(projects.common.work)

    // firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)
}
