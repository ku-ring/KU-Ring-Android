pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
        maven { setUrl("https://repo.sendbird.com/public/maven") }
        maven { setUrl("https://repository.map.naver.com/archive/maven") }
    }
}

rootProject.name = "KURing"
include(
    ":app",
    // core modules
    ":core:util",
    ":core:compose-util",
    ":core:ui",
    ":core:designsystem",
    ":core:preferences",
    ":core:work",
    ":core:testUtil",
    ":core:firebase-analytics",
    ":core:firebase-crashlytics",
    ":core:firebase-messaging",
    ":core:compose-locals",
    // data modules
    ":data:domain",
    ":data:domain:testUtils",
    ":data:notice",
    ":data:notice:test",
    ":data:noticecomment",
    ":data:academicevent",
    ":data:ai",
    ":data:user",
    ":data:staff",
    ":data:department",
    ":data:department:test",
    ":data:library",
    ":data:local",
    ":data:local:test",
    ":data:place",
    ":data:remote",
    ":data:report",
    ":data:space",
    ":data:search",
    ":data:verification",
    // domain modules
    ":domain:club",
    ":domain:user",
    ":domain:noticecomment",
    ":domain:report",
    ":domain:academicevent",
    ":domain:place",
    ":domain:navigation",
    // feature modules
    ":feature:auth",
    ":feature:club",
    ":feature:edit_subscription",
    ":feature:feedback",
    ":feature:library",
    ":feature:notice_detail",
    ":feature:notion",
    ":feature:onboarding",
    ":feature:splash",
    ":feature:main",
    ":feature:kuringbot",
    ":feature:edit_departments",
)
