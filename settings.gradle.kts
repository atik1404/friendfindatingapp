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

include(":core:cache")
include(":core:data")
include (":core:di")
include(":core:domain")
include(":core:common")
include(":core:design-system")
include(":core:ui")
include(":core:sharedpref")

include(":core:model:apiresponse")
include(":core:model:entity")

include(":feature:auth:splash-screen")
include(":feature:auth:login")
include(":feature:auth:forgot-password")
include(":feature:auth:registraion")

include(":feature:home")

include(":feature:chat-message:chat-list")
include(":feature:chat-message:chat-room")

include(":feature:profile-manager:profile-overview")
include(":feature:profile-manager:profile")
include(":feature:profile-manager:profile-completion")