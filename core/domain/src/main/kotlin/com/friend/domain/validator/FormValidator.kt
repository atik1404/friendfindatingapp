package com.friend.domain.validator

import android.util.Patterns

fun String.isUsernameValid(): Boolean {
    return this.trim().isNotBlank() && this.trim().length >= 3
}

fun String.isNameValid(): Boolean {
    return this.trim().isNotBlank() && this.length >= 3
}

fun String.isPasswordValid(): Boolean {
    return this.isNotBlank() && this.length >= 10
}

fun String.isPasswordMatched(password: String): Boolean {
    return this == password
}

fun String.isEmailValid(): Boolean {
    return this.isNotBlank() &&
            Patterns.EMAIL_ADDRESS.matcher(this.trim()).matches()
}