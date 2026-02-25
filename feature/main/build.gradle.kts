import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace
import java.util.Properties

plugins {
    kuring("view")
    kuring("compose")
    kuringPrimitive("retrofit")
    kuringPrimitive("junit5")
}

val properties = Properties().apply {
    load(project.rootProject.file("local.properties").inputStream())
}

android {
    setNameSpace("main")
    namespace = "com.ku_stacks.ku_ring.main"

    defaultConfig {
        manifestPlaceholders["naverClientId"] = properties["naver.client.id"] as String
    }
}

dependencies {
    implementation(projects.core.util)
    implementation(projects.core.ui)
    implementation(projects.core.designsystem)
    implementation(projects.core.preferences)
    implementation(projects.core.composeLocals)
    implementation(projects.core.firebaseAnalytics)
    implementation(projects.data.domain)
    implementation(projects.data.department)
    implementation(projects.data.notice)
    implementation(projects.data.staff)
    implementation(projects.data.search)
    implementation(projects.domain.user)
    implementation(projects.domain.academicevent)
    implementation(projects.domain.place)
    implementation(projects.domain.navigation)
    implementation(projects.domain.club)

    implementation(libs.bundles.compose.interop)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.immutable)
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)
    implementation(libs.play.services.auth)
    implementation(libs.bundles.paging)
    implementation(libs.shimmer)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.bundles.naver.map)
}
