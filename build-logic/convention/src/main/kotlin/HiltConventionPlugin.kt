import com.friendfinapp.libs
import org.gradle.api.Plugin
import org.gradle.api.Project

class HiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("dagger.hilt.android.plugin")
                apply("com.google.devtools.ksp")
            }

            with(dependencies) {
                add("implementation", libs.findBundle("di").get())
                add("ksp", libs.findLibrary("hilt.compiler").get())
            }
        }
    }
}