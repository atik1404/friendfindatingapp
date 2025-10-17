package com.friend.designsystem.colors

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class SurfaceColors(
    val primary: Color,
    val secondary: Color,
    val tertiary: Color,
)

val LocalSurfaceColors = staticCompositionLocalOf {
    SurfaceColors(
        primary = ColorPalette.Gray950,
        secondary = ColorPalette.Gray500,
        tertiary = ColorPalette.Gray200,
    )
}

fun surfaceColorsForLight() = SurfaceColors(
    primary = ColorPalette.Gray900,
    secondary = ColorPalette.Gray600,
    tertiary = ColorPalette.Gray500,
)

fun surfaceColorsForDark() = SurfaceColors(
    primary = ColorPalette.Gray100,
    secondary = ColorPalette.Gray400,
    tertiary = ColorPalette.Gray500,
)