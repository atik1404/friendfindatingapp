package com.friend.designsystem.colors

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class DividerColors(
    val primary: Color,
    val secondary: Color,
    val tertiary: Color,
)

val LocalDividerColors = staticCompositionLocalOf {
    DividerColors(
        primary = ColorPalette.Gray200,
        secondary = ColorPalette.Gray300,
        tertiary = ColorPalette.Gray400,
    )
}

fun dividerColorsForLight() = DividerColors(
    primary = ColorPalette.Gray200,
    secondary = ColorPalette.Gray300,
    tertiary = ColorPalette.Gray400,
)

fun dividerColorsForDark() = DividerColors(
    primary = ColorPalette.Gray200,
    secondary = ColorPalette.Gray300,
    tertiary = ColorPalette.Gray400,
)