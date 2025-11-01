package com.friend.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.friend.designsystem.spacing.RadiusToken
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.StrokeTokens
import com.friend.designsystem.spacing.appPaddingSymmetric
import com.friend.designsystem.spacing.appPaddingVertical
import com.friend.designsystem.theme.buttonColors
import com.friend.designsystem.theme.strokeColors
import com.friend.designsystem.theme.textColors
import com.friend.designsystem.typography.AppTypography

@Composable
private fun BaseChip(
    label: String,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    enabled: Boolean = true,
    onClick: (() -> Unit),
    shape: Shape = RoundedCornerShape(RadiusToken.medium),
    textStyle: TextStyle = AppTypography.bodyMedium,
    leading: (@Composable RowScope.() -> Unit)? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null,
) {
    Surface(
        modifier = modifier,
        shape = shape,
        border = BorderStroke(
            StrokeTokens.thin,
            MaterialTheme.strokeColors.primary
        ),
        shadowElevation = 0.dp,
        onClick = onClick,
        enabled = enabled
    ) {
        Row(
            modifier = Modifier
                .background(
                    if (selected)
                        MaterialTheme.buttonColors.primaryButton.containerColor
                    else
                        MaterialTheme.buttonColors.outlineButton.containerColor
                )
                .appPaddingSymmetric(
                    vertical = SpacingToken.micro,
                    horizontal = SpacingToken.medium
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leading != null) {
                leading()
                Spacer(Modifier.width(SpacingToken.micro))
            }
            Text(
                text = label,
                style = textStyle,
                color = if (selected)
                    MaterialTheme.buttonColors.primaryButton.contentColor
                else
                    MaterialTheme.textColors.primary,
                maxLines = 1,
                modifier = Modifier.appPaddingVertical(SpacingToken.micro)
            )
            if (trailing != null) {
                Spacer(Modifier.width(SpacingToken.micro))
                trailing()
            }
        }
    }
}

/* =========================
   Specializations (thin wrappers)
   ========================= */

@Composable
private fun FilterChip(
    label: String,
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leading: (@Composable RowScope.() -> Unit)? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null,
) = BaseChip(
    label = label,
    modifier = modifier,
    selected = selected,
    enabled = enabled,
    leading = leading,
    trailing = trailing,
    onClick = {
        onSelectedChange(!selected)
    }
)

/* =========================
   Chip groups (using BaseChip)
   ========================= */

@Composable
fun SingleSelectChipGroup(
    items: List<String>,
    selectedIndex: Int,
    onSelectedIndexChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    hGap: Dp = SpacingToken.tiny,
    vGap: Dp = SpacingToken.tiny,
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(hGap),
        verticalArrangement = Arrangement.spacedBy(vGap)
    ) {
        items.forEachIndexed { index, label ->
            FilterChip(
                label = label,
                selected = index == selectedIndex,
                onSelectedChange = { onSelectedIndexChange(index) }
            )
        }
    }
}

@Composable
fun MultiSelectChipGroup(
    items: List<String>,
    selected: Set<Int>,
    onSelectionChange: (Set<Int>) -> Unit,
    modifier: Modifier = Modifier,
    hGap: Dp = SpacingToken.tiny,
    vGap: Dp = SpacingToken.none,
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(hGap),
        verticalArrangement = Arrangement.spacedBy(vGap)
    ) {
        items.forEachIndexed { index, label ->
            val isSelected = index in selected
            FilterChip(
                label = label,
                selected = isSelected,
                onSelectedChange = {
                    onSelectionChange(if (isSelected) selected - index else selected + index)
                }
            )
        }
    }
}

@Composable
fun AppChipMultiWithTitle(
    title: String,
    items: List<String>,
    selected: Set<Int>,
    onSelectionChange: (Set<Int>) -> Unit,
    modifier: Modifier = Modifier,
    hGap: Dp = SpacingToken.tiny,
    vGap: Dp = SpacingToken.none,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        AppText(
            text = title,
            textStyle = AppTypography.bodySmall,
            fontWeight = FontWeight.Light,
            modifier = Modifier,
            textColor = MaterialTheme.textColors.secondary
        )

        MultiSelectChipGroup(
            items = items,
            selected = selected,
            onSelectionChange = {
                onSelectionChange(it)
            },
            hGap = hGap,
            vGap = vGap
        )
    }
}

@Composable
fun AppChipSingleWithTitle() {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        var single by remember { mutableIntStateOf(0) }
        var multi by remember { mutableStateOf(setOf(1, 3)) }

        Text("Single Select")
        SingleSelectChipGroup(
            items = listOf("Bus", "Train", "Launch"),
            selectedIndex = single,
            onSelectedIndexChange = { single = it }
        )

        Text("Multi Select")
        MultiSelectChipGroup(
            items = listOf("AC", "Sleeper", "Seater", "Economy", "Family"),
            selected = multi,
            onSelectionChange = { multi = it }
        )
    }
}

/* =========================
   Preview / Usage
   ========================= */

@Preview(showBackground = true)
@Composable
private fun Preview_BaseChip_Usage() {
    MaterialTheme {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            var single by remember { mutableIntStateOf(0) }
            var multi by remember { mutableStateOf(setOf(1, 3)) }

            Text("Single Select")
            SingleSelectChipGroup(
                items = listOf("Bus", "Train", "Launch"),
                selectedIndex = single,
                onSelectedIndexChange = { single = it }
            )

            Text("Multi Select")
            MultiSelectChipGroup(
                items = listOf("AC", "Sleeper", "Seater", "Economy", "Family"),
                selected = multi,
                onSelectionChange = { multi = it }
            )
        }
    }
}
