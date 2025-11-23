package com.friend.membership

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.friend.designsystem.R as Res
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPadding
import com.friend.designsystem.spacing.appPaddingHorizontal
import com.friend.designsystem.spacing.appPaddingVertical
import com.friend.designsystem.theme.surfaceColors
import com.friend.designsystem.typography.AppTypography
import com.friend.ui.common.AppToolbar
import com.friend.ui.components.AppElevatedButton
import com.friend.ui.components.AppScaffold
import com.friend.ui.components.AppText
import com.friend.ui.components.LocalImageLoader
import com.friend.ui.preview.LightPreview
import com.google.accompanist.pager.HorizontalPagerIndicator
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MembershipScreen(
    onBackClick: () -> Unit,
) {

    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    AppScaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            AppToolbar(
                title = stringResource(Res.string.menu_vip_membership),
                onBackClick = {
                    onBackClick.invoke()
                })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .consumeWindowInsets(padding)
                .navigationBarsPadding()
                .imePadding()
                .appPadding(SpacingToken.medium),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            BannerPager()

            Spacer(modifier = Modifier.height(SpacingToken.huge))

            AppElevatedButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.action_activate_vip_membership),
                onClick = {
                    showBottomSheet = true
                },
            )

            if (showBottomSheet)
                PackagesBottomSheet(
                    onClickListener = {
                        showBottomSheet = false
                    },
                    onDismissRequest = {
                        showBottomSheet = false
                    },
                )
        }
    }
}

@Composable
private fun BannerPager() {
    val pageCount = 2

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { 2 }
    )

    // Auto-slide effect
    LaunchedEffect(pagerState) {
        while (true) {
            delay(1500)
            if (pagerState.isScrollInProgress) continue

            val nextPage = (pagerState.currentPage + 1) % pageCount
            pagerState.animateScrollToPage(nextPage)
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        PagerItem(
            modifier = Modifier,
            image = if (pagerState.currentPage == 0) Res.drawable.banner_more_friends else Res.drawable.banner_no_ads,
            title = stringResource(if (pagerState.currentPage == 0) Res.string.msg_remove_ads else Res.string.msg_top_of_inbox),
            description = stringResource(if (pagerState.currentPage == 0) Res.string.msg_remove_ads_description else Res.string.msg_connect_to_unlimited_people_description)
        )
    }

    Spacer(modifier = Modifier.height(SpacingToken.large))

    HorizontalPagerIndicator(pagerState, pageCount = 2)
}

@Composable
private fun PagerItem(
    modifier: Modifier,
    image: Int,
    title: String,
    description: String,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.surfaceColors.white,
            contentColor = CardDefaults.cardColors().contentColor,
        )
    ) {
        Column(
            modifier = modifier
                .appPaddingVertical(SpacingToken.huge)
                .appPaddingHorizontal(SpacingToken.small),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LocalImageLoader(
                imageResId = image,
                modifier = modifier
                    .fillMaxWidth()
                    .height(220.dp),
                contentScale = ContentScale.Fit
            )

            AppText(
                text = title,
                modifier = modifier.appPadding(SpacingToken.medium),
                textStyle = AppTypography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            AppText(
                text = description,
                textStyle = AppTypography.bodySmall,
            )
        }
    }
}

@Composable
@LightPreview
private fun ScreenPreview() {
    MembershipScreen {

    }
}