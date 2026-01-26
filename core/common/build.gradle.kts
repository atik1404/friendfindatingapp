plugins {
    alias(libs.plugins.android.core.library)
}

android {
    namespace = "com.friend.common"
}

dependencies {
    implementation(projects.core.di)

    implementation(libs.timber)
    implementation(libs.bundles.core.ui)
    implementation(libs.bundles.lifecycle)
    implementation(libs.bundles.rxjava3)
    implementation(libs.picasso)
    implementation(libs.cirlce.imageview)
    implementation(libs.dateced)
    implementation(libs.date.picker)

    implementation(libs.bundles.google.login)
    implementation(libs.billing)
}