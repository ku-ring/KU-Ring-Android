plugins {
    `kotlin-dsl`
}

group = "com.ku_stacks.ku_ring.buildlogic"

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(libs.kotlin.gradle)
    compileOnly(libs.android.gradle)
    compileOnly(libs.compose.compiler.plugin)
}

gradlePlugin {
    plugins {
        register("com.ku_stacks.ku_ring.buildlogic.primitive.KotlinPlugin") {
            id = "com.ku_stacks.ku_ring.buildlogic.primitive.kotlin"
            implementationClass = "com.ku_stacks.ku_ring.buildlogic.primitive.KotlinPlugin"
        }
        register("com.ku_stacks.ku_ring.buildlogic.primitive.ComposePlugin") {
            id = "com.ku_stacks.ku_ring.buildlogic.primitive.compose"
            implementationClass = "com.ku_stacks.ku_ring.buildlogic.primitive.ComposePlugin"
        }
        register("com.ku_stacks.ku_ring.buildlogic.primitive.FirebasePlugin") {
            id = "com.ku_stacks.ku_ring.buildlogic.primitive.firebase"
            implementationClass = "com.ku_stacks.ku_ring.buildlogic.primitive.FirebasePlugin"
        }
        register("com.ku_stacks.ku_ring.buildlogic.primitive.HiltPlugin") {
            id = "com.ku_stacks.ku_ring.buildlogic.primitive.hilt"
            implementationClass = "com.ku_stacks.ku_ring.buildlogic.primitive.HiltPlugin"
        }
        register("com.ku_stacks.ku_ring.buildlogic.primitive.RoomPlugin") {
            id = "com.ku_stacks.ku_ring.buildlogic.primitive.room"
            implementationClass = "com.ku_stacks.ku_ring.buildlogic.primitive.RoomPlugin"
        }
        register("com.ku_stacks.ku_ring.buildlogic.primitive.KtorPlugin") {
            id = "com.ku_stacks.ku_ring.buildlogic.primitive.ktor"
            implementationClass = "com.ku_stacks.ku_ring.buildlogic.primitive.KtorPlugin"
        }
        register("com.ku_stacks.ku_ring.buildlogic.primitive.OkHttpPlugin") {
            id = "com.ku_stacks.ku_ring.buildlogic.primitive.okhttp"
            implementationClass = "com.ku_stacks.ku_ring.buildlogic.primitive.OkHttpPlugin"
        }
        register("com.ku_stacks.ku_ring.buildlogic.primitive.RetrofitPlugin") {
            id = "com.ku_stacks.ku_ring.buildlogic.primitive.retrofit"
            implementationClass = "com.ku_stacks.ku_ring.buildlogic.primitive.RetrofitPlugin"
        }
        register("com.ku_stacks.ku_ring.buildlogic.primitive.TestPlugin") {
            id = "com.ku_stacks.ku_ring.buildlogic.primitive.test"
            implementationClass = "com.ku_stacks.ku_ring.buildlogic.primitive.TestPlugin"
        }
        register("com.ku_stacks.ku_ring.buildlogic.primitive.CommonAndroidPlugin") {
            id = "com.ku_stacks.ku_ring.buildlogic.primitive.common-android"
            implementationClass = "com.ku_stacks.ku_ring.buildlogic.primitive.CommonAndroidPlugin"
        }
        register("com.ku_stacks.ku_ring.buildlogic.primitive.AndroidTestPlugin") {
            id = "com.ku_stacks.ku_ring.buildlogic.primitive.android-test"
            implementationClass = "com.ku_stacks.ku_ring.buildlogic.primitive.AndroidTestPlugin"
        }
        register("com.ku_stacks.ku_ring.buildlogic.convention.KotlinJvmPlugin") {
            id = "com.ku_stacks.ku_ring.buildlogic.kotlin"
            implementationClass = "com.ku_stacks.ku_ring.buildlogic.convention.KotlinJvmPlugin"
        }
        register("com.ku_stacks.ku_ring.buildlogic.convention.ViewBasedFeaturePlugin") {
            id = "com.ku_stacks.ku_ring.buildlogic.view"
            implementationClass =
                "com.ku_stacks.ku_ring.buildlogic.convention.ViewBasedFeaturePlugin"
        }
        register("com.ku_stacks.ku_ring.buildlogic.convention.ComposePlugin") {
            id = "com.ku_stacks.ku_ring.buildlogic.compose"
            implementationClass = "com.ku_stacks.ku_ring.buildlogic.convention.ComposePlugin"
        }
        register("com.ku_stacks.ku_ring.buildlogic.convention.FeaturePlugin") {
            id = "com.ku_stacks.ku_ring.buildlogic.feature"
            implementationClass = "com.ku_stacks.ku_ring.buildlogic.convention.FeaturePlugin"
        }
        register("com.ku_stacks.ku_ring.buildlogic.convention.ApplicationPlugin") {
            id = "com.ku_stacks.ku_ring.buildlogic.application"
            implementationClass = "com.ku_stacks.ku_ring.buildlogic.convention.ApplicationPlugin"
        }
    }
}
