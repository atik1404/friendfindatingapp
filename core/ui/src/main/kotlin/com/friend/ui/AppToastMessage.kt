package com.friend.ui

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.view.setPadding
import kotlin.math.pow

enum class ToastStatus { SUCCESS, INFO, ERROR, WARNING, NEUTRAL }

object StatusToastPalette {
    @ColorInt
    val Success = 0xFF16A34A.toInt() // green-600

    @ColorInt
    val Info = 0xFF2563EB.toInt() // blue-600

    @ColorInt
    val Error = 0xFFDC2626.toInt() // red-600

    @ColorInt
    val Warning = 0xFFF59E0B.toInt() // amber-500

    @ColorInt
    val Neutral = 0xFF374151.toInt() // gray-700
}

private fun Context.dp(i: Int): Int =
    (i * resources.displayMetrics.density).toInt()

/** WCAG-ish luminance check to pick white/black text automatically */
@ColorInt
private fun bestOnColor(@ColorInt bg: Int): Int {
    val r = ((bg shr 16) and 0xFF) / 255.0
    val g = ((bg shr 8) and 0xFF) / 255.0
    val b = (bg and 0xFF) / 255.0
    fun ch(c: Double) = if (c <= 0.03928) c / 12.92 else ((c + 0.055) / 1.055).pow(2.4)
    val luminance = 0.2126 * ch(r) + 0.7152 * ch(g) + 0.0722 * ch(b)
    return if (luminance > 0.5) 0xFF111827.toInt() else 0xFFFFFFFF.toInt() // dark or white
}

@ColorInt
private fun colorFor(status: ToastStatus): Int = when (status) {
    ToastStatus.SUCCESS -> StatusToastPalette.Success
    ToastStatus.INFO -> StatusToastPalette.Info
    ToastStatus.ERROR -> StatusToastPalette.Error
    ToastStatus.WARNING -> StatusToastPalette.Warning
    ToastStatus.NEUTRAL -> StatusToastPalette.Neutral
}

/**
 * Show a status toast with colored background.
 *
 * @param message The text to display
 * @param status  SUCCESS, INFO, ERROR, WARNING, NEUTRAL
 * @param duration Toast.LENGTH_SHORT or Toast.LENGTH_LONG
 * @param gravity Optional toast gravity (default bottom centered)
 * @param yOffsetPx Optional vertical offset in px (default 96dp)
 * @param icon Optional icon resource shown at start (pass null to hide)
 */
fun Context.showToastMessage(
    message: CharSequence,
    status: ToastStatus = ToastStatus.NEUTRAL,
    duration: Int = Toast.LENGTH_SHORT,
    gravity: Int = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL,
    yOffsetPx: Int = dp(96),
    @DrawableRes icon: Int? = null,
) {
    val backgroundColor = colorFor(status)
    val contentColor = bestOnColor(backgroundColor)

    // container
    val container = LinearLayout(this).apply {
        orientation = LinearLayout.HORIZONTAL
        setPadding(dp(12))
        background = GradientDrawable().apply {
            cornerRadius = dp(12).toFloat()
            setColor(backgroundColor)
        }
    }

    // optional icon
    if (icon != null) {
        val iv = ImageView(this).apply {
            setImageResource(icon)
            setColorFilter(contentColor)
            val size = dp(18)
            layoutParams = LinearLayout.LayoutParams(size, size).apply {
                rightMargin = dp(8)
            }
        }
        container.addView(iv)
    }

    // text
    val tv = TextView(this).apply {
        text = message
        setTextColor(contentColor)
        setLineSpacing(0f, 1.05f)
        typeface = Typeface.DEFAULT_BOLD
    }
    container.addView(tv)

    // toast
    Toast(this).apply {
        view = container
        setGravity(gravity, 0, yOffsetPx)
        this.duration = duration
    }.show()
}
