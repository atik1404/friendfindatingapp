plugins {
    alias(libs.plugins.android.core.library)
    alias(libs.plugins.android.hilt)
}

android {
    namespace = "com.friend.data"
}

dependencies {
    implementation(libs.bundles.network)
    implementation(libs.bundles.rxjava3)
    implementation(libs.kotlin.coroutines)
    implementation(libs.dateced)

    implementation(projects.core.domain)
    implementation(projects.core.di)
    //implementation(projects.core.cache)
    implementation(projects.core.common)

    implementation(projects.core.model.entity)
    implementation(projects.core.model.apiresponse)
}