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
    ":core:util",
    ":core:ui_util",
    ":data:domain",
    ":data:notice",
    ":core:preferences",
    ":data:push",
    ":data:user",
    ":data:staff",
    ":data:department",
    ":data:local",
    ":data:remote",
    ":core:thirdparty",
    ":core:work",
    ":feature:edit_subscription",
    ":feature:feedback",
    ":feature:my_notification",
    ":feature:notice_detail",
    ":feature:notion",
    ":feature:onboarding",
    ":feature:splash",
    ":feature:main",
    ":core:designsystem",
    ":feature:edit_departments"
)
