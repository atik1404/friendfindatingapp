package com.friend.profile

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
import androidx.compose.ui.text.font.FontWeight
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPadding
import com.friend.designsystem.typography.AppTypography
import com.friend.profile.ui.ProfileHeaderUi
import com.friend.ui.common.AppToolbar
import com.friend.ui.components.AppPopupMenu
import com.friend.ui.components.AppScaffold
import com.friend.ui.components.AppText
import com.friend.ui.components.ProfilePopupMenu
import com.friend.ui.preview.LightDarkPreview
import com.friend.designsystem.R as Res

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    username: String,
    userId: String,
    onBackButtonClicked: () -> Unit,
    navigateToEditProfile: () -> Unit,
    navigateToMessageRoom: () -> Unit,
) {
    val isOthersProfile = username == "others" || userId == "others"

    AppScaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            AppToolbar(
                title = stringResource(Res.string.title_profile),
                onBackClick = {
                    onBackButtonClicked.invoke()
                },
                actions = {
                    if (isOthersProfile)
                        AppPopupMenu(
                            icon = Icons.Default.MoreVert,
                            menuItems = ProfilePopupMenu,
                            onClick = { menu ->

                            }
                        )
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()              // from Scaffold
                .padding(padding)
                .consumeWindowInsets(padding)    // prevent double-inset consumption downstream
                .navigationBarsPadding()         // keep content above system nav bar
                .imePadding()                    // lift content when keyboard shows
                .verticalScroll(rememberScrollState())
                .appPadding(SpacingToken.small),
        ) {
            ProfileHeaderUi(
                modifier = Modifier,
                onEditClick = { navigateToEditProfile.invoke() },
                onSendMsgClicked = { navigateToMessageRoom.invoke() },
                isOtherProfile = isOthersProfile
            )

            Spacer(modifier = Modifier.height(SpacingToken.medium))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LabeledValue(
                    title = stringResource(Res.string.label_date_of_birth),
                    value = "September 29, 1995",
                    modifier = Modifier.weight(1f)
                )

                LabeledValue(
                    title = stringResource(Res.string.label_gender),
                    value = "Male",
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
                    title = stringResource(Res.string.label_looking_for),
                    value = "Female",
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
                    value = "5 feet 4 inch",
                    modifier = Modifier.weight(1f)
                )

                LabeledValue(
                    title = stringResource(Res.string.label_weight),
                    value = "77 Kg",
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
                    value = "Black",
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
                    value = "Sometimes",
                    modifier = Modifier.weight(1f)
                )

                LabeledValue(
                    title = stringResource(Res.string.label_drinking),
                    value = "No",
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
                    value = "Slim",
                    modifier = Modifier.weight(1f)
                )

                LabeledValue(
                    title = stringResource(Res.string.label_country),
                    value = "Bangladesh",
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
                    value = "Dhaka",
                    modifier = Modifier.weight(1f)
                )

                LabeledValue(
                    title = stringResource(Res.string.label_state),
                    value = "Dhaka",
                    modifier = Modifier.weight(1f),
                    alignment = Alignment.End
                )
            }

            Spacer(modifier = Modifier.height(SpacingToken.medium))

            LabeledValue(
                title = stringResource(Res.string.label_about_yourself),
                value = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
                modifier = Modifier.fillMaxWidth(),
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(SpacingToken.medium))

            LabeledValue(
                title = stringResource(Res.string.label_interested_in),
                value = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
                modifier = Modifier.fillMaxWidth(),
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(SpacingToken.medium))

            LabeledValue(
                title = stringResource(Res.string.label_whats_up),
                value = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
                modifier = Modifier.fillMaxWidth(),
                maxLines = 5
            )
        }
    }
}

@Composable
private fun LabeledValue(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    alignment: Alignment.Horizontal = Alignment.Start,
    maxLines: Int = 1,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = alignment
    ) {
        AppText(
            text = title,
            fontWeight = FontWeight.Light,
            textStyle = AppTypography.bodySmall
        )
        Spacer(modifier = Modifier.height(SpacingToken.micro))
        AppText(
            text = value,
            fontWeight = FontWeight.Medium,
            maxLines = maxLines
        )
    }
}

@Composable
@LightDarkPreview
private fun ScreenPreview() {
    ProfileScreen(
        username = "",
        userId = "",
        onBackButtonClicked = {},
        navigateToEditProfile = {},
        navigateToMessageRoom = {},
    )
}