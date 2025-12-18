plugins {
    alias(libs.plugins.android.core.library)
    alias(libs.plugins.android.hilt)
}

dependencies {
    implementation(libs.kotlin.coroutines)
    api(projects.core.model.entity)

    implementation(libs.timber)
    implementation(libs.gson)
}
android {
    namespace = "com.friend.domain"
}
