
import com.friendfinapp.AppConfig
import com.friendfinapp.libs
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

class AndroidFeaturePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("android.library")
                apply("android.hilt")
                apply("android.navigation")
            }
            extensions.configure<LibraryExtension> {
                defaultConfig {
                    testInstrumentationRunner = AppConfig.testInstrumentationRunner
                }
                buildFeatures {
                    viewBinding = true
                }
            }

            dependencies {
//                add("implementation", project(":core:di"))
//                add("implementation", project(":core:domain"))
//                add("implementation", project(":core:model:entity"))
//                add("implementation", project(":library:sharedpref"))
//
//                add("implementation", project(":core:common"))
//                add("implementation", project(":core:design-system"))
//                add("implementation", project(":core:ui"))
//
                add("implementation", libs.findBundle("core.ui").get())
                add("implementation", libs.findBundle("lifecycle").get())

                add("implementation", libs.findBundle("compose.core").get())
                add("implementation", libs.findBundle("compose.tooling").get())

                add("implementation", libs.findLibrary("view.state.layout").get())
                add("implementation", libs.findLibrary("dateced").get())
                add("implementation", libs.findLibrary("timber").get())
                add("implementation", libs.findLibrary("gson").get())

                add("implementation", libs.findLibrary("kotlin.coroutines").get())

                add("testImplementation", libs.findLibrary("test.junit").get())
                add("androidTestImplementation", libs.findLibrary("test.extjunit").get())
                add( "androidTestImplementation",libs.findLibrary("test.espresso").get())
            }
        }
    }
}
