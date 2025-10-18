package com.friend.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

enum class AppTextFieldStyle { Filled, Outlined }

@Composable
fun AppTextField(
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
    isError: Boolean = false,
    errorText: String? = null,
    supportingText: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    shape: Shape = TextFieldDefaults.shape,
    onValueChange: (String) -> Unit, // keep last for trailing-lambda calls
) {
    var pwdVisible by remember { mutableStateOf(false) }
    val vt: VisualTransformation =
        if (isPassword && !pwdVisible) PasswordVisualTransformation() else VisualTransformation.None

    val leading: (@Composable (() -> Unit))? = leadingIcon?.let {
        { Icon(imageVector = it, contentDescription = null) }
    }

    val trailing: @Composable (() -> Unit) = {
        Row {
            when {
                isPassword -> {
                    IconButton(onClick = { pwdVisible = !pwdVisible }) {
                        Icon(
                            imageVector = if (pwdVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (pwdVisible) "Hide password" else "Show password"
                        )
                    }
                }

                trailingIcon != null && onTrailingClick != null -> {
                    IconButton(onClick = onTrailingClick) {
                        Icon(imageVector = trailingIcon, contentDescription = null)
                    }
                }

                clearable && value.isNotEmpty() -> {
                    IconButton(onClick = { onValueChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }

    val labelComposable: (@Composable (() -> Unit))? = label?.let { { Text(it) } }
    val placeholderComposable: (@Composable (() -> Unit))? = placeholder?.let { { Text(it) } }

    val supportingComposable: (@Composable (() -> Unit))? = when {
        isError && !errorText.isNullOrBlank() -> {
            { Text(errorText, color = MaterialTheme.colorScheme.error) }
        }

        !supportingText.isNullOrBlank() -> {
            { Text(supportingText!!) }
        }

        else -> null
    }

    when (style) {
        AppTextFieldStyle.Filled -> TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier,
            label = labelComposable,
            placeholder = placeholderComposable,
            leadingIcon = leading,
            trailingIcon = trailing,
            singleLine = singleLine,
            maxLines = maxLines,
            enabled = enabled,
            readOnly = readOnly,
            isError = isError,
            visualTransformation = vt,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            shape = shape,
            supportingText = supportingComposable
        )

        AppTextFieldStyle.Outlined -> OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier,
            label = labelComposable,
            placeholder = placeholderComposable,
            leadingIcon = leading,
            trailingIcon = trailing,
            singleLine = singleLine,
            maxLines = maxLines,
            enabled = enabled,
            readOnly = readOnly,
            isError = isError,
            visualTransformation = vt,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            shape = shape,
            supportingText = supportingComposable
        )
    }
}
