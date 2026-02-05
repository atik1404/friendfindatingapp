package com.friend.membership

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.friend.designsystem.spacing.IconSizeToken
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPadding
import com.friend.designsystem.theme.textColors
import com.friend.designsystem.typography.AppTypography
import com.friend.ui.common.bottomsheet.ShowBottomSheet
import com.friend.ui.components.AppElevatedButton
import com.friend.ui.components.AppText
import com.friend.ui.components.ColoredTextSegment
import com.friend.ui.components.MultiColorText
import com.friend.ui.components.ResourceImageLoader
import com.friend.ui.preview.LightPreview
import com.friend.designsystem.R as Res

@Composable
fun PackagesBottomSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    monthlySubscription: () -> Unit,
    yearlySubscription: () -> Unit,
    manageSubscription: () -> Unit,
    privacyPolicyClicked: () -> Unit,
) {
    ShowBottomSheet(
        onDismissRequest = onDismissRequest,
        isExpand = true,
        title = stringResource(Res.string.title_member_ship),
    ) {
        PackageDetails(
            modifier = modifier,
            monthlySubscription = monthlySubscription,
            yearlySubscription = yearlySubscription,
            manageSubscription = manageSubscription,
            privacyPolicyClicked = privacyPolicyClicked,
        )
    }
}

@Composable
private fun PackageDetails(
    modifier: Modifier = Modifier,
    monthlySubscription: () -> Unit,
    yearlySubscription: () -> Unit,
    manageSubscription: () -> Unit,
    privacyPolicyClicked: () -> Unit,
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

        Spacer(modifier = modifier.height(SpacingToken.extraSmall))

        AppText(
            text = stringResource(Res.string.msg_vip_member),
            fontWeight = FontWeight.Light,
            maxLines = 3,
            alignment = TextAlign.Center
        )

        Spacer(modifier = modifier.height(SpacingToken.huge))

        AppElevatedButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.placeholder_monthly, "$8.99"),
            onClick = monthlySubscription,
        )

        Spacer(modifier = Modifier.height(SpacingToken.medium))

        AppElevatedButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.placeholder_yearly, "$59.99"),
            onClick = yearlySubscription,
        )

        Spacer(modifier = Modifier.height(SpacingToken.medium))

        AppElevatedButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.action_manage_subscription),
            onClick = manageSubscription,
        )

        Spacer(modifier = modifier.height(SpacingToken.large))


        val segments = listOf(
            ColoredTextSegment(
                text = stringResource(Res.string.msg_auto_renew),
                color = MaterialTheme.textColors.secondary,
                style = AppTypography.bodySmall.copy(fontWeight = FontWeight.Light),
                addSpace = true
            ),
            ColoredTextSegment(
                text = stringResource(Res.string.label_privacy_policy),
                color = MaterialTheme.textColors.brand,
                style = AppTypography.bodySmall.copy(fontWeight = FontWeight.Bold),
                addSpace = true,
                onClick = privacyPolicyClicked
            ),
        )
        MultiColorText(segments = segments)

        Spacer(modifier = modifier.height(SpacingToken.large))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@LightPreview
fun ModalPreview() {
    PackageDetails(
        monthlySubscription = {},
        yearlySubscription = {},
        privacyPolicyClicked = {},
        manageSubscription = {}
    )
}