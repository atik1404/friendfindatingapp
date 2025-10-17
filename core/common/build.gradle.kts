plugins {
    alias(libs.plugins.android.core.library)
}

android {
    namespace = "com.friend.common"
}

dependencies {
    //implementation(projects.library.sharedpref)
    //implementation(projects.core.designSystem)
    //implementation(projects.core.model.entity)

    implementation(libs.timber)
    implementation(libs.bundles.core.ui)
    implementation(libs.bundles.lifecycle)
    implementation(libs.bundles.rxjava3)
    implementation(libs.picasso)
    implementation(libs.cirlce.imageview)
    implementation(libs.dateced)
    implementation(libs.date.picker)
}