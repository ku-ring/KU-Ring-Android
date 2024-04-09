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
    }
}

rootProject.name = "KURing"
include(
    ":app",
    ":common:util",
    ":common:ui_util",
    ":data:domain",
    ":data:notice",
    ":common:preferences",
    ":data:push",
    ":data:user",
    ":data:staff",
    ":data:department",
    ":data:local",
    ":data:remote",
    ":common:thirdparty",
    ":common:work",
    ":feature:edit_subscription",
    ":feature:feedback",
    ":feature:my_notification",
    ":feature:notice_detail",
    ":feature:notion",
    ":feature:onboarding",
    ":feature:splash",
    ":feature:main",
    ":common:designsystem",
    ":feature:edit_departments"
)
