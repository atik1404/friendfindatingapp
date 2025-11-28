package com.friend.ui.validator

import android.util.Patterns

fun String.isValidEmail(): Boolean {
    return this.isNotBlank() &&
            Patterns.EMAIL_ADDRESS.matcher(this.trim()).matches()
}

fun String.isUsernameValid(): Boolean {
    return this.isNotBlank() && this.length >= 3
}

fun String.isPasswordValid(): Boolean {
    return this.isNotBlank() && this.length >= 10
}

fun String.isPasswordMatched(password: String): Boolean {
    return this == password
}