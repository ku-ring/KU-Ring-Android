import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("compose")
}

android {
    setNameSpace("designsystem")
}

dependencies {
    implementation(libs.bundles.compose.interop)
}
