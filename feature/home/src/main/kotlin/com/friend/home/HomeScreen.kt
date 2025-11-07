package com.friend.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.friend.designsystem.spacing.IconSizeToken
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPadding
import com.friend.designsystem.spacing.appPaddingSymmetric
import com.friend.designsystem.theme.surfaceColors
import com.friend.designsystem.theme.textColors
import com.friend.designsystem.theme.textFieldColors
import com.friend.designsystem.typography.AppTypography
import com.friend.ui.components.AppBaseTextField
import com.friend.ui.components.AppScaffold
import com.friend.ui.components.AppText
import com.friend.ui.components.LocalImageLoader
import com.friend.ui.components.NetworkImageLoader
import com.friend.ui.preview.LightDarkPreview
import com.friend.designsystem.R as Res

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToChatListScreen: () -> Unit,
    navigateToOverviewScreen: () -> Unit,
    navigateToProfileScreen: (String, String) -> Unit,
    navigateToSearchScreen: () -> Unit,
) {
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
            SearchBar(
                navigateToOverviewScreen = navigateToOverviewScreen,
                navigateToSearchScreen = navigateToSearchScreen
            )
            Spacer(Modifier.height(SpacingToken.medium))
            ProfileSummary(
                navigateToChatListScreen = navigateToChatListScreen,
                navigateToProfileScreen = {
                    navigateToProfileScreen.invoke("", "")//TODO replace with current user data
                }
            )
            Spacer(Modifier.height(SpacingToken.medium))
            PersonList{
                navigateToProfileScreen.invoke("", "")//TODO replace with current user data
            }
        }
    }
}

@Composable
private fun SearchBar(
    navigateToOverviewScreen: () -> Unit,
    navigateToSearchScreen: () -> Unit,
){
    var userName by rememberSaveable { mutableStateOf("") }
    AppBaseTextField(
        value = userName,
        modifier = Modifier.fillMaxWidth(),
        placeholder = stringResource(Res.string.hint_search_here),
        onValueChange = { userName = it },
        colors = MaterialTheme.textFieldColors.outlinedTextField,
        readOnly = true,
        shape = RoundedCornerShape(SpacingToken.medium),
        trailingIcon = Icons.Default.Search,
        leadingIcon = Icons.Default.Dashboard,
        onTrailingClick = navigateToSearchScreen,
        onLeadingClick = navigateToOverviewScreen
    )
}

@Composable
private fun ProfileSummary(
    navigateToChatListScreen: () -> Unit,
    navigateToProfileScreen: () -> Unit,
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        NetworkImageLoader(
            "https://images.unsplash.com/photo-1483909796554-bb0051ab60ad?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&q=80&w=2373",
            modifier = Modifier
                .size(IconSizeToken.extraLarge)
                .clickable{
                    navigateToProfileScreen.invoke()
                },
            shape = CircleShape
        )

        Spacer(
            modifier = Modifier.width(SpacingToken.medium)
        )

        Column {
            AppText(
                text = "Good Morning, Atik Faysal",
                textStyle = AppTypography.titleMedium,
                fontWeight = FontWeight.Bold,
                textColor = MaterialTheme.textColors.primary,
            )

            Spacer(
                modifier = Modifier.height(SpacingToken.micro)
            )

            AppText(
                text = "Welcome back to your dashboard",
                textStyle = AppTypography.bodyMedium,
                fontWeight = FontWeight.Light,
                textColor = MaterialTheme.textColors.primary,
            )
        }

        Spacer(
            modifier = Modifier.weight(1f)
        )

        IconButton(
            onClick = {
                navigateToChatListScreen.invoke()
            }
        ){
            LocalImageLoader(
                imageResId = Res.drawable.ic_chat_bubble,
                modifier = Modifier.size(IconSizeToken.medium)
            )
        }
    }
}

@Composable
private fun PersonList(
    onPersonClick: (String) -> Unit
){
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
    ) {
        items(100) {
            PersonCardItem(
                modifier = Modifier
                    .clickable{
                        onPersonClick.invoke("")
                    }
            )
        }
    }
}

@Composable
private fun PersonCardItem(
    modifier: Modifier
){
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier.appPadding(SpacingToken.micro)
    ) {
        NetworkImageLoader(
            "https://images.unsplash.com/photo-1483909796554-bb0051ab60ad?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&q=80&w=2373",
            modifier = modifier
                .fillMaxSize()
                .height(200.dp),
            shape = RoundedCornerShape(SpacingToken.extraSmall)
        )

        AppText(
            text = "Atik Faysal",
            textStyle = AppTypography.bodyMedium,
            fontWeight = FontWeight.Bold,
            textColor = MaterialTheme.textColors.white,
            modifier = modifier
                .appPaddingSymmetric(
                    horizontal = SpacingToken.medium,
                    vertical = SpacingToken.micro
                )
                .background(
                    MaterialTheme.surfaceColors.tertiary.copy(alpha = .1f),
                    shape = RoundedCornerShape(SpacingToken.medium)
                )
                .appPaddingSymmetric(
                    horizontal = SpacingToken.medium,
                    vertical = SpacingToken.micro
                ),
            alignment = TextAlign.Center,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
@LightDarkPreview
private fun ScreenPreview() {
    HomeScreen(
        navigateToChatListScreen = {},
        navigateToOverviewScreen = {},
        navigateToProfileScreen = { _, _ ->},
        navigateToSearchScreen = {}
    )
}