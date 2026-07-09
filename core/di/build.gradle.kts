plugins {
    alias(libs.plugins.android.core.library)
    alias(libs.plugins.android.hilt)
}

dependencies {
    api(projects.core.sharedpref)
    implementation(libs.bundles.network)
    implementation(libs.timber)

    // Sentry OkHttp integration: HTTP breadcrumbs + client spans/tracing.
    implementation(libs.sentry.okhttp)
}

android {
    namespace = "com.friend.di"
}
