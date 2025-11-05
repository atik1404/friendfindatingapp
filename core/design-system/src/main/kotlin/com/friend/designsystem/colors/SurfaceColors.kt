package com.friend.designsystem.colors

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class SurfaceColors(
    val primary: Color,
    val secondary: Color,
    val tertiary: Color,
    val selectedItemColor: Color,
    val grayLight: Color,
    val yellowLight: Color,

)

val LocalSurfaceColors = staticCompositionLocalOf {
    SurfaceColors(
        primary = ColorPalette.Blue400,
        secondary = ColorPalette.Blue100,
        tertiary = ColorPalette.Blue200,
        selectedItemColor = ColorPalette.Blue400,
        grayLight = ColorPalette.Gray100,
        yellowLight = ColorPalette.Gold100,
    )
}

fun surfaceColorsForLight() = SurfaceColors(
    primary = ColorPalette.Blue400,
    secondary = ColorPalette.Blue100,
    tertiary = ColorPalette.Blue200,
    selectedItemColor = ColorPalette.Blue400,
    grayLight = ColorPalette.Gray100,
    yellowLight = ColorPalette.Gold100,
)

fun surfaceColorsForDark() = SurfaceColors(
    primary = ColorPalette.Blue400,
    secondary = ColorPalette.Blue100,
    tertiary = ColorPalette.Blue200,
    selectedItemColor = ColorPalette.Blue400,
    grayLight = ColorPalette.Gray100,
    yellowLight = ColorPalette.Gold100,
)