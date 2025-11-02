package com.friend.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import com.friend.designsystem.theme.textColors

@Composable
fun AppText(
    text: String,
    modifier: Modifier = Modifier,
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
            .then(
                if (onClick != null) {
                    Modifier.clickable { onClick() }
                } else {
                    Modifier
                }
            )
    ) {
        if (leading != null) {
            leading()
        }
        Text(
            text = text,
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

data class ColoredTextSegment(
    val text: String,
    val color: Color,
    val style: TextStyle? = null,
    val addSpace: Boolean = false,
    val onClick: (() -> Unit)? = null,
    val underlineWhenClickable: Boolean = false,
)

@Composable
fun MultiColorText(
    segments: List<ColoredTextSegment>,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle.Default
) {
    // Build the annotated string and tag clickable ranges with the segment index
    val tag = "segment"
    val annotated = buildAnnotatedString {
        segments.forEachIndexed { index, segment ->
            val clickable = segment.onClick != null

            if (clickable) {
                // Push an annotation so we can detect clicks inside this range
                pushStringAnnotation(tag = tag, annotation = index.toString())
            }

            withStyle(
                SpanStyle(
                    color = segment.color,
                    fontSize = segment.style?.fontSize ?: textStyle.fontSize,
                    fontWeight = segment.style?.fontWeight ?: textStyle.fontWeight,
                    textDecoration = when {
                        clickable && segment.underlineWhenClickable ->
                            (segment.style?.textDecoration ?: textStyle.textDecoration)
                                ?: androidx.compose.ui.text.style.TextDecoration.Underline

                        else -> segment.style?.textDecoration ?: textStyle.textDecoration
                    }
                )
            ) {
                append(segment.text)
                if (segment.addSpace) append(" ")
            }

            if (clickable) {
                pop() // end annotation
            }
        }
    }

    var layoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

    Text(
        text = annotated,
        style = textStyle,
        modifier = modifier.pointerInput(annotated) {
            detectTapGestures { pos: Offset ->
                val layout = layoutResult ?: return@detectTapGestures
                val offset = layout.getOffsetForPosition(pos)
                annotated.getStringAnnotations(tag, offset, offset)
                    .firstOrNull()
                    ?.let { ann ->
                        ann.item.toIntOrNull()
                            ?.let { idx -> segments.getOrNull(idx)?.onClick?.invoke() }
                    }
            }
        },
        onTextLayout = { layoutResult = it }
    )
}
