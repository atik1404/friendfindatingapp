package com.friend.membership.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.friend.designsystem.R as Res

@Composable
fun HorizontalPagerSection(
    pagerState: PagerState
) {
    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ViewPagerItem(
            modifier = Modifier,
            image = if (pagerState.currentPage == 0) Res.drawable.banner_more_friends else Res.drawable.banner_no_ads,
            title = stringResource(if (pagerState.currentPage == 0) Res.string.msg_remove_ads else Res.string.msg_top_of_inbox),
            description = stringResource(if (pagerState.currentPage == 0) Res.string.msg_find_more else Res.string.msg_remove_ads_description)
        )
    }
}