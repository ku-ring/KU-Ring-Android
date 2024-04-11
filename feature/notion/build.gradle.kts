import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("view")
}

android {
    setNameSpace("notion")
}

dependencies {
    implementation(projects.core.uiUtil)
}
