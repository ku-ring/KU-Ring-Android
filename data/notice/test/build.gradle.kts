import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("feature")
}

android {
    setNameSpace("notice.test")
}

dependencies {
    implementation(projects.data.domain)
    implementation(projects.data.local)
    implementation(projects.data.remote)
}