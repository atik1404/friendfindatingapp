package com.friend.home

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.friend.common.constant.AppConstants
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPadding
import com.friend.entity.search.FriendSuggestionApiEntity
import com.friend.home.components.PersonItemCardSection
import com.friend.home.components.ProfileSummarySection
import com.friend.home.components.SearchBarSection
import com.friend.ui.common.ErrorType
import com.friend.ui.common.ErrorUi
import com.friend.ui.common.LoadingAnimation
import com.friend.ui.components.AppScaffold
import com.friend.ui.preview.LightPreview
import com.friend.ui.shimmer_effect.PersonItemCardShimmerSection
import com.friend.designsystem.R as Res

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    fullName: String,
    profilePicture: String,
    uiState: UiState,
    filterUiState: FilterUiState,
    onEvent: (UiAction) -> Unit,
    navigateToChatListScreen: () -> Unit,
    navigateToOverviewScreen: () -> Unit,
    navigateToProfileScreen: () -> Unit,
    navigateToOtherProfileScreen: (String) -> Unit,
    navigateToMembershipScreen: () -> Unit,
) {
    var showFilterBottomSheet by rememberSaveable { mutableStateOf(false) }
    AppScaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .consumeWindowInsets(padding)
                .navigationBarsPadding()
                .imePadding()
                .appPadding(SpacingToken.medium)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                SearchBarSection(
                    navigateToOverviewScreen = navigateToOverviewScreen,
                    showBottomSheet = {
                        showFilterBottomSheet = true
                    },
                    navigateToMembership = navigateToMembershipScreen
                )
                Spacer(Modifier.height(SpacingToken.medium))
                ProfileSummarySection(
                    fullName = fullName,
                    profilePicture = profilePicture,
                    navigateToChatListScreen = navigateToChatListScreen,
                    navigateToProfileScreen = {
                        navigateToProfileScreen.invoke()
                    }
                )
                Spacer(Modifier.height(SpacingToken.medium))

                if (uiState.error.isNotEmpty()) {
                    ErrorSection(
                        message = uiState.error
                    ) {
                        onEvent(UiAction.FetchFriendSuggestion)
                    }
                }

                if (uiState.isLoading)
                    LoadingSection()

                if (uiState.data.isEmpty()) {
                    ErrorSection(
                        error = ErrorType.EMPTY_DATA,
                        message = stringResource(Res.string.error_no_data_found)
                    ) {
                        onEvent(UiAction.FetchFriendSuggestion)
                    }
                } else {
                    PersonList(
                        uiState.data,
                        uiState.hasMorePage,
                        onLoadMore = {
                            onEvent.invoke(UiAction.FetchFriendSuggestion)
                        },
                        onPersonClick = { username ->
                            navigateToOtherProfileScreen.invoke(username)
                        }
                    )
                }
            }

            if (uiState.isLoadingMore) {
                LoadingAnimation(
                    modifier = Modifier
                        .align(alignment = Alignment.BottomCenter)
                )
            }
        }
    }

    if (showFilterBottomSheet)
        FilterUserBottomSheet(
            filterUiState = filterUiState,
            onAction = onEvent,
            onSearchApply = {
                showFilterBottomSheet = false
                onEvent.invoke(UiAction.OnFilterApply)
            },
            onDismissRequest = {
                showFilterBottomSheet = false
            },
        )
}

@Composable
private fun ErrorSection(
    message: String,
    error: ErrorType = ErrorType.API_ERROR,
    onRetry: () -> Unit,
) {
    ErrorUi(
        message = message,
        onRetry = onRetry,
        errorType = error
    )
}

@Composable
private fun PersonList(
    items: List<FriendSuggestionApiEntity>,
    hasMoreSize: Boolean,
    onPersonClick: (String) -> Unit,
    onLoadMore: () -> Unit,
) {
    val listState = rememberLazyGridState()
    LaunchedEffect(listState, items, AppConstants.DATA_PER_PAGE) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisible ->
                val lastIndex = items.lastIndex
                if (lastVisible != null && lastVisible == lastIndex && hasMoreSize) {
                    onLoadMore.invoke()
                }
            }
    }
    LazyVerticalGrid(
        state = listState,
        columns = GridCells.Fixed(2),
    ) {
        items(
            items = items,
            key = { it.username }
        )
        { person ->
            PersonItemCardSection(
                person = person,
                modifier = Modifier
                    .clickable {
                        onPersonClick.invoke(person.username)
                    }
            )
        }
    }
}

@Composable
private fun LoadingSection() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
    ) {
        items(10) {
            PersonItemCardShimmerSection(
                modifier = Modifier
            )
        }
    }
}

@Composable
@LightPreview
private fun ScreenPreview() {
    HomeScreen(
        fullName = "Cruise",
        profilePicture = "https://images.mubicdn.net/images/cast_member/2184/cache-2992-1547409411/image-w856.jpg",
        navigateToChatListScreen = {},
        navigateToOverviewScreen = {},
        navigateToProfileScreen = { },
        navigateToOtherProfileScreen = { },
        onEvent = {},
        filterUiState = FilterUiState(),
        uiState = UiState(isLoadingMore = true),
        navigateToMembershipScreen = {}
        //state = UiState.Error("No data found"),
//        state = UiState.Success(
//            listOf(
//                FriendSuggestionApiEntity("Atik Faysal Atik", ""),
//                FriendSuggestionApiEntity("Atik", ""),
//                FriendSuggestionApiEntity("Atik", ""),
//                FriendSuggestionApiEntity("Atik", ""),
//                FriendSuggestionApiEntity("Atik", ""),
//            )
//        ),
    )
}