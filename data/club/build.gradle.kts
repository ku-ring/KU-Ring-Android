import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("feature")
    kuringPrimitive("test")
}

android {
    setNameSpace("data.club")
}

dependencies {
    implementation(projects.core.util)
    implementation(projects.data.domain)
    implementation(projects.data.remote)
    implementation(projects.domain.club)

    implementation(libs.kotlinx.datetime)
    implementation(libs.androidx.paging.common.ktx)
    implementation(libs.androidx.paging.runtime.ktx)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.paging.testing)
}
