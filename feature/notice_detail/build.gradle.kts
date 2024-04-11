import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("view")
    kuringPrimitive("retrofit")
}

android {
    setNameSpace("notice_detail")
}

dependencies {
    implementation(projects.core.util)
    implementation(projects.core.uiUtil)
    implementation(projects.data.domain)
    implementation(projects.data.notice)
}
