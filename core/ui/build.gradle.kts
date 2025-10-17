plugins {
    alias(libs.plugins.android.core.library)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.friend.ui"
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    api(projects.core.designSystem)
    implementation(libs.bundles.compose.core)
    implementation(libs.androidx.compose.materialicon)
    implementation(libs.androidx.compose.material.iconsExtended)
}