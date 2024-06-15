import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("feature")
    kuringPrimitive("test")
}

android {
    setNameSpace("testUtil")
}

dependencies {
    implementation(libs.androidx.lifecycle.livedata)
    implementation(libs.bundles.rxjava)
    implementation(libs.bundles.unit.test)
}