package com.friend.domain.base

enum class FormStatus {
    Pure,        // untouched
    Valid,       // all fields valid
    Invalid,     // some field invalid
    Submitting,  // calling API
    Success,     // API success
    Failure      // API failed
}

data class TextInput(
    val value: String = "",
    val isValid: Boolean = true,
    val isDirty: Boolean = false,
) {
    val hasError: Boolean get() = !isValid && isDirty

    fun onChange(newValue: String): TextInput {
        return copy(value = newValue, isValid = true)
    }

    fun onChange(
        newValue: String,
        validator: (String) -> Boolean
    ): TextInput {
        val valid = if (!isDirty) {
            // first time typing: don't show error yet
            true
        } else {
            validator(newValue)
        }

        return copy(
            value = newValue,
            isValid = valid,
            isDirty = true
        )
    }

    fun validate(
        validator: (String) -> Boolean
    ): TextInput {
        return copy(
            isValid = validator(value),
            isDirty = true
        )
    }
}


data class BoolInput(
    val value: Boolean = false,
    val error: String? = null,
    val isDirty: Boolean = false,
) {
    val isValid: Boolean get() = error == null

    fun onChange(
        newValue: Boolean,
        validator: (Boolean) -> String?
    ): BoolInput {
        return copy(
            value = newValue,
            error = if (!isDirty) null else validator(newValue),
            isDirty = true
        )
    }

    fun validate(
        validator: (Boolean) -> String?
    ): BoolInput {
        return copy(
            error = validator(value),
            isDirty = true
        )
    }
}
