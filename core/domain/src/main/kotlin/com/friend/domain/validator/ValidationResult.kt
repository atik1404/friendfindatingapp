package com.friend.domain.validator

sealed class DataValidationResult {
    data object Success : DataValidationResult()
    data class Failure<T>(val ioErrorResult: T) : DataValidationResult()
}

sealed interface LoginIoResult {
    data object InvalidUsername : LoginIoResult
    data object InvalidPassword : LoginIoResult
}

sealed interface RegistrationIoResult {
    data object InvalidUsername : RegistrationIoResult
    data object InvalidName : RegistrationIoResult
    data object InvalidEmail : RegistrationIoResult
    data object InvalidPassword : RegistrationIoResult
    data object InvalidGender : RegistrationIoResult
    data object InvalidInterested : RegistrationIoResult
    data object InvalidBirthDate : RegistrationIoResult
    data object InvalidCity : RegistrationIoResult
    data object InvalidCountry : RegistrationIoResult
    data object InvalidState : RegistrationIoResult
    data object InvalidPostCode : RegistrationIoResult
}