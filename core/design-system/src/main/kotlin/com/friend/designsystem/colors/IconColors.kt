package com.friend.designsystem.colors

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class IconColors(
    val primary: Color,
    val secondary: Color,
    val tertiary: Color,
    val yellow: Color
)

val LocalIconColors = staticCompositionLocalOf {
    IconColors(
        primary = ColorPalette.Gray950,
        secondary = ColorPalette.Gray500,
        tertiary = ColorPalette.Gray200,
        yellow = ColorPalette.Gold400,
    )
}

fun iconsColorsForLight() = IconColors(
    primary = ColorPalette.Gray900,
    secondary = ColorPalette.Gray600,
    tertiary = ColorPalette.Gray500,
    yellow = ColorPalette.Gold400,
)

fun iconsColorsForDark() = IconColors(
    primary = ColorPalette.Gray100,
    secondary = ColorPalette.Gray400,
    tertiary = ColorPalette.Gray500,
    yellow = ColorPalette.Gold400,
)