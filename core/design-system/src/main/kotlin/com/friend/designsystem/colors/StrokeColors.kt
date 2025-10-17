package com.friend.designsystem.colors

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class StrokesColors(
    val primary: Color,
    val secondary: Color,
    val tertiary: Color,
)

val LocalStrokeColors = staticCompositionLocalOf {
    StrokesColors(
        primary = ColorPalette.Gray950,
        secondary = ColorPalette.Gray500,
        tertiary = ColorPalette.Gray200,
    )
}

fun strokesColorsForLight() = StrokesColors(
    primary = ColorPalette.Gray900,
    secondary = ColorPalette.Gray600,
    tertiary = ColorPalette.Gray500,
)

fun strokesColorsForDark() = StrokesColors(
    primary = ColorPalette.Gray100,
    secondary = ColorPalette.Gray400,
    tertiary = ColorPalette.Gray500,
)