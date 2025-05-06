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
    implementation(projects.core.uiUtil)
    implementation(projects.core.designsystem)
    implementation(projects.data.domain)
    implementation(projects.data.notice)
    implementation(projects.domain.noticecomment)

    implementation(libs.bundles.compose.interop)
}
