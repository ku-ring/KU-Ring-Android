import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("feature")
}

android {
    setNameSpace("firebase.analytics")
}

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics.ktx)
}
