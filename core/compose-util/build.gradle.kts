import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("compose")
}

android {
    setNameSpace("compose_util")
}

dependencies {
    implementation(libs.bundles.compose)
}
