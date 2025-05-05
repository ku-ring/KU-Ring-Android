import com.ku_stacks.ku_ring.buildlogic.dsl.implementation
import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace

plugins {
    kuring("feature")
    kuringPrimitive("test")
}

android {
    setNameSpace("domain.noticecomment")
}

dependencies {
    implementation(projects.data.domain)

    implementation(libs.bundles.paging)
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.kotlinx.coroutines.test)

    testImplementation(libs.androidx.paging.testing)
}