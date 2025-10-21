plugins {
    alias(libs.plugins.android.features)
}

android {
    namespace = "com.friend.login"
}

dependencies {
    implementation(libs.androidx.compose.constraintlayout)
}