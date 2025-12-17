pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
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
    ":core:util",
    ":core:ui_util",
    ":data:domain",
    ":data:domain:testUtils",
    ":data:notice",
    ":data:notice:test",
    ":data:noticecomment",
    ":core:preferences",
    ":data:academicevent",
    ":data:ai",
    ":data:user",
    ":data:staff",
    ":data:department",
    ":data:department:test",
    ":data:library",
    ":data:local",
    ":data:local:test",
    ":data:remote",
    ":data:report",
    ":data:space",
    ":data:search",
    ":data:verification",
    ":domain:user",
    ":domain:noticecomment",
    ":domain:report",
    ":domain:academicevent",
    ":domain:navigation",
    ":core:thirdparty",
    ":core:work",
    ":core:testUtil",
    ":feature:auth",
    ":feature:edit_subscription",
    ":feature:feedback",
    ":feature:library",
    ":feature:notice_detail",
    ":feature:notion",
    ":feature:onboarding",
    ":feature:splash",
    ":feature:main",
    ":core:designsystem",
    ":feature:kuringbot",
    ":feature:edit_departments",
)
