package com.friend.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.friend.designsystem.theme.buttonColors

/* -------------------------------------------------------------------------- */
/*  Shared internal content for all buttons                                   */
/* -------------------------------------------------------------------------- */

@Composable
private fun ButtonContent(
    text: String,
    leadingIcon: ImageVector?,
    trailingIcon: ImageVector?,
    iconSpacing: Dp,
    iconTint: Color
) {
    Row {
        if (leadingIcon != null) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = iconTint
            )
            Spacer(Modifier.width(iconSpacing))
        }

        Text(text)

        if (trailingIcon != null) {
            Spacer(Modifier.width(iconSpacing))
            Icon(
                imageVector = trailingIcon,
                contentDescription = null,
                tint = iconTint
            )
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
    shape: Shape = ButtonDefaults.shape,
    colors: ButtonColors = MaterialTheme.buttonColors.primaryButton,
    elevation: ButtonElevation? = ButtonDefaults.elevatedButtonElevation(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    role: Role? = null,
    // Icon options
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    iconTint: Color = LocalContentColor.current,
    iconSpacing: Dp = 8.dp,
) {
    ElevatedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
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
            iconTint = iconTint
        )
    }
}

/* -------------------------------------------------------------------------- */
/*  Outlined Button                                                            */
/* -------------------------------------------------------------------------- */

@Composable
fun AppOutlinedButton(
    text: String,                         // required
    onClick: () -> Unit,                  // required
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.shape,
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(),
    border: BorderStroke? = ButtonDefaults.outlinedButtonBorder(enabled),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    role: Role? = null,
    // Icon options
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    iconTint: Color = LocalContentColor.current,
    iconSpacing: Dp = 8.dp,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
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
            iconTint = iconTint
        )
    }
}

/* -------------------------------------------------------------------------- */
/*  Text Button                                                                */
/* -------------------------------------------------------------------------- */

@Composable
fun AppTextButton(
    text: String,                         // required
    onClick: () -> Unit,                  // required
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.shape,
    colors: ButtonColors = ButtonDefaults.textButtonColors(),
    contentPadding: PaddingValues = ButtonDefaults.TextButtonContentPadding,
    role: Role? = null,
    // Icon options
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    iconTint: Color = LocalContentColor.current,
    iconSpacing: Dp = 8.dp,
) {
    TextButton(
        onClick = onClick,
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
            iconTint = iconTint
        )
    }
}