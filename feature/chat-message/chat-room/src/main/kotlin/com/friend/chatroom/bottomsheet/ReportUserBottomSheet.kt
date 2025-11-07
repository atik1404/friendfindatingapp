package com.friend.chatroom.bottomsheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPadding
import com.friend.designsystem.theme.textColors
import com.friend.designsystem.R as Res
import com.friend.ui.common.ShowBottomSheet
import com.friend.ui.components.AppElevatedButton
import com.friend.ui.components.AppText

@Composable
fun ReportUserBottomSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    onReportUser: () -> Unit
){
    ShowBottomSheet(
        onDismissRequest = onDismissRequest,
        title = "Report an abuse",
        titleColor = MaterialTheme.textColors.error
    ) {
        Column(
            modifier = modifier.appPadding(SpacingToken.medium)
        ) {
            AppText(
                text = stringResource(Res.string.msg_report_abuse),
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(SpacingToken.extraLarge))

            AppElevatedButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.action_report),
                onClick = {
                    onReportUser.invoke()
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@PreviewLightDark
fun ModalPreview() {
    ReportUserBottomSheet{

    }
}