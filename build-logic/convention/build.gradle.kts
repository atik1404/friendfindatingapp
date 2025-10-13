import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.firebase.crashlytics.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.ksp.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidLibraryCompose") {
            id = "android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }

        register("androidApplicationCompose") {
            id = "android.compose.application"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }

        register("androidApplication"){
            id = "android.application"
            implementationClass = "AndroidApplicationPlugin"
        }

        register("androidLibrary"){
            id = "android.library"
            implementationClass = "AndroidLibraryPlugin"
        }

        register("androidFeatures"){
            id = "android.features"
            implementationClass = "AndroidFeaturePlugin"
        }

        register("androidFirebase"){
            id = "android.firebase"
            implementationClass = "AndroidFirebasePlugin"
        }

        register("androidHilt"){
            id = "android.hilt"
            implementationClass = "AndroidHiltPlugin"
        }

        register("jvmLibrary") {
            id = "jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }

        register("androidRoom") {
            id = "android.room"
            implementationClass = "AndroidRoomPlugin"
        }
    }
}
