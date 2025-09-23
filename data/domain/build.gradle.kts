plugins {
    kuring("kotlin")
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(libs.kotlinx.datetime)
}