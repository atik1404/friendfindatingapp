package com.friendfinapp

import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Project
import org.gradle.api.tasks.SourceTask

internal fun Project.configureKSP(
    extension: AndroidComponentsExtension<*, *, *>
) {
    extension.onVariants { variant ->
        val variantName = variant.name.replaceFirstChar { it.uppercaseChar() }

        val dataBindingTaskName = "dataBindingGenBaseClasses$variantName"
        val kspTaskName = "ksp${variantName}Kotlin"

        if (tasks.names.contains(dataBindingTaskName) && tasks.names.contains(kspTaskName)) {
            val dataBindingTaskProvider = tasks.named(dataBindingTaskName)
            val kspTaskProvider = tasks.named(kspTaskName)

            kspTaskProvider.configure {
                doFirst {
                    val outputDir = dataBindingTaskProvider.get().outputs.files.singleFile
                    if (this is SourceTask) {
                        this.source(outputDir)
                    }
                }
            }
        }
    }
}
