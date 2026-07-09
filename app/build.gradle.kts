import com.friendfinapp.AppConfig

/**
 * Resolves a Sentry setting from (in order) an environment variable, then a
 * Gradle project property (e.g. in ~/.gradle/gradle.properties or -P flag),
 * then the supplied default. This keeps DSN/secrets out of source control and
 * lets Development / Staging / Production builds differ purely by configuration.
 */
fun sentryConfig(name: String, default: String): String =
    System.getenv(name) ?: (project.findProperty(name) as String?) ?: default

/** Short Git commit SHA for release/commit tracking; "unknown" if unavailable. */
val gitCommitSha: String = runCatching {
    ProcessBuilder("git", "rev-parse", "--short", "HEAD")
        .directory(rootDir)
        .start()
        .inputStream.bufferedReader().readText().trim()
}.getOrDefault("unknown").ifBlank { "unknown" }

plugins {
    alias(libs.plugins.android.core.application)
    alias(libs.plugins.android.compose.convention.plugin)
    alias(libs.plugins.android.hilt)
    alias(libs.plugins.android.firebase)
    alias(libs.plugins.firebase.perf.plugin)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = AppConfig.applicationId
    compileSdk = AppConfig.compileSdkVersion

    defaultConfig {
        applicationId = AppConfig.applicationId
        minSdk = AppConfig.minimumSdkVersion
        targetSdk = AppConfig.targetSdkVersion
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName
        testInstrumentationRunner = AppConfig.testInstrumentationRunner

        multiDexEnabled = true

        // ----- Sentry: values shared by all build types -------------------
        // Release convention: name@version+build (Sentry-recommended format).
        buildConfigField(
            "String",
            "SENTRY_RELEASE",
            "\"${AppConfig.applicationId}@${AppConfig.versionName}+${AppConfig.versionCode}\"",
        )
        buildConfigField("String", "GIT_COMMIT", "\"$gitCommitSha\"")
        // DSN comes from env/property so it is never committed. Empty => Sentry
        // stays disabled (safe no-op) for local builds without a DSN.
        buildConfigField("String", "SENTRY_DSN", "\"${sentryConfig("SENTRY_DSN", "")}\"")
    }

    signingConfigs {
        create("release") {
            storeFile = file("app_credential/friendfinjks")
            storePassword = "friendfinapp"
            keyAlias = "key0"
            keyPassword = "friendfinapp"
        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("debug")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            // Development: full sampling for local debugging, PII allowed on
            // the developer's own device.
            buildConfigField(
                "String", "SENTRY_ENVIRONMENT",
                "\"${sentryConfig("SENTRY_ENVIRONMENT", "development")}\"",
            )
            buildConfigField(
                "double", "SENTRY_TRACES_SAMPLE_RATE",
                sentryConfig("SENTRY_TRACES_SAMPLE_RATE", "1.0"),
            )
            buildConfigField(
                "double", "SENTRY_PROFILE_SAMPLE_RATE",
                sentryConfig("SENTRY_PROFILE_SAMPLE_RATE", "1.0"),
            )
            buildConfigField(
                "double", "SENTRY_REPLAY_SESSION_SAMPLE_RATE",
                sentryConfig("SENTRY_REPLAY_SESSION_SAMPLE_RATE", "1.0"),
            )
            buildConfigField(
                "double", "SENTRY_REPLAY_ON_ERROR_SAMPLE_RATE",
                sentryConfig("SENTRY_REPLAY_ON_ERROR_SAMPLE_RATE", "1.0"),
            )
            buildConfigField(
                "boolean", "SENTRY_SEND_PII",
                sentryConfig("SENTRY_SEND_PII", "true"),
            )
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            // Production defaults: reduced sampling to minimize overhead and
            // PII disabled. Override per environment (e.g. Staging) via
            // SENTRY_ENVIRONMENT / SENTRY_*_SAMPLE_RATE env vars in CI.
            buildConfigField(
                "String", "SENTRY_ENVIRONMENT",
                "\"${sentryConfig("SENTRY_ENVIRONMENT", "production")}\"",
            )
            buildConfigField(
                "double", "SENTRY_TRACES_SAMPLE_RATE",
                sentryConfig("SENTRY_TRACES_SAMPLE_RATE", "0.2"),
            )
            buildConfigField(
                "double", "SENTRY_PROFILE_SAMPLE_RATE",
                sentryConfig("SENTRY_PROFILE_SAMPLE_RATE", "0.2"),
            )
            buildConfigField(
                "double", "SENTRY_REPLAY_SESSION_SAMPLE_RATE",
                sentryConfig("SENTRY_REPLAY_SESSION_SAMPLE_RATE", "0.1"),
            )
            buildConfigField(
                "double", "SENTRY_REPLAY_ON_ERROR_SAMPLE_RATE",
                sentryConfig("SENTRY_REPLAY_ON_ERROR_SAMPLE_RATE", "1.0"),
            )
            buildConfigField(
                "boolean", "SENTRY_SEND_PII",
                sentryConfig("SENTRY_SEND_PII", "false"),
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    with(projects.core){
        implementation(di)
        implementation(data)
        //implementation(cache)
        implementation(domain)
        implementation(model.entity)
        implementation(model.apiresponse)
        implementation(sharedpref)

        implementation(common)
        implementation(designSystem)
        implementation(ui)
    }

    with(projects.feature){
        implementation(auth.splashScreen)
        implementation(auth.login)
        implementation(auth.registraion)
        implementation(auth.forgotPassword)
        implementation(home)

        implementation(chatMessage.chatList)
        implementation(chatMessage.chatRoom)
        implementation(chatMessage.forwardMessage)

        implementation(profileManager.myProfile)
        implementation(profileManager.otherProfile)
        implementation(profileManager.profileOverview)
        implementation(profileManager.profileCompletion)
        implementation(profileManager.personalSetting)
        implementation(profileManager.vipMembership)
        implementation(profileManager.changePassword)

        implementation(common.privacyPolicy)
        implementation(common.reportAbuse)
        implementation(common.videoPlayer)
    }

    with(libs){
        implementation(androidx.core.ktx)
        implementation(androidx.appcompat)
        implementation(androidx.activity.ktx)
        implementation(bundles.lifecycle)

        implementation(platform(libs.androidx.compose.bom))
        implementation(bundles.compose.core)
        implementation(bundles.compose.navigation)
        implementation(bundles.androidx.navigation.dependencies)
        implementation(libs.kotlinx.serialization.core)

        debugImplementation(leakcanary)
        implementation(timber)

        implementation(platform(firebase.bom))
        implementation(bundles.firebase)

        implementation(libs.play.services.ads)

        implementation(libs.android.image.cropper)
        implementation(libs.billing)

        implementation(libs.crarity.compose)

        // Sentry: crashes, tracing, profiling, session replay, logs.
        implementation(libs.sentry.android)

        testImplementation(test.junit)
        androidTestImplementation(test.extjunit)
        androidTestImplementation(test.espresso)
    }

}