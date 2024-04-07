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
    }
}
