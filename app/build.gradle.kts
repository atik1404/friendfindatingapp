import com.friendfinapp.AppConfig

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
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.timber)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    // multi dex
    implementation(libs.androidx.multidex)


    // gson
    implementation(libs.gson)

    // spinkit
    implementation(libs.android.spinkit)


    // retrofit
    implementation(libs.retrofit)
    implementation(libs.dateced)

    // ok http
    implementation(libs.okhttp)


    // localisation
    implementation(libs.localisation)

    // permission
    implementation(libs.dexter)

    // billing
    implementation(libs.billing)

    // admob
    implementation(libs.play.services.ads)

    // google auth
    implementation(libs.play.services.auth)

    // Firebase BoM + libs
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)

    implementation(libs.photoview)

    implementation(libs.play.services.safetynet)
    implementation(libs.play.services.recaptcha)

    // image cropper
    implementation(libs.android.image.cropper)

    // exoplayer
    implementation(libs.exoplayer)
    implementation(libs.exoplayer.core)
    implementation(libs.exoplayer.dash)
    implementation(libs.exoplayer.hls)
    implementation(libs.exoplayer.smoothstreaming)
    implementation(libs.exoplayer.ui)

    implementation(libs.bundles.ui.helpers)

    // If you enabled desugaring above:
    // coreLibraryDesugaring(libs.desugar.jdk.libs)
}
