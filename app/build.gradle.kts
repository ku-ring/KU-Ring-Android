import java.io.FileInputStream
import java.util.Properties

plugins {
    kuring("application")
    kuringPrimitive("test")
    kuringPrimitive("android-test")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.android.gms.oss-licenses-plugin")
}

val keystorePropertiesFile = rootProject.file("app/signing/keystore.properties")
val keystoreFile = rootProject.file("app/signing/ku_ring_keystore.jks")
val localPropertiesFile = rootProject.file("local.properties")

android {
    namespace = "com.ku_stacks.ku_ring"
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
    signingConfigs {
        create("release") {
            if (keystorePropertiesFile.exists()) {
                val keystoreProperties = Properties()
                keystoreProperties.load(FileInputStream(keystorePropertiesFile))

                storeFile = keystoreFile
                keyAlias = keystoreProperties["keyAlias"] as? String ?: ""
                keyPassword = keystoreProperties["keyPassword"] as? String ?: ""
                storePassword = keystoreProperties["storePassword"] as? String ?: ""
            }
        }
    }

    defaultConfig {
        applicationId = "com.ku_stacks.ku_ring"

        val localProperties = Properties()
        localProperties.load(FileInputStream(localPropertiesFile))

        buildConfigField(
            "String",
            "APPS_FLYER_DEV_KEY",
            localProperties["APPS_FLYER_DEV_KEY"] as? String ?: ""
        )
        buildConfigField(
            "String",
            "SENDBIRD_APP_ID",
            localProperties["SENDBIRD_APP_ID"] as? String ?: ""
        )
        buildConfigField(
            "String",
            "SENDBIRD_API_TOKEN",
            localProperties["SENDBIRD_API_TOKEN"] as? String ?: ""
        )
        javaCompileOptions {
            annotationProcessorOptions {
                arguments(mapOf("room.schemaLocation" to "$projectDir/schemas"))
            }
        }
    }
    sourceSets {
        getByName("androidTest") {
            assets.srcDirs(files("$projectDir/schemas"))
        }
    }
    buildTypes {
        debug {
            isMinifyEnabled = false
            manifestPlaceholders.putAll(
                mapOf(
                    "appName" to "@string/app_name_debug",
                    "appIcon" to "@drawable/ic_ku_ring_launcher_dev"
                )
            )
            applicationIdSuffix = ".debug"
        }
        release {
            isDebuggable = false
            signingConfig = signingConfigs.getByName("release")
            manifestPlaceholders.putAll(
                mapOf(
                    "appName" to "@string/app_name",
                    "appIcon" to "@drawable/ic_ku_ring_launcher"
                )
            )
        }
    }
    lint {
        disable.add("Instantiatable")
    }
}

dependencies {
    implementation(projects.core.util)
    implementation(projects.core.uiUtil)
    implementation(projects.core.thirdparty)
    implementation(projects.data.domain)
    implementation(projects.feature.editDepartments)
    implementation(projects.feature.editSubscription)
    implementation(projects.feature.feedback)
    implementation(projects.feature.noticeDetail)
    implementation(projects.feature.notion)
    implementation(projects.feature.onboarding)
    implementation(projects.feature.splash)
    implementation(projects.feature.main)

    implementation(libs.androidx.startup.runtime)
    testImplementation(libs.kotlinx.coroutines.test)
    implementation(libs.play.services.oss.licenses)
    implementation(libs.bundles.androidx.work)
    debugImplementation(libs.leakcanary.android)
    implementation(libs.play.services.auth)
    androidTestImplementation(libs.androidx.navigation.testing)
    androidTestImplementation(libs.androidx.espresso.intents)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics.ktx)
}
