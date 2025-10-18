plugins {
    alias(libs.plugins.android.library.compose.convention.plugin)
}

android {
    namespace = "com.friend.designsystem"

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