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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPadding
import com.friend.membership.components.HorizontalPagerSection
import com.friend.ui.common.AppToolbar
import com.friend.ui.common.PageIndicator
import com.friend.ui.components.AppElevatedButton
import com.friend.ui.components.AppScaffold
import com.friend.ui.components.AppText
import com.friend.ui.preview.LightPreview
import kotlinx.coroutines.delay
import com.friend.designsystem.R as Res

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MembershipScreen(
    onBackClick: () -> Unit,
    monthlySubscription: () -> Unit,
    yearlySubscription: () -> Unit,
    manageSubscription: () -> Unit,
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
                .imePadding()
                .appPadding(SpacingToken.medium),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AppText(
                text = stringResource(Res.string.msg_get_more_in_vip),
                fontWeight = FontWeight.Bold,
                textStyle = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.weight(.5f))

            BannerPager()

            Spacer(modifier = Modifier.height(SpacingToken.huge))

            AppElevatedButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.action_activate_vip_membership),
                onClick = {
                    showBottomSheet = true
                },
            )

            Spacer(modifier = Modifier.weight(.5f))

            if (showBottomSheet)
                PackagesBottomSheet(
                    onDismissRequest = {
                        showBottomSheet = false
                    },
                    monthlySubscription = {
                        showBottomSheet = false
                        monthlySubscription.invoke()
                    },
                    yearlySubscription = {
                        showBottomSheet = false
                        yearlySubscription.invoke()
                    },
                    manageSubscription = {
                        showBottomSheet = false
                        manageSubscription.invoke()
                    }
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
            delay(2500)
            if (pagerState.isScrollInProgress) continue
            val nextPage = (pagerState.currentPage + 1) % pageCount
            pagerState.animateScrollToPage(nextPage)
        }
    }

    HorizontalPagerSection(
        pagerState = pagerState
    )

    Spacer(modifier = Modifier.height(SpacingToken.large))

    PageIndicator(pageCount = 2, currentPage = pagerState.currentPage)
}

@Composable
@LightPreview
private fun ScreenPreview() {
    MembershipScreen(
        onBackClick = {},
        monthlySubscription = {},
        yearlySubscription = {},
        manageSubscription = {},
    )
}