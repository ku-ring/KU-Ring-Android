import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("feature")
}

android {
    setNameSpace("local.test")
}

dependencies {
    implementation(projects.data.local)
}