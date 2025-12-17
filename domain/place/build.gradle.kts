plugins {
    kuring("kotlin")
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(projects.data.domain)
}