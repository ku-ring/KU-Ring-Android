plugins {
    `kotlin-dsl`
}

group = "com.ku_stacks.ku_ring.buildlogic"

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
    }
}
