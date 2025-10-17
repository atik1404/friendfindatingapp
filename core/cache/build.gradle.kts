plugins {
    alias(libs.plugins.android.core.library)
    alias(libs.plugins.android.hilt)
    alias(libs.plugins.android.room)
}
android {
    namespace = "com.friend.cache"
}

dependencies {
    implementation(projects.core.domain)
}