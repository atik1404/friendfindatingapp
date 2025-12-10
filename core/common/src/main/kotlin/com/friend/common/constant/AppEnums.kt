package com.friend.common.constant

enum class Gender(val value: Int) {
    MALE(1),
    FEMALE(0);

    companion object {
        // 0 / 1 -> Gender
        fun fromValue(value: Int): Gender =
            when (value) {
                1 -> MALE
                0 -> FEMALE
                else -> throw IllegalArgumentException("Unknown gender value: $value")
            }

        // "Male" / "Female" -> 1 / 0
        fun toValue(name: String): Int =
            when (name.trim().lowercase()) {
                "male" -> MALE.value   // 1
                "female" -> FEMALE.value // 0
                else -> throw IllegalArgumentException("Unknown gender name: $name")
            }

        // "Male" / "Female" -> 1 / 0
        fun toEnum(name: String): Gender =
            when (name.trim().lowercase()) {
                "male" -> MALE
                "female" -> FEMALE
                else -> throw IllegalArgumentException("Unknown gender name: $name")
            }
    }
}