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

        testImplementation(test.junit)
        androidTestImplementation(test.extjunit)
        androidTestImplementation(test.espresso)
    }

}
