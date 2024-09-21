import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("feature")
    kuringPrimitive("test")
}

android {
    setNameSpace("staff")
    namespace = "com.ku_stacks.ku_ring.staff"
}

dependencies {
    implementation(projects.data.domain)
    implementation(projects.data.local)
    implementation(projects.data.remote)
}
