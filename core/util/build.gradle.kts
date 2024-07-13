import com.ku_stacks.ku_ring.buildlogic.dsl.setNameSpace
import java.util.Properties

plugins {
    kuring("feature")
    kuringPrimitive("test")
    kuringPrimitive("okhttp")
    kuringPrimitive("retrofit")
}

val properties = Properties().apply {
    load(rootProject.file("local.properties").inputStream())
}

android {
    setNameSpace("util")

    defaultConfig {
        buildConfigField("String", "APPS_FLYER_DEV_KEY", properties["APPS_FLYER_DEV_KEY"] as? String ?: "")
        buildConfigField("String", "SENDBIRD_APP_ID", properties["SENDBIRD_APP_ID"] as? String ?: "")
        buildConfigField("String", "SENDBIRD_API_TOKEN", properties["SENDBIRD_API_TOKEN"] as? String ?: "")
    }
}

dependencies {
    implementation(projects.core.uiUtil)
    testImplementation(libs.kotlinx.coroutines.test)
}
