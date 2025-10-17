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
        primary = ColorPalette.Gray950,
        secondary = ColorPalette.Gray500,
        tertiary = ColorPalette.Gray200,
    )
}

fun dividerColorsForLight() = DividerColors(
    primary = ColorPalette.Gray900,
    secondary = ColorPalette.Gray600,
    tertiary = ColorPalette.Gray500,
)

fun dividerColorsForDark() = DividerColors(
    primary = ColorPalette.Gray100,
    secondary = ColorPalette.Gray400,
    tertiary = ColorPalette.Gray500,
)