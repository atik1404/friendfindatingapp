package com.friend.designsystem.colors

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class BackgroundColors(
    val primary: Color,
    val secondary: Color,
    val tertiary: Color,
    val surface: Color,
    val white: Color,
    val black: Color,
)

val LocalBackgroundColors = staticCompositionLocalOf {
    BackgroundColors(
        primary = ColorPalette.Gray50,
        secondary = ColorPalette.Gray200,
        tertiary = ColorPalette.Gray300,
        surface = ColorPalette.Gray100,
        white = ColorPalette.White,
        black = ColorPalette.Gray700,
    )
}

fun backgroundColorsForLight() = BackgroundColors(
    primary = ColorPalette.Gray50,
    secondary = ColorPalette.Gray200,
    tertiary = ColorPalette.Gray300,
    surface = ColorPalette.Gray100,
    white = ColorPalette.White,
    black = ColorPalette.Gray700,
)

fun backgroundColorsForDark() = BackgroundColors(
    primary = ColorPalette.Gray800,
    secondary = ColorPalette.Gray600,
    tertiary = ColorPalette.Gray400,
    surface = ColorPalette.Gray300,
    white = ColorPalette.White,
    black = ColorPalette.Gray700,
)