import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("view")
    kuring("compose")
    kuringPrimitive("retrofit")
    kuringPrimitive("test")
}

android {
    setNameSpace("main")
    namespace = "com.ku_stacks.ku_ring.main"
}

dependencies {
    implementation(projects.core.util)
    implementation(projects.core.uiUtil)
    implementation(projects.core.designsystem)
    implementation(projects.core.preferences)
    implementation(projects.core.thirdparty)
    implementation(projects.data.domain)
    implementation(projects.data.department)
    implementation(projects.data.notice)
    implementation(projects.data.push)
    implementation(projects.data.staff)
    implementation(projects.data.search)

    implementation(libs.bundles.compose.interop)
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)
    implementation(libs.play.services.auth)
    implementation(libs.bundles.paging)
    implementation(libs.shimmer)
    implementation(libs.androidx.swiperefreshlayout)
}
