plugins {
    alias(libs.plugins.android.features)
}
android {
    namespace = "com.friend.videoplayer"
}

dependencies{
    implementation(libs.bundles.media.exoplayer)
}