import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("view")
    kuring("compose")
    kuringPrimitive("test")
}

android {
    setNameSpace("kuringbot")
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.core.preferences)
    implementation(projects.core.util)
    implementation(projects.data.domain)
    implementation(projects.data.ai)
    testImplementation(projects.core.testUtil)

    implementation(libs.bundles.compose.interop)
    testImplementation(libs.kotlinx.coroutines.test)
}