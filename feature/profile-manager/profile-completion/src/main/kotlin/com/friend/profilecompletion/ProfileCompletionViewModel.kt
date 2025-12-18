package com.friend.profilecompletion

import com.friend.common.base.BaseViewModel
import com.friend.common.constant.Gender
import com.friend.domain.apiusecase.profilemanager.FetchProfileApiUseCase
import com.friend.domain.apiusecase.profilemanager.PostProfileUpdateApiUseCase
import com.friend.domain.base.ApiResult
import com.friend.domain.validator.ProfileCompletionIoResult
import com.friend.sharedpref.SharedPrefHelper
import com.friend.sharedpref.SpKey
import com.friend.ui.common.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import com.friend.designsystem.R as Res

@HiltViewModel
class ProfileCompletionViewModel @Inject constructor(
    private val profileCompletionApiUseCase: PostProfileUpdateApiUseCase,
    private val fetchProfileApiUseCase: FetchProfileApiUseCase,
    private val sharedPrefHelper: SharedPrefHelper,
) : BaseViewModel() {
    val ioError get() = profileCompletionApiUseCase.ioError.receiveAsFlow()

    private val _formUiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _formUiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val action: (UiAction) -> Unit = {
        when (it) {
            UiAction.ResetState -> _formUiState.value = UiState()
            UiAction.SetDefaultData -> defaultValue()
            UiAction.FormSubmit -> performProfileCompletion()
            UiAction.FetchProfile -> fetchProfile()
            is UiAction.AboutYouChanged -> onAboutYouChange(it.value)
            is UiAction.BodyTypeChanged -> onBodyTypeChange(it.value)
            is UiAction.DrinkingChanged -> onDrinkingChange(it.value)
            is UiAction.EyesChanged -> onEyesChange(it.value)
            is UiAction.HairChanged -> onHairChange(it.value)
            is UiAction.HeightChanged -> onHeightChange(it.value)
            is UiAction.InterestsChanged -> onInterestedInChange(it.value)
            is UiAction.LookingForChanged -> onLookingForChange(it.value)
            is UiAction.SmokingChanged -> onSmokingChange(it.value)
            is UiAction.TitleChanged -> onTitleChange(it.value)
            is UiAction.WeightChanged -> onWeightChange(it.value)
            is UiAction.WhatsUpChanged -> onWhatsUpChange(it.value)
        }
    }

    init {
        bindIoError()
    }

    private fun defaultValue() {
        val interests = sharedPrefHelper.getString(SpKey.interests).split(":")
        updateForm { state ->
            state.copy(
                height = sharedPrefHelper.getString(SpKey.height),
                weight = sharedPrefHelper.getString(SpKey.weight),
                hair = sharedPrefHelper.getString(SpKey.hair),
                eyes = sharedPrefHelper.getString(SpKey.eyes),
                smoking = sharedPrefHelper.getString(SpKey.smoking),
                drinking = sharedPrefHelper.getString(SpKey.drinking),
                bodyType = sharedPrefHelper.getString(SpKey.bodyType),
                lookingFor = sharedPrefHelper.getString(SpKey.lookingFor),
                title = state.title.onChange(sharedPrefHelper.getString(SpKey.title)),
                aboutYou = state.aboutYou.onChange(sharedPrefHelper.getString(SpKey.aboutYou)),
                whatsUp = state.whatsUp.onChange(sharedPrefHelper.getString(SpKey.whatsUp)),
                interests = interests,
            )
        }
    }

    private fun performProfileCompletion() {
        execute {
            val current = _formUiState.value
            val params = PostProfileUpdateApiUseCase.Params(
                username = sharedPrefHelper.getString(SpKey.userName),
                name = sharedPrefHelper.getString(SpKey.fullName),
                gender = Gender.toValue(sharedPrefHelper.getString(SpKey.gender)),
                interestedIn = Gender.toValue(sharedPrefHelper.getString(SpKey.interestedIn)),
                birthdate = sharedPrefHelper.getString(SpKey.dateOfBirth),
                email = sharedPrefHelper.getString(SpKey.email),
                country = sharedPrefHelper.getString(SpKey.country),
                state = sharedPrefHelper.getString(SpKey.state),
                city = sharedPrefHelper.getString(SpKey.city),
                zipCode = sharedPrefHelper.getString(SpKey.zipCode),
                height = current.height,
                weight = current.weight,
                eyes = current.eyes,
                hair = current.hair,
                smoking = current.smoking,
                drinking = current.drinking,
                bodyType = current.bodyType,
                lookingFor = current.lookingFor,
                aboutYou = current.aboutYou.value,
                title = current.title.value,
                whatsUp = current.whatsUp.value,
                interests = current.interests.joinToString(":")
            )

            profileCompletionApiUseCase.execute(params).collect { result ->
                when (result) {
                    is ApiResult.Error -> setToastMessage(UiText.Dynamic(result.message))
                    is ApiResult.Loading -> setLoading(result.loading)
                    is ApiResult.Success -> {
                        setToastMessage(UiText.Dynamic(result.data))
                        fetchProfile()
                    }
                }
            }
        }
    }

    private fun fetchProfile() {
        execute {
            fetchProfileApiUseCase.execute().collect { result ->
                when (result) {
                    is ApiResult.Error -> updateForm { state ->
                        state.copy(isFailedToFetchProfile = true, apiErrorMessage = result.message)
                    }

                    is ApiResult.Loading -> updateForm { state ->
                        state.copy(
                            isLoading = result.loading,
                            isFailedToFetchProfile = false,
                            apiErrorMessage = ""
                        )
                    }

                    is ApiResult.Success -> _uiEvent.send(UiEvent.NavigateToHome)
                }
            }
        }
    }

    private fun onHeightChange(value: String) {
        updateForm { state ->
            state.copy(height = value)
        }
    }

    private fun onWeightChange(value: String) {
        updateForm { state ->
            state.copy(weight = value)
        }
    }

    private fun onEyesChange(value: String) {
        updateForm { state ->
            state.copy(eyes = value)
        }
    }

    private fun onHairChange(value: String) {
        updateForm { state ->
            state.copy(hair = value)
        }
    }

    private fun onSmokingChange(value: String) {
        updateForm { state ->
            state.copy(smoking = value)
        }
    }

    private fun onDrinkingChange(value: String) {
        updateForm { state ->
            state.copy(drinking = value)
        }
    }

    private fun onBodyTypeChange(value: String) {
        updateForm { state ->
            state.copy(bodyType = value)
        }
    }

    private fun onLookingForChange(value: String) {
        updateForm { state ->
            state.copy(lookingFor = value)
        }
    }

    private fun onTitleChange(value: String) {
        updateForm { state ->
            state.copy(title = state.title.onChange(value))
        }
    }

    private fun onAboutYouChange(value: String) {
        updateForm { state ->
            state.copy(aboutYou = state.aboutYou.onChange(value))
        }
    }

    private fun onWhatsUpChange(value: String) {
        updateForm { state ->
            state.copy(whatsUp = state.whatsUp.onChange(value))
        }
    }

    private fun onInterestedInChange(value: Set<String>) {
        updateForm { state ->
            state.copy(interests = value.toList())
        }
    }

    private fun setLoading(value: Boolean) {
        updateForm { state ->
            state.copy(isSubmitting = value)
        }
    }

    /** Update only the form part of the UiState in a single place to reduce repetition. */
    private inline fun updateForm(transform: (UiState) -> UiState) {
        _formUiState.update { state -> transform(state) }
    }

    private fun bindIoError() {
        execute {
            ioError.collect { error ->
                when (error) {
                    ProfileCompletionIoResult.InvalidAboutYou -> updateForm {
                        it.copy(
                            aboutYou = it.aboutYou.copy(
                                isValid = false
                            )
                        )
                    }

                    ProfileCompletionIoResult.InvalidTitle -> updateForm {
                        it.copy(
                            title = it.title.copy(
                                isValid = false
                            )
                        )
                    }

                    ProfileCompletionIoResult.InvalidWhatsUp -> updateForm {
                        it.copy(
                            whatsUp = it.whatsUp.copy(
                                isValid = false
                            )
                        )
                    }

                    ProfileCompletionIoResult.InvalidBodyType -> setToastMessage(
                        UiText.StringRes(
                            Res.string.error_invalid_body_type
                        )
                    )

                    ProfileCompletionIoResult.InvalidDrinking -> setToastMessage(
                        UiText.StringRes(
                            Res.string.error_invalid_drinking
                        )
                    )

                    ProfileCompletionIoResult.InvalidEyes -> setToastMessage(UiText.StringRes(Res.string.error_invalid_eyes))
                    ProfileCompletionIoResult.InvalidHair -> setToastMessage(UiText.StringRes(Res.string.error_invalid_hair))
                    ProfileCompletionIoResult.InvalidHeight -> setToastMessage(UiText.StringRes(Res.string.error_invalid_height))
                    ProfileCompletionIoResult.InvalidInterestedIn -> setToastMessage(
                        UiText.StringRes(
                            Res.string.error_invalid_interest_in
                        )
                    )

                    ProfileCompletionIoResult.InvalidLookingFor -> setToastMessage(
                        UiText.StringRes(
                            Res.string.error_invalid_looking_for
                        )
                    )

                    ProfileCompletionIoResult.InvalidSmoking -> setToastMessage(UiText.StringRes(Res.string.error_invalid_smoking))
                    ProfileCompletionIoResult.InvalidWeight -> setToastMessage(UiText.StringRes(Res.string.error_invalid_weight))
                }
            }
        }
    }

    private fun setToastMessage(message: UiText) {
        execute {
            _uiEvent.send(UiEvent.ShowToastMessage(message))
        }
    }
}