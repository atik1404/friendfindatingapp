plugins {
    alias(libs.plugins.android.features)
}

android {
    namespace = "com.friend.chatroom"
}

dependencies{
    implementation(libs.bundles.media.exoplayer)
    implementation(libs.android.image.cropper)
}