import com.friendfinapp.AppConfig
import com.friendfinapp.configureKotlinAndroid
import com.friendfinapp.libs
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class LibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = AppConfig.targetSdkVersion
            }

            dependencies {
                add("testImplementation", libs.findLibrary("test.junit").get())
                add("androidTestImplementation", libs.findLibrary("test.extjunit").get())
                add("androidTestImplementation", libs.findLibrary("test.espresso").get())
            }
        }
    }
}