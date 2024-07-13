import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("feature")
    kuringPrimitive("retrofit")
    kuringPrimitive("test")
}

android {
    setNameSpace("space")
}

dependencies {
    implementation(projects.core.util)
    implementation(projects.data.remote)

    testImplementation(libs.kotlinx.coroutines.test)

    // okHttp
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    testImplementation(libs.okhttp.mockwebserver)
}
