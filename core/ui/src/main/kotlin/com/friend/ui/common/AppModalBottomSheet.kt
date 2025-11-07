package com.friend.ui.common

import AppDivider
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.theme.dividerColors
import com.friend.designsystem.theme.textColors
import com.friend.designsystem.typography.AppTypography
import com.friend.ui.components.AppText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowBottomSheet(
    title: String,
    titleColor: Color = MaterialTheme.textColors.primary,
    onDismissRequest: () -> Unit,
    cancellable: Boolean = true,
    content: @Composable () -> Unit
) {
    val sheetState = rememberStandardBottomSheetState(
        skipHiddenState = !cancellable,
        confirmValueChange = { target ->
            !(!cancellable && target == SheetValue.Hidden) // don't let it hide
        }
    )
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
    ) {
        // Get the available height of the bottom sheet's layout scope
        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth()
        ) {
            // maxHeight here is the full window height in Dp.
            // We'll allow the sheet content to grow up to 60% of that.
            val maxSheetHeight = maxHeight * 0.75f

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = maxSheetHeight) // ⬅ limit how tall the sheet can get
            ) {
                // Header row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = SpacingToken.medium)
                ) {
                    AppText(
                        text = title,
                        textColor = titleColor,
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.Bold,
                        textStyle = AppTypography.titleMedium
                    )

                    IconButton(onClick = onDismissRequest) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Close"
                        )
                    }
                }

                // Divider
                AppDivider(
                    color = MaterialTheme.dividerColors.primary
                )

                content()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@PreviewLightDark
fun ModalPreview() {
    ShowBottomSheet(onDismissRequest = {}, title = "Sample Bottom Sheet") {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = SpacingToken.medium)
        ) {
            items(102) { index ->
                Text(
                    text = "Item $index",
                    modifier = Modifier.padding(vertical = SpacingToken.medium)
                )
            }
        }
    }
}