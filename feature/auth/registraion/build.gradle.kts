plugins {
    alias(libs.plugins.android.features)
}

android {
    namespace = "com.friend.registration"
}

dependencies {

    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.materialicon)
}