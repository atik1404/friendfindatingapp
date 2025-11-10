package com.friend.chatroom.bottomsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPaddingHorizontal
import com.friend.designsystem.spacing.appPaddingOnly
import com.friend.designsystem.theme.buttonColors
import com.friend.ui.common.ShowBottomSheet
import com.friend.ui.components.AppCheckbox
import com.friend.ui.components.AppText
import com.friend.ui.components.AppTextButton
import com.friend.ui.preview.LightPreview
import com.friend.designsystem.R as Res

@Composable
fun MessageForwardBottomSheet(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onForward: () -> Unit,
) {
    ShowBottomSheet(onDismissRequest = onDismiss, title = "Forward to") {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            AppTextButton(
                modifier = modifier.appPaddingOnly(end =  SpacingToken.medium),
                text = stringResource(Res.string.action_forward),
                onClick = {},
                trailingIcon = Icons.Default.NavigateNext,
                textColor = MaterialTheme.buttonColors.primaryButton.containerColor,
            )

            LazyColumn(
                modifier = modifier
                    .fillMaxWidth()
                    .appPaddingHorizontal(SpacingToken.medium),
            ) {
                items(50) { index ->
                    MessageForwardItem(
                        modifier = Modifier,
                        index = index
                    )
                }
            }
        }
    }
}

@Composable
private fun MessageForwardItem(
    modifier: Modifier,
    index: Int,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AppText(
            text = "Username $index",
            modifier = modifier.padding(vertical = SpacingToken.medium)
        )

        AppCheckbox(
            checked = false,
            onCheckedChange = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@LightPreview
fun ForwardMessageModalPreview() {
    MessageForwardBottomSheet(
        onDismiss = {},
        onForward = {}
    )
}