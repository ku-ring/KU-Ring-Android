import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("feature")
}

android {
    setNameSpace("verification")
}

dependencies {
    implementation(projects.core.util)
    implementation(projects.data.remote)
}
