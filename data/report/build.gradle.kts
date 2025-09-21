import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("feature")
    kuringPrimitive("retrofit")
}

android {
    setNameSpace("report")
}

dependencies {
    implementation(projects.core.util)
    implementation(projects.data.domain)
    implementation(projects.data.local)
    implementation(projects.data.remote)
    implementation(projects.domain.report)
}
