package com.friend.home

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPadding
import com.friend.home.components.PersonItemCardSection
import com.friend.home.components.ProfileSummarySection
import com.friend.home.components.SearchBarSection
import com.friend.ui.components.AppScaffold
import com.friend.ui.preview.LightDarkPreview
import com.friend.ui.preview.LightPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    fullName: String,
    navigateToChatListScreen: () -> Unit,
    navigateToOverviewScreen: () -> Unit,
    navigateToProfileScreen: (String, String) -> Unit,
) {
    var showFilterBottomSheet by rememberSaveable { mutableStateOf(false) }
    AppScaffold(
        contentWindowInsets = WindowInsets.safeDrawing
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .consumeWindowInsets(padding)
                .navigationBarsPadding()
                .imePadding()
                .appPadding(SpacingToken.medium)
        ) {
            SearchBarSection(
                navigateToOverviewScreen = navigateToOverviewScreen,
                navigateToSearchScreen = {
                    showFilterBottomSheet = true
                }
            )
            Spacer(Modifier.height(SpacingToken.medium))
            ProfileSummarySection(
                fullName = fullName,
                navigateToChatListScreen = navigateToChatListScreen,
                navigateToProfileScreen = {
                    navigateToProfileScreen.invoke("", "")//TODO replace with current user data
                }
            )
            Spacer(Modifier.height(SpacingToken.medium))
            PersonList {
                navigateToProfileScreen.invoke(
                    "others",
                    "others"
                )//TODO replace with current user data
            }
        }
    }

    if (showFilterBottomSheet)
        FilterUserBottomSheet(
            onSearchApply = {
                showFilterBottomSheet = false
            },
            onDismissRequest = {
                showFilterBottomSheet = false
            },
        )
}

@Composable
private fun PersonList(
    onPersonClick: (String) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
    ) {
        items(100) {
            PersonItemCardSection(
                modifier = Modifier
                    .clickable {
                        onPersonClick.invoke("others")
                    }
            )
        }
    }
}

@Composable
@LightPreview
private fun ScreenPreview() {
    HomeScreen(
        fullName = "Cruise",
        navigateToChatListScreen = {},
        navigateToOverviewScreen = {},
        navigateToProfileScreen = { _, _ -> },
    )
}