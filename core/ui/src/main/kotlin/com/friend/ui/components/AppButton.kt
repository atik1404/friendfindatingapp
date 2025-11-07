package com.friend.ui.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Female
import androidx.compose.material.icons.rounded.Male
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.friend.designsystem.spacing.RadiusToken
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPaddingVertical
import com.friend.designsystem.theme.buttonColors
import com.friend.designsystem.theme.strokeColors
import com.friend.designsystem.theme.surfaceColors
import com.friend.designsystem.theme.textColors
import com.friend.designsystem.typography.AppTypography
import com.friend.ui.preview.LightDarkPreview
import com.friend.designsystem.R as Res

/* -------------------------------------------------------------------------- */
/*  Shared internal content for all buttons                                   */
/* -------------------------------------------------------------------------- */

@Composable
private fun ButtonContent(
    modifier: Modifier,
    text: String,
    leadingIcon: ImageVector?,
    trailingIcon: ImageVector?,
    iconSpacing: Dp,
    iconTint: Color,
    textColor: Color,
    fontWeight: FontWeight = FontWeight.Medium,
    isLoading: Boolean = false,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (leadingIcon != null) {
            Icon(imageVector = leadingIcon, contentDescription = null, tint = iconTint)
            Spacer(Modifier.width(iconSpacing))
        }
        AppText(
            text = text,
            modifier = modifier,
            textColor = textColor,
            fontWeight = fontWeight
        )
        if (trailingIcon != null) {
            Spacer(Modifier.width(iconSpacing))
            Icon(imageVector = trailingIcon, contentDescription = null, tint = iconTint)
        }

        if (isLoading) {
            Spacer(modifier = Modifier.width(SpacingToken.tiny))
            CircularProgressIndicator(
                strokeWidth = 2.dp,
                modifier = Modifier.size(18.dp),
                color = textColor
            )
        }
    }
}

/* Creates a debounced onClick that ignores rapid taps within [debounceMillis]. */
var lastClickAt = 0L
@Composable
private fun rememberDebouncedClick(
    debounceMillis: Long = 1700L,
    onClick: () -> Unit
): () -> Unit {
    return {
        val now = System.currentTimeMillis()
        val isClickAllowed = now - lastClickAt > debounceMillis
        Log.d("buttonClicked", "isClickAllowed: $isClickAllowed")
        if (isClickAllowed) {
            lastClickAt = now
            onClick()
        }
    }
}

/* -------------------------------------------------------------------------- */
/*  Elevated Button                                                            */
/* -------------------------------------------------------------------------- */

@Composable
fun AppElevatedButton(
    text: String,                         // required
    onClick: () -> Unit,                  // required
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    shape: Shape = ButtonDefaults.shape,
    colors: ButtonColors = MaterialTheme.buttonColors.primaryButton,
    elevation: ButtonElevation? = ButtonDefaults.elevatedButtonElevation(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    iconTint: Color = LocalContentColor.current,
    iconSpacing: Dp = 8.dp,
) {
    val textColor = MaterialTheme.textColors.white
    val debounced = rememberDebouncedClick(onClick = onClick)

    ElevatedButton(
        onClick = debounced,
        modifier = modifier,
        enabled = enabled && !isLoading,
        shape = shape,
        colors = colors,
        elevation = elevation,
        contentPadding = contentPadding,
    ) {
        ButtonContent(
            text = text,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            iconSpacing = iconSpacing,
            iconTint = iconTint,
            textColor = textColor,
            isLoading = isLoading,
            modifier = Modifier.appPaddingVertical(SpacingToken.micro)
        )
    }
}

/* -------------------------------------------------------------------------- */
/*  Outlined Button                                                            */
/* -------------------------------------------------------------------------- */

@Composable
fun AppOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    shape: Shape = ButtonDefaults.shape,
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(),
    border: BorderStroke? = ButtonDefaults.outlinedButtonBorder(enabled),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    iconTint: Color = LocalContentColor.current,
    iconSpacing: Dp = 8.dp,
) {
    val textColor = MaterialTheme.textColors.primary
    val debounced = rememberDebouncedClick(onClick = onClick)

    OutlinedButton(
        onClick = debounced,
        modifier = modifier,
        enabled = enabled && !isLoading,
        shape = shape,
        colors = colors,
        border = border,
        contentPadding = contentPadding,
    ) {
        ButtonContent(
            text = text,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            iconSpacing = iconSpacing,
            iconTint = iconTint,
            textColor = textColor,
            isLoading = isLoading,
            modifier = Modifier.appPaddingVertical(SpacingToken.micro)
        )
    }
}

/* -------------------------------------------------------------------------- */
/*  Text Button                                                                */
/* -------------------------------------------------------------------------- */

@Composable
fun AppTextButton(
    text: String,
    textColor: Color = MaterialTheme.textColors.primary,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.shape,
    colors: ButtonColors = ButtonDefaults.textButtonColors(),
    contentPadding: PaddingValues = ButtonDefaults.TextButtonContentPadding,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    iconTint: Color = LocalContentColor.current,
    iconSpacing: Dp = SpacingToken.none,
) {
    val debounced = rememberDebouncedClick(onClick = onClick)

    TextButton(
        onClick = debounced,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = colors,
        contentPadding = contentPadding,
    ) {
        ButtonContent(
            text = text,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            iconSpacing = iconSpacing,
            iconTint = iconTint,
            textColor = textColor,
            modifier = modifier
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleChoiceSegmentsWithIcons(
    title: String? = null,
    options: List<Pair<ImageVector, String>>,
    selectedIndex: Int = -1,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        title?.let {
            AppText(
                text = title,
                textStyle = AppTypography.bodySmall,
                fontWeight = FontWeight.Light,
                modifier = Modifier,
                textColor = MaterialTheme.textColors.secondary
            )
            Spacer(Modifier.height(SpacingToken.micro))
        }
        SingleChoiceSegmentedButtonRow(
            Modifier.fillMaxWidth()
        ) {
            options.forEachIndexed { index, (icon, label) ->
                SegmentedButton(
                    selected = index == selectedIndex,
                    onClick = { onSelected(index) },
                    shape = when (index) {
                        0 -> {
                            RoundedCornerShape(
                                topStart = RadiusToken.large,
                                bottomStart = RadiusToken.large,
                                topEnd = 0.dp,
                                bottomEnd = 0.dp
                            )
                        }
                        options.size -1 -> {
                            RoundedCornerShape(
                                topStart = 0.dp,
                                bottomStart = 0.dp,
                                topEnd = RadiusToken.large,
                                bottomEnd = RadiusToken.large
                            )
                        }
                        else -> {
                            RoundedCornerShape(0.dp)
                        }
                    },
                    colors = SegmentedButtonDefaults.colors(
                        activeContainerColor = MaterialTheme.surfaceColors.selectedItemColor,
                        inactiveContainerColor = Color.White,
                        activeContentColor = MaterialTheme.textColors.white,
                        inactiveContentColor = MaterialTheme.textColors.primary,
                        activeBorderColor = MaterialTheme.strokeColors.primary,
                        inactiveBorderColor = MaterialTheme.strokeColors.primary,
                    ),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.appPaddingVertical(SpacingToken.micro)
                    ) {
                        Spacer(Modifier.width(SpacingToken.tiny))
                        Text(label)
                        Spacer(Modifier.width(SpacingToken.tiny))
                        Icon(icon, contentDescription = null)
                    }
                }
//                if(index < options.size -1) {
//                    Spacer(Modifier.width(SpacingToken.micro))
//                }
            }
        }
    }
}

@Composable
fun AppIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    vectorIcon: ImageVector? = null,   // XML vector via ImageVector
    resourceIcon: Int? = null,         // drawable/mipmap id (png/jpg/xml)
    contentDescription: String? = null,
    iconSize: Dp = 24.dp,
    tint: Color = LocalContentColor.current,
    preserveOriginalColors: Boolean = false, // set true to ignore tint
) {
    require(vectorIcon != null || resourceIcon != null) {
        "Provide either vectorIcon or resourceIcon"
    }

    val cd = contentDescription ?: stringResource(Res.string.msg_image_content_description)
    val effectiveTint = if (preserveOriginalColors) Color.Unspecified else tint

    IconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled
    ) {
        when {
            vectorIcon != null -> {
                Icon(
                    imageVector = vectorIcon,
                    contentDescription = cd,
                    modifier = Modifier.size(iconSize),
                    tint = effectiveTint
                )
            }

            resourceIcon != null -> {
                Icon(
                    painter = painterResource(id = resourceIcon),
                    contentDescription = cd,
                    modifier = Modifier.size(iconSize),
                    tint = effectiveTint
                )
            }
        }
    }
}



@Composable
@LightDarkPreview
fun LoginScreenPreview() {
    SingleChoiceSegmentsWithIcons(
        title = "Interested In*",
        options = listOf(
            Pair(Icons.Rounded.Male, "Male"),
            Pair(Icons.Rounded.Female, "Female"),
        ),
        selectedIndex = 1,
        onSelected = { }
    )
}