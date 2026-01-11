import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("view")
    kuring("compose")
}

android {
    setNameSpace("notice_detail")
}

dependencies {
    implementation(projects.core.util)
    implementation(projects.core.composeUtil)
    implementation(projects.core.ui)
    implementation(projects.core.designsystem)
    implementation(projects.core.preferences)
    implementation(projects.core.composeLocals)
    implementation(projects.data.domain)
    implementation(projects.data.notice)
    implementation(projects.domain.noticecomment)
    implementation(projects.domain.report)
    implementation(projects.domain.navigation)

    implementation(libs.bundles.compose.interop)
}
