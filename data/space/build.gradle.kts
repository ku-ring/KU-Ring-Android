import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("feature")
    kuringPrimitive("retrofit")
    kuringPrimitive("okhttp")
    kuringPrimitive("test")
}

android {
    setNameSpace("space")
}

dependencies {
    implementation(projects.core.util)
    implementation(projects.data.remote)

    testImplementation(libs.kotlinx.coroutines.test)
}
