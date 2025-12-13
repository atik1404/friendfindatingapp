package com.friend.common.constant

enum class Gender(val value: Int) {
    MALE(1),
    FEMALE(2);

    companion object {
        // 1 / 2 -> Gender
        fun fromValue(value: Int): Gender =
            when (value) {
                1 -> MALE
                2 -> FEMALE
                else -> throw IllegalArgumentException("Unknown gender value: $value")
            }

        // "Male" / "Female" -> 1 / 2
        fun toValue(name: String): Int =
            when (name.trim().lowercase()) {
                "male" -> MALE.value   // 1
                "female" -> FEMALE.value // 2
                else -> throw IllegalArgumentException("Unknown gender name: $name")
            }

        // "Male" / "Female" -> 1 / 2
        fun toEnum(name: String): Gender =
            when (name.trim().lowercase()) {
                "male" -> MALE
                "female" -> FEMALE
                else -> throw IllegalArgumentException("Unknown gender name: $name")
            }
    }
}

enum class PersonalMenu {
    PERSONAL_SETTING,
    PRIVACY_POLICY,
    SHARE_APP,
    RATE_APP,
    CHANGE_PASSWORD,
    VIP_MEMBERSHIP,
    CONTACT_US,
    LOGOUT,
}
