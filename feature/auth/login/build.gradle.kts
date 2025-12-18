plugins {
    alias(libs.plugins.android.features)
}

android {
    namespace = "com.friend.login"
}

dependencies{
    implementation(libs.bundles.google.login)
}