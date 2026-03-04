package com.friendfinapp

import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.Project

internal fun Project.configureKSP() {
    plugins.withId("com.google.devtools.ksp") {
        extensions.configure<KspExtension>("ksp") {
            arg("room.schemaLocation", "$projectDir/schemas")
            arg("room.incremental", "true")
            arg("room.expandProjection", "true")
        }
    }
}
