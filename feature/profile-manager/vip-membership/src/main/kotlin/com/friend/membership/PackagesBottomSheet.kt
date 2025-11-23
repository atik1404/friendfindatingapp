package com.friend.membership

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPadding
import com.friend.ui.common.ShowBottomSheet
import com.friend.ui.components.AppElevatedButton
import com.friend.ui.preview.LightPreview
import com.friend.designsystem.R as Res

@Composable
fun PackagesBottomSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    onClickListener: () -> Unit
) {
    ShowBottomSheet(
        onDismissRequest = onDismissRequest,
        title = stringResource(Res.string.title_member_ship),
    ) {
        Column(
            modifier = modifier.appPadding(SpacingToken.medium)
        ) {
            AppElevatedButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.action_monthly),
                onClick = {

                },
            )

            Spacer(modifier = Modifier.height(SpacingToken.medium))

            AppElevatedButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.action_yearly),
                onClick = {

                },
            )

            Spacer(modifier = Modifier.height(SpacingToken.medium))

            AppElevatedButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.action_manage_subscription),
                onClick = {

                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@LightPreview
fun ModalPreview() {
    PackagesBottomSheet {

    }
}