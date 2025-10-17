plugins {
    alias(libs.plugins.android.core.library)
    alias(libs.plugins.android.hilt)
}

dependencies {
    api(projects.core.sharedpref)
    implementation(libs.bundles.network)
    implementation(libs.timber)
}
android {
    namespace = "com.friend.di"
}
