import com.android.build.api.dsl.LibraryExtension
import com.friendfinapp.configureKotlinAndroid
import com.friendfinapp.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class LibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
            }

            dependencies {
                add("testImplementation", libs.findLibrary("test.junit").get())
                add("androidTestImplementation", libs.findLibrary("test.extjunit").get())
                add("androidTestImplementation", libs.findLibrary("test.espresso").get())
            }
        }
    }
}