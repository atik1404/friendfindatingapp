@file:Suppress("UnstableApiUsage")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://oss.jfrog.org/libs-snapshot")
        maven("https://www.jitpack.io")
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://www.jitpack.io")
        maven("https://oss.jfrog.org/libs-snapshot")
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "friendfin"
include(":app")

include(":core:data")
include(":core:di")
include(":core:domain")
include(":core:common")
include(":core:sharedpref")

include(":core:design-system")
include(":core:ui")

include(":core:model:apiresponse")
include(":core:model:entity")

include(":feature:auth:splash-screen")
include(":feature:auth:login")
include(":feature:auth:forgot-password")
include(":feature:auth:registraion")

include(":feature:home")

include(":feature:chat-message:chat-list")
include(":feature:chat-message:chat-room")
include(":feature:chat-message:forward-message")


include(":feature:profile-manager:profile-overview")
include(":feature:profile-manager:my-profile")
include(":feature:profile-manager:other-profile")
include(":feature:profile-manager:profile-completion")
include(":feature:profile-manager:personal-setting")
include(":feature:profile-manager:change-password")
include(":feature:profile-manager:vip-membership")

include(":feature:common:privacy-policy")
include(":feature:common:report-abuse")
include(":feature:common:video-player")
