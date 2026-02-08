import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("feature")
}

android {
    setNameSpace("domain.club")
}

dependencies {
    implementation(projects.data.domain)

    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)
}