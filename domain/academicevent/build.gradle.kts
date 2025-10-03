import com.ku_stacks.ku_ring.buildlogic.dsl.implementation

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
    implementation(libs.kotlinx.datetime)
}