package com.friend.designsystem.colors

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class SurfaceColors(
    val primary: Color,
    val secondary: Color,
    val tertiary: Color,
    val selectedItemColor: Color,
)

val LocalSurfaceColors = staticCompositionLocalOf {
    SurfaceColors(
        primary = ColorPalette.Blue400,
        secondary = ColorPalette.Blue100,
        tertiary = ColorPalette.Blue200,
        selectedItemColor = ColorPalette.Blue400,
    )
}

fun surfaceColorsForLight() = SurfaceColors(
    primary = ColorPalette.Blue400,
    secondary = ColorPalette.Blue100,
    tertiary = ColorPalette.Blue200,
    selectedItemColor = ColorPalette.Blue400,
)

fun surfaceColorsForDark() = SurfaceColors(
    primary = ColorPalette.Blue400,
    secondary = ColorPalette.Blue100,
    tertiary = ColorPalette.Blue200,
    selectedItemColor = ColorPalette.Blue400,
)