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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.friend.common.dateparser.DateTimePatterns
import com.friend.common.dateparser.DateTimeUtils
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPadding
import com.friend.entity.profilemanager.ProfileApiEntity
import com.friend.otherprofile.components.LabeledValue
import com.friend.otherprofile.components.ProfileHeaderUi
import com.friend.ui.common.AppToolbar
import com.friend.ui.common.ErrorUi
import com.friend.ui.common.LoadingUi
import com.friend.ui.components.AppPopupMenu
import com.friend.ui.components.AppScaffold
import com.friend.ui.components.PopupMenuType
import com.friend.ui.components.ProfilePopupMenu
import com.friend.ui.preview.LightPreview
import com.friend.designsystem.R as Res

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtherProfileScreen(
    uiState: UiState,
    onBackButtonClicked: () -> Unit,
    navigateToMessageRoom: () -> Unit,
    navigateToReportAbuse: () -> Unit,
    onRetry: () -> Unit,
) {
    AppScaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            AppToolbar(
                title = stringResource(Res.string.title_profile),
                onBackClick = {
                    onBackButtonClicked.invoke()
                },
                actions = {
                    AppPopupMenu(
                        icon = Icons.Default.MoreVert,
                        menuItems = ProfilePopupMenu,
                        onClick = { menu ->
                            when (menu) {
                                PopupMenuType.ReportUser -> {
                                    navigateToReportAbuse.invoke()
                                }

                                PopupMenuType.BlockUser -> {}
                                PopupMenuType.UnblockUser -> {}
                                else -> {}
                            }
                        }
                    )
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
                onRetry.invoke()
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
                    data = uiState.data.profile,
                    onSendMsgClicked = navigateToMessageRoom
                )
            }
        }
    }
}

@Composable
private fun ProfileUi(
    modifier: Modifier,
    data: ProfileApiEntity,
    onSendMsgClicked: () -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        ProfileHeaderUi(
            modifier = Modifier,
            fullName = data.fullName,
            email = data.email,
            profilePicture = data.profilePicture,
            onSendMsgClicked = onSendMsgClicked
        )

        Spacer(modifier = Modifier.height(SpacingToken.medium))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LabeledValue(
                title = stringResource(Res.string.label_date_of_birth),
                value = DateTimeUtils.parseToPattern(
                    data.birthdate,
                    DateTimePatterns.MDY_TEXT_COMMA
                ),
                modifier = Modifier.weight(1f)
            )

            LabeledValue(
                title = stringResource(Res.string.label_gender),
                value = data.gender,
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
                value = data.interestedIn,
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
                value = data.height,
                modifier = Modifier.weight(1f)
            )

            LabeledValue(
                title = stringResource(Res.string.label_weight),
                value = data.weight,
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
                value = data.hair,
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
                value = data.smoking,
                modifier = Modifier.weight(1f)
            )

            LabeledValue(
                title = stringResource(Res.string.label_drinking),
                value = data.drinking,
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
                value = data.bodyType,
                modifier = Modifier.weight(1f)
            )

            LabeledValue(
                title = stringResource(Res.string.label_country),
                value = data.country,
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
                value = data.city,
                modifier = Modifier.weight(1f)
            )

            LabeledValue(
                title = stringResource(Res.string.label_state),
                value = data.state,
                modifier = Modifier.weight(1f),
                alignment = Alignment.End
            )
        }

        Spacer(modifier = Modifier.height(SpacingToken.medium))

        LabeledValue(
            title = stringResource(Res.string.label_about_yourself),
            value = data.aboutYou,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(SpacingToken.medium))

        LabeledValue(
            title = stringResource(Res.string.label_interest),
            value = data.interests,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(SpacingToken.medium))

        LabeledValue(
            title = stringResource(Res.string.label_whats_up),
            value = data.whatsUp,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 5
        )
    }
}

@Composable
@LightPreview
private fun ScreenPreview() {
    OtherProfileScreen(
        uiState = UiState.ApiError("Error message"),
//        uiState = UiState.ShowProfileData(
//            OtherProfileApiEntity(
//                isBlocked = false,
//                profile = ProfileApiEntity(
//                    userName = "Tom Cruise",
//                    fullName = "Tom Cruise",
//                    gender = "Male",
//                    birthdate = "September 29, 1995",
//                    email = "tom@gmail.com",
//                    interestedIn = "Female",
//                    country = "Bangladesh",
//                    state = "Dhaka",
//                    city = "Dhaka",
//                    zipCode = "",
//                    profilePicture = "https://images.mubicdn.net/images/cast_member/2184/cache-2992-1547409411/image-w856.jpg",
//                    bodyType = "",
//                    drinking = "",
//                    eyes = "",
//                    hair = "",
//                    height = "",
//                    interests = "",
//                    lookingFor = "",
//                    smoking = "",
//                    aboutYou = "",
//                    title = "",
//                    weight = "",
//                    whatsUp = "",
//                    isProfileComplete = false
//                )
//            )
//        ),
        onBackButtonClicked = {},
        navigateToMessageRoom = {},
        navigateToReportAbuse = {},
        onRetry = {}
    )
}