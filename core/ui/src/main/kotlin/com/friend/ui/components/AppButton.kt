package com.friend.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.friend.designsystem.theme.buttonColors
import com.friend.designsystem.theme.textColors

@Composable
fun AppButton(
    modifier: Modifier = Modifier,
    text: String? = null,
    onClick: () -> Unit,
    leading: (@Composable (() -> Unit))? = null,
    trailing: (@Composable (() -> Unit))? = null,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    variant: AppButtonVariant = AppButtonVariant.Primary,
    size: AppButtonSize = AppButtonSize.Medium,
    shape: Shape = ButtonDefaults.shape,
    colors: ButtonColors = variant.toColors(),
    elevation: ButtonElevation? = null,
    textStyle: TextStyle = size.textStyle(),
    contentPadding: PaddingValues = size.contentPadding(),
    content: (@Composable RowScope.() -> Unit)? = null,
) {
    val clickable = enabled && !isLoading
    val actualOnClick = if (clickable) onClick else { }

    val buttonComposable: @Composable (
        onClick: () -> Unit,
        modifier: Modifier,
        enabled: Boolean,
        shape: Shape,
        colors: ButtonColors,
        elevation: ButtonElevation?,
        contentPadding: PaddingValues,
        content: @Composable RowScope.() -> Unit
    ) -> Unit = when (variant) {
        AppButtonVariant.Outline -> { oc, m, e, s, c, el, pad, cnt ->
            OutlinedButton(
                onClick = oc,
                modifier = m.heightIn(min = size.minHeight),
                enabled = e,
                shape = s,
                colors = c,
                elevation = el,
                contentPadding = pad,
                content = cnt
            )
        }

        else -> { oc, m, e, s, c, el, pad, cnt ->
            Button(
                onClick = oc,
                modifier = m.heightIn(min = size.minHeight),
                enabled = e,
                shape = s,
                colors = c,
                elevation = el,
                contentPadding = pad,
                content = cnt
            )
        }
    }

    buttonComposable(
        onClick,
        modifier,
        clickable,
        shape,
        colors,
        elevation,
        contentPadding
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                strokeWidth = 2.dp,
                modifier = Modifier
                    .size(size.progressSize)
                    .align(Alignment.CenterVertically)
            )
            if (text != null || content != null || leading != null || trailing != null) {
                Spacer(Modifier.width(size.iconSpacing))
            }
        } else if (leading != null) {
            leading()
            if (text != null || content != null) Spacer(Modifier.width(size.iconSpacing))
        }

        when {
            content != null -> content()
            text != null -> Text(
                text = text,
                style = textStyle,
            )
        }

        if (!isLoading && trailing != null) {
            if (text != null || content != null) Spacer(Modifier.width(size.iconSpacing))
            trailing()
        }
    }
}

// ----- Variants & Sizes -----
enum class AppButtonVariant {
    Primary,        // uses design system primaryButton
    Outline;        // outlined style

    @Composable
    fun toColors(): ButtonColors = when (this) {
        Primary -> MaterialTheme.buttonColors.primaryButton
        Outline -> MaterialTheme.buttonColors.outlineButton
    }
}

enum class AppButtonSize(
    val minHeight: Dp,
    val horizontalPadding: Dp,
    val verticalPadding: Dp,
    val iconSpacing: Dp,
    val progressSize: Dp,
) {
    Small(
        minHeight = 36.dp,
        horizontalPadding = 12.dp,
        verticalPadding = 8.dp,
        iconSpacing = 6.dp,
        progressSize = 16.dp
    ),
    Medium(
        minHeight = FortyEight,
        horizontalPadding = 16.dp,
        verticalPadding = 12.dp,
        iconSpacing = 8.dp,
        progressSize = 18.dp
    ),
    Large(
        minHeight = 56.dp,
        horizontalPadding = 20.dp,
        verticalPadding = 14.dp,
        iconSpacing = 10.dp,
        progressSize = 20.dp
    );

    @Composable
    fun contentPadding(): PaddingValues =
        PaddingValues(horizontal = horizontalPadding, vertical = verticalPadding)

    @Composable
    fun textStyle(): TextStyle = when (this) {
        Small -> MaterialTheme.typography.labelMedium
        Medium -> MaterialTheme.typography.labelLarge
        Large -> MaterialTheme.typography.titleSmall
    }
}

// Helper constant to keep magic numbers readable (optional)
private val FortyEight = 48.dp

// ----- Convenience wrappers (optional) -----

@Composable
fun AppFilledButton(
    modifier: Modifier = Modifier,
    buttonText: String,
    onClick: () -> Unit,
    leading: (@Composable (() -> Unit))? = null,
    trailing: (@Composable (() -> Unit))? = null,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    size: AppButtonSize = AppButtonSize.Medium,
    shape: Shape = ButtonDefaults.shape,
    variant: AppButtonVariant = AppButtonVariant.Primary,
) = AppButton(
    modifier = modifier,
    text = buttonText,
    onClick = onClick,
    leading = leading,
    trailing = trailing,
    enabled = enabled,
    isLoading = isLoading,
    variant = variant,
    size = size,
    shape = shape
)
