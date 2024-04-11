import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("feature")
}

android {
    setNameSpace("tracker")
}

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)
}
