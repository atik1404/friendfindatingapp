package com.friend.domain.validator

sealed class DataValidationResult {
    data object Success : DataValidationResult()
    data class Failure<T>(val ioErrorResult: T) : DataValidationResult()
}

sealed interface LoginIoResult {
    data object InvalidUsername : LoginIoResult
    data object InvalidPassword : LoginIoResult
}

sealed interface ForgotPasswordIoResult {
    data object InvalidEmail : ForgotPasswordIoResult
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

sealed interface ProfileCompletionIoResult {
    data object InvalidUsername : ProfileCompletionIoResult
    data object InvalidName : ProfileCompletionIoResult
    data object InvalidEmail : ProfileCompletionIoResult
    data object InvalidGender : ProfileCompletionIoResult
    data object InvalidInterested : ProfileCompletionIoResult
    data object InvalidBirthDate : ProfileCompletionIoResult
    data object InvalidCity : ProfileCompletionIoResult
    data object InvalidCountry : ProfileCompletionIoResult
    data object InvalidState : ProfileCompletionIoResult
    data object InvalidPostCode : ProfileCompletionIoResult
    data object InvalidHeight : ProfileCompletionIoResult
    data object InvalidWeight : ProfileCompletionIoResult
    data object InvalidEyes : ProfileCompletionIoResult
    data object InvalidHair : ProfileCompletionIoResult
    data object InvalidBodyType : ProfileCompletionIoResult
    data object InvalidLookingFor : ProfileCompletionIoResult
    data object InvalidSmoking : ProfileCompletionIoResult
    data object InvalidDrinking : ProfileCompletionIoResult
    data object InvalidAboutYou : ProfileCompletionIoResult
    data object InvalidTitle : ProfileCompletionIoResult
    data object InvalidWhatsUp : ProfileCompletionIoResult
    data object InvalidInterestedIn : ProfileCompletionIoResult
}

sealed interface PasswordChangeIoResult {
    data object InvalidOldPassword : PasswordChangeIoResult
    data object InvalidNewPassword : PasswordChangeIoResult
    data object InvalidConfirmPassword : PasswordChangeIoResult
    data object PasswordNotMatched : PasswordChangeIoResult
}