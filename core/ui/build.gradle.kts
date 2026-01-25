plugins {
    alias(libs.plugins.android.library.compose.convention.plugin)
}

android {
    namespace = "com.friend.ui"
}

dependencies {
    api(projects.core.designSystem)
    api(projects.core.common)
    implementation(libs.bundles.compose.core)
    implementation(libs.androidx.compose.materialicon)
    implementation(libs.bundles.compose.tooling)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.lottie.animation)
    implementation(libs.coil.video)
    implementation(libs.coil)
    implementation(libs.coil.okkhttp)

    implementation(libs.play.services.ads)

    implementation("com.google.accompanist:accompanist-permissions:0.34.0")
}