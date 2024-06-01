import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("view")
    kuring("compose")
}

android {
    setNameSpace("notice_detail")
}

dependencies {
    implementation(projects.common.util)
    implementation(projects.common.uiUtil)
    implementation(projects.common.designsystem)
    implementation(projects.data.domain)
    implementation(projects.data.notice)

    implementation(libs.bundles.compose.interop)
}
