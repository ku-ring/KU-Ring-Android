import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("feature")
    kuringPrimitive("test")
    kuringPrimitive("okhttp")
    kuringPrimitive("retrofit")
}

android {
    setNameSpace("util")
}

dependencies {
    implementation(projects.core.uiUtil)
    testImplementation(libs.kotlinx.coroutines.test)
}
