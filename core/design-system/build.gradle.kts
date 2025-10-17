plugins {
    alias(libs.plugins.android.core.library)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.friend.designsystem"

    buildFeatures {
        viewBinding = true
    }

    sourceSets.getByName("main") {
        res.srcDir("src/main/res/vector-icon")
        res.srcDir("src/main/res/images")
    }
}

dependencies {
    implementation(libs.bundles.core.ui)
    implementation(libs.bundles.ui.helpers)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.compose.material3)
}