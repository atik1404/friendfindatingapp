package com.friend.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.friend.designsystem.spacing.SpacingToken

@Composable
fun AppCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,

    // Simple text label use-case
    label: String? = null,
    labelTextStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    labelColor: Color = MaterialTheme.colorScheme.onSurface,

    // Advanced use-case: fully custom label row content
    customLabel: (@Composable () -> Unit)? = null,

    // If you ever need RTL-ish row with text first and checkbox last
    checkboxFirst: Boolean = true,

    // Spacing between checkbox and label
    horizontalSpacing: Dp = SpacingToken.none,
) {
    // We wrap the whole row in clickable so tapping text toggles the checkbox
    Row(
        modifier = modifier
            .clickable(
                enabled = enabled,
                // we mirror Checkbox behavior:
                role = Role.Checkbox,
                onClick = { onCheckedChange(!checked) }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (checkboxFirst) {
            CheckboxSection(
                checked = checked,
                enabled = enabled,
                onCheckedChange = onCheckedChange
            )
            LabelSection(
                label = label,
                customLabel = customLabel,
                labelTextStyle = labelTextStyle,
                labelColor = labelColor,
                horizontalSpacing = horizontalSpacing
            )
        } else {
            LabelSection(
                label = label,
                customLabel = customLabel,
                labelTextStyle = labelTextStyle,
                labelColor = labelColor,
                horizontalSpacing = horizontalSpacing
            )
            CheckboxSection(
                checked = checked,
                enabled = enabled,
                onCheckedChange = onCheckedChange
            )
        }
    }
}

@Composable
private fun LabelSection(
    label: String?,
    customLabel: (@Composable () -> Unit)?,
    labelTextStyle: TextStyle,
    labelColor: Color,
    horizontalSpacing: Dp,
) {
    if (label != null || customLabel != null) {
        Spacer(Modifier.width(horizontalSpacing))

        if (customLabel != null) {
            // Dev provided custom composable (can be Column with title+subtitle, etc.)
            customLabel()
        } else {
            // Default simple text
            Text(
                text = label.orEmpty(),
                style = labelTextStyle,
                color = labelColor
            )
        }
    }
}

@Composable
private fun CheckboxSection(
    checked: Boolean,
    enabled: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Checkbox(
        checked = checked,
        onCheckedChange = { onCheckedChange(it) },
        enabled = enabled,
    )
}
