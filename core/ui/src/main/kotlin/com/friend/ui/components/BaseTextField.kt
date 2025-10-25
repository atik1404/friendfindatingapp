package com.friend.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.friend.designsystem.colors.TextFieldColors
import com.friend.designsystem.spacing.RadiusToken
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.theme.textColors
import com.friend.designsystem.theme.textFieldColors
import com.friend.designsystem.typography.AppTypography

enum class AppTextFieldStyle { Filled, Outlined }

@Composable
private fun BaseTextField(
    value: String,
    modifier: Modifier = Modifier,
    style: AppTextFieldStyle = AppTextFieldStyle.Outlined,
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onTrailingClick: (() -> Unit)? = null,
    clearable: Boolean = false,
    isPassword: Boolean = false,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    maxLength: Int? = null,
    isError: Boolean = false,
    errorText: String? = null,
    supportingText: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    shape: Shape = TextFieldDefaults.shape,
    onValueChange: (String) -> Unit,
    colors: TextFieldColors,
) {
    var pwdVisible by remember { mutableStateOf(false) }
    val vt: VisualTransformation =
        if (isPassword && !pwdVisible) PasswordVisualTransformation() else VisualTransformation.None

    val leading: (@Composable (() -> Unit))? = leadingIcon?.let {
        { Icon(imageVector = it, contentDescription = null) }
    }

    val trailingContent: (@Composable () -> Unit)? = when {
        isPassword -> {
            {
                IconButton(onClick = { pwdVisible = !pwdVisible }) {
                    Icon(
                        imageVector = if (pwdVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (pwdVisible) "Hide password" else "Show password"
                    )
                }
            }
        }

        trailingIcon != null && onTrailingClick != null -> {
            {
                IconButton(onClick = onTrailingClick) {
                    Icon(imageVector = trailingIcon, contentDescription = null)
                }
            }
        }

        clearable && value.isNotEmpty() -> {
            {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear",
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        else -> null
    }


    val labelComposable: (@Composable (() -> Unit))? = label?.let { { Text(it) } }
    val placeholderComposable: (@Composable (() -> Unit))? =
        placeholder?.let {
            {
                AppText(
                    it,
                    textColor = MaterialTheme.textColors.secondary,
                    modifier = Modifier,
                    fontWeight = FontWeight.Light
                )
            }
        }

    val supportingComposable: (@Composable (() -> Unit))? = when {
        isError && !errorText.isNullOrBlank() -> {
            { Text(errorText, color = MaterialTheme.colorScheme.error) }
        }

        !supportingText.isNullOrBlank() -> {
            { Text(supportingText) }
        }

        else -> null
    }

    val cappedChange: (String) -> Unit = { new ->
        when {
            maxLength == null -> onValueChange(new)
            new.length <= maxLength -> onValueChange(new)
            else -> onValueChange(new.take(maxLength))
        }
    }

    when (style) {
        AppTextFieldStyle.Filled -> TextField(
            value = value,
            onValueChange = cappedChange,
            modifier = modifier,
            label = labelComposable,
            placeholder = placeholderComposable,
            leadingIcon = leading,
            trailingIcon = trailingContent,
            singleLine = singleLine,
            maxLines = maxLines,
            enabled = enabled,
            readOnly = readOnly,
            isError = isError,
            visualTransformation = vt,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            shape = shape,
            supportingText = supportingComposable,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = colors.focusedBorderColor,
                unfocusedIndicatorColor = colors.borderColor,
                disabledIndicatorColor = colors.borderColor,
                errorIndicatorColor = colors.errorBorderColor,
                focusedLeadingIconColor = colors.leadingIconColor,
                unfocusedLeadingIconColor = colors.leadingIconColor,
                focusedTrailingIconColor = colors.leadingIconColor,
                unfocusedTrailingIconColor = colors.leadingIconColor,
                focusedTextColor = colors.textColor,
                unfocusedTextColor = colors.textColor,
                focusedLabelColor = colors.labelColor,
                unfocusedLabelColor = colors.labelColor,
                focusedContainerColor = colors.backgroundColor,
                unfocusedContainerColor = colors.backgroundColor,
            )
        )

        AppTextFieldStyle.Outlined -> OutlinedTextField(
            value = value,
            onValueChange = cappedChange,
            modifier = modifier,
            label = labelComposable,
            placeholder = placeholderComposable,
            leadingIcon = leading,
            trailingIcon = trailingContent,
            singleLine = singleLine,
            maxLines = maxLines,
            enabled = enabled,
            readOnly = readOnly,
            isError = isError,
            visualTransformation = vt,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            shape = shape,
            supportingText = supportingComposable,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = colors.focusedBorderColor,
                unfocusedIndicatorColor = colors.borderColor,
                disabledIndicatorColor = colors.borderColor,
                errorIndicatorColor = colors.errorBorderColor,
                focusedLeadingIconColor = colors.leadingIconColor,
                unfocusedLeadingIconColor = colors.leadingIconColor,
                focusedTrailingIconColor = colors.leadingIconColor,
                unfocusedTrailingIconColor = colors.leadingIconColor,
                focusedTextColor = colors.textColor,
                unfocusedTextColor = colors.textColor,
                focusedLabelColor = colors.labelColor,
                unfocusedLabelColor = colors.labelColor,
                focusedContainerColor = colors.backgroundColor,
                unfocusedContainerColor = colors.backgroundColor,
            )
        )
    }
}


@Composable
fun AppOutlineTextField(
    modifier: Modifier = Modifier,
    text: String,
    title: String,
    placeholder: String,
    error: String? = null,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    isPassword: Boolean = false,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    maxLines: Int = 1,
    maxLength: Int? = null,
    isReadOnly: Boolean = false,
    onClickListener: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        AppText(
            text = title,
            textStyle = AppTypography.bodySmall,
            fontWeight = FontWeight.Light,
            modifier = Modifier,
            textColor = MaterialTheme.textColors.secondary
        )
        Spacer(Modifier.height(SpacingToken.micro))

        Box(
            modifier = Modifier.fillMaxWidth()
        ){
            BaseTextField(
                value = text,
                modifier = Modifier.fillMaxWidth(),
                placeholder = placeholder,
                style = AppTextFieldStyle.Outlined,
                shape = RoundedCornerShape(RadiusToken.large),
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                onValueChange = onValueChange,
                colors = MaterialTheme.textFieldColors.outlinedTextField,
                maxLines = maxLines,
                keyboardActions = keyboardActions,
                keyboardOptions = keyboardOptions,
                isPassword = isPassword,
                isError = error != null,
                maxLength = maxLength,
                readOnly = isReadOnly
            )

            // Invisible click layer ONLY when readOnly + callback provided
            if (isReadOnly && onClickListener != null) {
                Spacer(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable(
                            enabled = true,
                            indication = null, // no ripple over text field
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            onClickListener.invoke()
                        }
                )
            }
        }

        if (error != null) {
            Spacer(Modifier.height(SpacingToken.micro))
            AppText(
                text = error,
                textStyle = AppTypography.labelSmall,
                fontWeight = FontWeight.Light,
                textColor = MaterialTheme.textColors.error,
                modifier = Modifier,
            )
        }
    }
}

@Composable
fun AppFilledTextField() {

}

@Composable
@PreviewLightDark
fun AppTextFieldPreview() {
    Column {
        AppOutlineTextField(
            text = "",
            title = "Outlined TextField",
            placeholder = "Placeholder",
            onValueChange = {},
        )
    }
}