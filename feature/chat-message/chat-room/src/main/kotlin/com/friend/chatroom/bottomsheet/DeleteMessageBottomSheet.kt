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
import com.friend.designsystem.R as Res
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPadding
import com.friend.designsystem.theme.textColors
import com.friend.ui.common.bottomsheet.ShowBottomSheet
import com.friend.ui.components.AppElevatedButton
import com.friend.ui.components.AppText
import com.friend.ui.preview.LightPreview

@Composable
fun DeleteMessageBottomSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    onDelete: () -> Unit
) {
    ShowBottomSheet(
        onDismissRequest = onDismissRequest,
        title = stringResource(Res.string.title_delete_message),
        titleColor = MaterialTheme.textColors.error
    ) {
        Column(
            modifier = modifier.appPadding(SpacingToken.medium)
        ) {
            AppText(
                text = stringResource(Res.string.msg_delete_message),
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(SpacingToken.extraLarge))

            AppElevatedButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.menu_delete_message),
                onClick = {
                    onDelete.invoke()
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@LightPreview
private fun DeleteModalPreview() {
    DeleteMessageBottomSheet {

    }
}