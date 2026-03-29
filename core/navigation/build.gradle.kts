import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("feature")
}

android {
    setNameSpace("core.navigation")
}

dependencies {
    api(libs.navigation3.runtime)
}
