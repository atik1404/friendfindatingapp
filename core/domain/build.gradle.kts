plugins {
    alias(libs.plugins.android.core.library)
    alias(libs.plugins.android.hilt)
}

dependencies {
    implementation(libs.kotlin.coroutines)
    api(projects.core.model.entity)
}
android {
    namespace = "com.friend.domain"
}
