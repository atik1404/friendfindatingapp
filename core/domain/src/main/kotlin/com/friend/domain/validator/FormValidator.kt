package com.friend.domain.validator

import android.util.Patterns

fun String.isUsernameValid(): Boolean {
    return this.isNotBlank() && this.length >= 3
}

fun String.isNameValid(): Boolean {
    return this.isNotBlank() && this.length >= 3
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

fun userNameValidator(value: String): Boolean {
    return value.isNotBlank() && value.length >= 3
}

fun emailValidator(value: String): Boolean {
    return value.isNotBlank() &&
            Patterns.EMAIL_ADDRESS.matcher(value.trim()).matches()
}

fun nameValidator(value: String): Boolean {
    return value.isNotBlank() && value.length >= 3
}

fun passwordValidator(value: String): Boolean {
    return value.isNotBlank() && value.length >= 10
}

fun textEmptyValidator(value: String): Boolean {
    return value.isNotBlank()
}