package com.friend.designsystem.colors

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class IconColors(
    val primary: Color,
    val secondary: Color,
    val tertiary: Color,
)

val LocalIconColors = staticCompositionLocalOf {
    IconColors(
        primary = ColorPalette.Gray950,
        secondary = ColorPalette.Gray500,
        tertiary = ColorPalette.Gray200,
    )
}

fun iconsColorsForLight() = IconColors(
    primary = ColorPalette.Gray900,
    secondary = ColorPalette.Gray600,
    tertiary = ColorPalette.Gray500,
)

fun iconsColorsForDark() = IconColors(
    primary = ColorPalette.Gray100,
    secondary = ColorPalette.Gray400,
    tertiary = ColorPalette.Gray500,
)