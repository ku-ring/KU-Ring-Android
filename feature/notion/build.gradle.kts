import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("view")
    kuring("compose")
}

android {
    setNameSpace("notion")
}

dependencies {
    implementation(projects.core.uiUtil)
    implementation(projects.core.designsystem)
}
