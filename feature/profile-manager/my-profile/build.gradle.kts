plugins {
    alias(libs.plugins.android.features)
}
android {
    namespace = "com.friend.profile"
}

dependencies{
    implementation(libs.android.image.cropper)
}