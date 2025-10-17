plugins {
    alias(libs.plugins.android.core.library)
    alias(libs.plugins.android.hilt)
}

dependencies {
    implementation(libs.bundles.core.ui)
    implementation(libs.kotlin.coroutines)
    api(projects.core.model.entity)
    implementation(libs.bundles.room)
}
android {
    namespace = "com.friend.domain"
}
