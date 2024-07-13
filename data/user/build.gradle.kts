import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("feature")
    kuringPrimitive("room")
}

android {
    setNameSpace("user")
}

dependencies {
    implementation(projects.core.util)
    implementation(projects.core.preferences)
    implementation(projects.data.local)
    api(projects.data.remote)

    implementation(libs.bundles.rxjava)
    testImplementation(libs.kotlinx.coroutines.test)
}
