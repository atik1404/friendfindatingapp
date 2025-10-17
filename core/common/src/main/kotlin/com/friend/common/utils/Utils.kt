package com.friend.common.utils

object Utils {
    fun getBuildTypeName(buildType: String) = when (buildType) {
        "debug", "dev" -> "Dev"
        "qa" -> "QA"
        "release" -> "Live"
        else -> "Unknown"
    }
}