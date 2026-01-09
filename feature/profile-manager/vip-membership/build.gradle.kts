plugins {
    alias(libs.plugins.android.features)
}

android {
    namespace = "com.friend.membership"
}

dependencies{
    implementation(libs.billing)
}