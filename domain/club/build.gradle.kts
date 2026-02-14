plugins {
    kuring("kotlin")
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(projects.data.domain)

    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)
}
