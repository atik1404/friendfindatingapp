package com.friend.otherprofile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.friend.common.dateparser.DateTimePatterns
import com.friend.common.dateparser.DateTimeUtils
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPadding
import com.friend.entity.profilemanager.OtherProfileApiEntity
import com.friend.entity.profilemanager.ProfileApiEntity
import com.friend.otherprofile.components.AppToolbarSection
import com.friend.otherprofile.components.LabeledValue
import com.friend.otherprofile.components.ProfileHeaderUi
import com.friend.ui.common.ErrorUi
import com.friend.ui.common.LoadingUi
import com.friend.ui.components.AppScaffold
import com.friend.ui.preview.LightPreview
import com.friend.designsystem.R as Res

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtherProfileScreen(
    uiState: UiState,
    username: String,
    isBlocked: Boolean,
    uiAction: (UiAction) -> Unit,
    onBackButtonClicked: () -> Unit,
    navigateToMessageRoom: () -> Unit,
    navigateToReportAbuse: () -> Unit,
) {
    AppScaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            AppToolbarSection(
                isBlocked = isBlocked,
                onBackButtonClicked = onBackButtonClicked,
                navigateToReportAbuse = navigateToReportAbuse,
                onBlockMenuClicked = {
                    uiAction.invoke(UiAction.PerformBlockUnblock(username))
                },
                onUnblockMenuClicked = {
                    uiAction.invoke(UiAction.PerformBlockUnblock(username))
                }
            )
        }
    ) { padding ->
        when (uiState) {
            is UiState.ApiError -> ErrorUi(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .consumeWindowInsets(padding),
                message = uiState.message
            ) {
                uiAction.invoke(UiAction.PerformBlockUnblock(username))
            }

            UiState.Loading -> LoadingUi(
                modifier = Modifier
                    .padding(padding)
                    .consumeWindowInsets(padding)
            )

            is UiState.ShowProfileData -> {
                ProfileUi(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .consumeWindowInsets(padding)
                        .navigationBarsPadding()         // keep content above system nav bar
                        .imePadding()                    // lift content when keyboard shows
                        .verticalScroll(rememberScrollState())
                        .appPadding(SpacingToken.small),
                    data = uiState.data,
                    onSendMsgClicked = navigateToMessageRoom
                )
            }
        }
    }
}

@Composable
private fun ProfileUi(
    modifier: Modifier,
    data: OtherProfileApiEntity,
    onSendMsgClicked: () -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        ProfileHeaderUi(
            modifier = Modifier,
            isBlocked = data.isBlocked,
            fullName = data.profile.fullName,
            email = data.profile.email,
            profilePicture = data.profile.profilePicture,
            onSendMsgClicked = onSendMsgClicked
        )

        Spacer(modifier = Modifier.height(SpacingToken.medium))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LabeledValue(
                title = stringResource(Res.string.label_date_of_birth),
                value = DateTimeUtils.parseToPattern(
                    data.profile.birthdate,
                    DateTimePatterns.MDY_TEXT_COMMA
                ),
                modifier = Modifier.weight(1f)
            )

            LabeledValue(
                title = stringResource(Res.string.label_gender),
                value = data.profile.gender,
                modifier = Modifier.weight(1f),
                alignment = Alignment.End
            )
        }

        Spacer(modifier = Modifier.height(SpacingToken.medium))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LabeledValue(
                title = stringResource(Res.string.label_language),
                value = "English",
                modifier = Modifier.weight(1f)
            )

            LabeledValue(
                title = stringResource(Res.string.label_interested_in),
                value = data.profile.interestedIn,
                modifier = Modifier.weight(1f),
                alignment = Alignment.End
            )
        }

        Spacer(modifier = Modifier.height(SpacingToken.medium))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LabeledValue(
                title = stringResource(Res.string.label_height),
                value = data.profile.height,
                modifier = Modifier.weight(1f)
            )

            LabeledValue(
                title = stringResource(Res.string.label_weight),
                value = data.profile.weight,
                modifier = Modifier.weight(1f),
                alignment = Alignment.End
            )
        }

        Spacer(modifier = Modifier.height(SpacingToken.medium))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LabeledValue(
                title = stringResource(Res.string.label_eyes),
                value = "Black",
                modifier = Modifier.weight(1f)
            )

            LabeledValue(
                title = stringResource(Res.string.label_hair),
                value = data.profile.hair,
                modifier = Modifier.weight(1f),
                alignment = Alignment.End
            )
        }

        Spacer(modifier = Modifier.height(SpacingToken.medium))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LabeledValue(
                title = stringResource(Res.string.label_smoking),
                value = data.profile.smoking,
                modifier = Modifier.weight(1f)
            )

            LabeledValue(
                title = stringResource(Res.string.label_drinking),
                value = data.profile.drinking,
                modifier = Modifier.weight(1f),
                alignment = Alignment.End
            )
        }

        Spacer(modifier = Modifier.height(SpacingToken.medium))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LabeledValue(
                title = stringResource(Res.string.label_body_type),
                value = data.profile.bodyType,
                modifier = Modifier.weight(1f)
            )

            LabeledValue(
                title = stringResource(Res.string.label_country),
                value = data.profile.country,
                modifier = Modifier.weight(1f),
                alignment = Alignment.End
            )
        }

        Spacer(modifier = Modifier.height(SpacingToken.medium))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LabeledValue(
                title = stringResource(Res.string.label_city),
                value = data.profile.city,
                modifier = Modifier.weight(1f)
            )

            LabeledValue(
                title = stringResource(Res.string.label_state),
                value = data.profile.state,
                modifier = Modifier.weight(1f),
                alignment = Alignment.End
            )
        }

        Spacer(modifier = Modifier.height(SpacingToken.medium))

        LabeledValue(
            title = stringResource(Res.string.label_about_yourself),
            value = data.profile.aboutYou,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(SpacingToken.medium))

        LabeledValue(
            title = stringResource(Res.string.label_interest),
            value = data.profile.interests,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(SpacingToken.medium))

        LabeledValue(
            title = stringResource(Res.string.label_whats_up),
            value = data.profile.whatsUp,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 5
        )
    }
}

@Composable
@LightPreview
private fun ScreenPreview() {
    OtherProfileScreen(
        //uiState = UiState.ApiError("Error message"),
        uiState = UiState.ShowProfileData(
            OtherProfileApiEntity(
                isBlocked = false,
                profile = ProfileApiEntity(
                    userName = "Tom Cruise",
                    fullName = "Tom Cruise",
                    gender = "Male",
                    birthdate = "September 29, 1995",
                    email = "tom@gmail.com",
                    interestedIn = "Female",
                    country = "Bangladesh",
                    state = "Dhaka",
                    city = "Dhaka",
                    zipCode = "",
                    profilePicture = "https://images.mubicdn.net/images/cast_member/2184/cache-2992-1547409411/image-w856.jpg",
                    bodyType = "",
                    drinking = "",
                    eyes = "",
                    hair = "",
                    height = "",
                    interests = "",
                    lookingFor = "",
                    smoking = "",
                    aboutYou = "",
                    title = "",
                    weight = "",
                    whatsUp = "",
                    isProfileComplete = false
                )
            )
        ),
        onBackButtonClicked = {},
        navigateToMessageRoom = {},
        navigateToReportAbuse = {},
        username = "",
        isBlocked = false,
        uiAction = {}
    )
}