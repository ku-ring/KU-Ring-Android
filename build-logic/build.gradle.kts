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
    }
}
