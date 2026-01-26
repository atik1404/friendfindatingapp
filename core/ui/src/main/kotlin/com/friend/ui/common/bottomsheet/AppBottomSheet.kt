package com.friend.ui.common.bottomsheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.friend.designsystem.R as Res
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPadding
import com.friend.designsystem.theme.textColors
import com.friend.ui.components.AppElevatedButton
import com.friend.ui.components.AppText
import com.friend.ui.openSetting

@Composable
fun RuntimePermissionBottomSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
) {
    val context = LocalContext.current
    ShowBottomSheet(
        onDismissRequest = onDismissRequest,
        title = stringResource(Res.string.title_need_permission),
        titleColor = MaterialTheme.textColors.error
    ) {
        Column(
            modifier = modifier.appPadding(SpacingToken.medium)
        ) {
            AppText(
                text = stringResource(Res.string.msg_permission_needed, "Microphone"),
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(SpacingToken.extraLarge))

            AppElevatedButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.action_go_to_setting),
                onClick = {
                    openSetting(context)
                    onDismissRequest.invoke()
                },
            )
        }
    }
}