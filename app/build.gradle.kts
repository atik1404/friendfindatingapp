plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.friendfinapp.dating"

    compileSdk = 36

    defaultConfig {
        applicationId = "com.friendfinapp.dating"
        minSdk = 24
        targetSdk = 36
        versionCode = 145
        versionName = "1.0.144"

        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file("friendfinjks")
            storePassword = "friendfinapp"
            keyAlias = "key0"
            keyPassword = "friendfinapp"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
        // coreLibraryDesugaringEnabled = true // if you use desugaring, uncomment and add dependency
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    lint {
        checkReleaseBuilds = false
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
