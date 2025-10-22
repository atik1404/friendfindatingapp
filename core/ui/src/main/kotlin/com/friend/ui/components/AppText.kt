package com.friend.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.friend.designsystem.theme.textColors

@Composable
fun AppText(
    text: String,
    modifier: Modifier,
    alignment: TextAlign = TextAlign.Start,
    maxLines: Int = 1,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    fontWeight: FontWeight = FontWeight.Normal,
    textColor: Color = MaterialTheme.textColors.primary,
    overflow: TextOverflow = TextOverflow.Clip,
    onClick: (() -> Unit)? = null,
    leading: (@Composable (() -> Unit))? = null,
    trailing: (@Composable (() -> Unit))? = null,
) {
    return Row(
        modifier = modifier
    ) {
        if (leading != null) {
            leading()
        }
        Text(
            text = text,
            modifier = modifier
                .then(
                    if (onClick != null) {
                        Modifier.clickable { onClick() }
                    } else {
                        Modifier
                    }
                ),
            style = textStyle.copy(color = textColor, fontWeight = fontWeight),
            textAlign = alignment,
            maxLines = maxLines,
            overflow = overflow,
        )
        if (trailing != null) {
            trailing()
        }
    }
}