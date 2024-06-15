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
    ":data:domain:testUtils",
    ":data:notice",
    ":data:notice:test",
    ":common:preferences",
    ":data:push",
    ":data:user",
    ":data:staff",
    ":data:department",
    ":data:department:test",
    ":data:local",
    ":data:remote",
    ":data:space",
    ":data:search",
    ":common:thirdparty",
    ":common:work",
    ":feature:edit_subscription",
    ":feature:feedback",
    ":feature:notice_detail",
    ":feature:notion",
    ":feature:onboarding",
    ":feature:splash",
    ":feature:main",
    ":common:designsystem",
    ":feature:edit_departments"
)
