package com.friend.membership

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.friend.designsystem.spacing.IconSizeToken
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPadding
import com.friend.ui.common.bottomsheet.ShowBottomSheet
import com.friend.ui.components.AppElevatedButton
import com.friend.ui.components.AppText
import com.friend.ui.components.ResourceImageLoader
import com.friend.ui.preview.LightPreview
import com.friend.designsystem.R as Res

@Composable
fun PackagesBottomSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    monthlySubscription: () -> Unit,
    yearlySubscription: () -> Unit,
    manageSubscription: () -> Unit
) {
    ShowBottomSheet(
        onDismissRequest = onDismissRequest,
        title = stringResource(Res.string.title_member_ship),
    ) {
        Column(
            modifier = modifier.appPadding(SpacingToken.medium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ResourceImageLoader(
                imageResId = Res.drawable.img_vip,
                modifier = modifier.size(IconSizeToken.extraLarge)
            )

            Spacer(modifier = Modifier.height(SpacingToken.medium))

            AppText(
                text = stringResource(Res.string.msg_get_more_in_vip),
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = modifier.height(SpacingToken.huge))

            AppElevatedButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.action_monthly),
                onClick = monthlySubscription,
            )

            Spacer(modifier = Modifier.height(SpacingToken.medium))

            AppElevatedButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.action_yearly),
                onClick = yearlySubscription,
            )

            Spacer(modifier = Modifier.height(SpacingToken.medium))

            AppElevatedButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.action_manage_subscription),
                onClick = manageSubscription,
            )

            Spacer(modifier = modifier.height(SpacingToken.hugePlus))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@LightPreview
fun ModalPreview() {
    PackagesBottomSheet(
        monthlySubscription = {},
        yearlySubscription = {},
        manageSubscription = {}
    )
}