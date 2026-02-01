package com.friend.otherprofile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPadding
import com.friend.designsystem.theme.textColors
import com.friend.entity.profilemanager.OtherProfileApiEntity
import com.friend.entity.profilemanager.ProfileApiEntity
import com.friend.otherprofile.UiAction.PerformBlockUnblock
import com.friend.otherprofile.components.AppToolbarSection
import com.friend.otherprofile.components.ProfileHeaderSection
import com.friend.otherprofile.components.ProfileInformationSection
import com.friend.ui.common.ErrorUi
import com.friend.ui.common.LoadingUi
import com.friend.ui.components.AppScaffold
import com.friend.ui.components.AppText
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
    navigateToMessageRoom: (String, String, String) -> Unit,
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
                    uiAction.invoke(PerformBlockUnblock(username))
                },
                onUnblockMenuClicked = {
                    uiAction.invoke(PerformBlockUnblock(username))
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
                uiAction.invoke(PerformBlockUnblock(username))
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
                        .imePadding()                    // lift content when keyboard shows
                        .verticalScroll(rememberScrollState())
                        .appPadding(SpacingToken.small),
                    data = uiState.data,
                    onSendMsgClicked = {
                        navigateToMessageRoom.invoke(
                            uiState.data.profile.userName,
                            uiState.data.profile.fullName,
                            uiState.data.profile.profilePicture
                        )
                    }
                )
            }

            UiState.PrivateProfile -> ErrorUi(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .consumeWindowInsets(padding),
                message = stringResource(Res.string.msg_private_profile),
                title = stringResource(Res.string.title_private_profile),
                isRetryEnable = false,
            )
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
        ProfileHeaderSection(
            modifier = Modifier,
            isBlocked = data.isBlocked,
            fullName = data.profile.fullName,
            email = data.profile.email,
            profilePicture = data.profile.profilePicture,
            onSendMsgClicked = onSendMsgClicked
        )

        if (data.isBlocked) {
            Spacer(modifier = Modifier.height(SpacingToken.medium))

            AppText(
                text = stringResource(Res.string.error_user_blocked),
                textColor = MaterialTheme.textColors.error
            )
        }

        Spacer(modifier = Modifier.height(SpacingToken.medium))

        ProfileInformationSection(data.profile)
    }
}

@Composable
@LightPreview
private fun ScreenPreview() {
    OtherProfileScreen(
        //uiState = UiState.PrivateProfile,
        //uiState = UiState.ApiError("Error message"),
        uiState = UiState.ShowProfileData(
            OtherProfileApiEntity(
                isBlocked = false,
                isPrivateProfile = true,
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
                    isProfileComplete = false,
                )
            )
        ),
        onBackButtonClicked = {},
        navigateToMessageRoom = { a, b, c -> },
        navigateToReportAbuse = {},
        username = "",
        isBlocked = false,
        uiAction = {}
    )
}