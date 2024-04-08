import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("feature")
}

android {
    setNameSpace("work")

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
    implementation(projects.common.util)
    implementation(projects.common.uiUtil)
    implementation(projects.data.user)

    // WorkManager
    api(libs.bundles.androidx.work)
    androidTestImplementation(libs.androidx.work.testing)
}
