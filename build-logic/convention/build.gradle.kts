import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
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
            implementationClass = "LibraryComposeConventionPlugin"
        }

        register("androidApplicationCompose") {
            id = "android.compose.application"
            implementationClass = "ComposeConventionPlugin"
        }

        register("androidApplication"){
            id = "android.application"
            implementationClass = "ApplicationConventionPlugin"
        }

        register("androidLibrary"){
            id = "android.library"
            implementationClass = "LibraryConventionPlugin"
        }

        register("androidFeatures"){
            id = "android.features"
            implementationClass = "FeatureConventionPlugin"
        }

        register("androidFirebase"){
            id = "android.firebase"
            implementationClass = "FirebaseConventionPlugin"
        }

        register("androidHilt"){
            id = "android.hilt"
            implementationClass = "HiltConventionPlugin"
        }

        register("jvmLibrary") {
            id = "jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }

        register("androidRoom") {
            id = "android.room"
            implementationClass = "RoomConventionPlugin"
        }
    }
}
