plugins {
    alias(libs.plugins.android.features)
}

android {
    namespace = "com.friend.registration"
}

dependencies{
    implementation(libs.play.services.recaptcha)
}