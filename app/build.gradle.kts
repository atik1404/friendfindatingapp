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
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
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
        implementation(domain)
        implementation(model.entity)
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
        implementation(auth.profileCompletion)
        implementation(home)
        implementation(chatMessage.chatList)
    }

    with(libs){
        implementation(androidx.core.ktx)
        implementation(androidx.appcompat)

        implementation(bundles.compose.core)
        implementation(bundles.compose.navigation)
        implementation(bundles.androidx.navigation.dependencies)
        implementation(libs.kotlinx.serialization.core)

        debugImplementation(leakcanary)
        implementation(timber)

        implementation(platform(firebase.bom))
        implementation(firebase.analytics)
        implementation(firebase.crashlytics)

        testImplementation(test.junit)
        androidTestImplementation(test.extjunit)
        androidTestImplementation(test.espresso)
    }

}
