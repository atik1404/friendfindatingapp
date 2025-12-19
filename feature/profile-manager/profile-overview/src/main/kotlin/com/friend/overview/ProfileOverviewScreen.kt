package com.friend.overview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.friend.common.constant.PersonalMenu
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPadding
import com.friend.overview.components.MenuCard
import com.friend.overview.components.ProfileSummaryUi
import com.friend.ui.common.AppToolbar
import com.friend.ui.common.LoadingUi
import com.friend.ui.components.AppScaffold
import com.friend.ui.preview.LightPreview
import com.friend.designsystem.R as Res

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileOverviewScreen(
    userInfo: UserInfo,
    state: UiState,
    onBackButtonClicked: () -> Unit,
    navigateToProfileScreen: () -> Unit,
    clickedOnMenu: (PersonalMenu) -> Unit,
) {
    AppScaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            AppToolbar(
                title = stringResource(Res.string.title_overview),
                onBackClick = {
                    onBackButtonClicked.invoke()
                })
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .consumeWindowInsets(padding)
                    .navigationBarsPadding()
                    .imePadding()
                    .appPadding(SpacingToken.medium)
            ) {
                ProfileSummaryUi(
                    modifier = Modifier,
                    name = userInfo.username,
                    image = userInfo.image,
                    email = userInfo.email,
                ) {
                    navigateToProfileScreen.invoke()
                }

                Spacer(modifier = Modifier.height(SpacingToken.medium))

                MenuCard {
                    clickedOnMenu.invoke(it)
                }
            }


            when (state) {
                is UiState.Loading -> {
                    if (state.isLoading) {
                        LoadingUi()
                    }
                }
            }
        }
    }
}

@Composable
@LightPreview
private fun ScreenPreview() {
    ProfileOverviewScreen(
        onBackButtonClicked = {},
        navigateToProfileScreen = {}, clickedOnMenu = {},
        userInfo = UserInfo(
            username = "Atik Faysal",
            email = "atik@gmail.com",
            image = "https://images.mubicdn.net/images/cast_member/2184/cache-2992-1547409411/image-w856.jpg"
        ),
        state = UiState.Loading(false),
    )
}