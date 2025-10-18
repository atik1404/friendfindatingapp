plugins {
    alias(libs.plugins.android.library.compose.convention.plugin)
}

android {
    namespace = "com.friend.ui"
}

dependencies {
    api(projects.core.designSystem)
    implementation(libs.bundles.compose.core)
    implementation(libs.androidx.compose.materialicon)
    implementation(libs.bundles.compose.tooling)
}