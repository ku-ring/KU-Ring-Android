plugins {
    kuring("view")
    kuringPrimitive("retrofit")
}

android {
    namespace = "com.ku_stacks.ku_ring.notice_detail"
}

dependencies {
    implementation(projects.common.util)
    implementation(projects.common.uiUtil)
    implementation(projects.data.domain)
    implementation(projects.data.notice)
}
