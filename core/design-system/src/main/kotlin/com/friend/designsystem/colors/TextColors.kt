package com.friend.designsystem.colors

// TextColors.kt
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.friend.designsystem.theme.Brand

data class TextColors(
    val primary: Color,          // default body/title on surfaces
    val secondary: Color,        // subdued body, hints
    val tertiary: Color,         // extra-subdued captions
    val inverse: Color,          // text on inverse surfaces
    val success: Color,          // success text
    val warning: Color,          // warning text
    val error: Color,            // error text
    val link: Color,             // hyperlinks
    val white: Color,             // hyperlinks
    val black: Color,             // hyperlinks
    val brand: Color
)

val LocalTextColors = staticCompositionLocalOf {
    TextColors(
        primary = ColorPalette.NavyBlue800,
        secondary = ColorPalette.Gray500,
        tertiary = ColorPalette.Gray200,
        inverse = ColorPalette.Gray50,
        success = ColorPalette.Green500,
        warning = ColorPalette.Yellow500,
        error = ColorPalette.Pink700,
        link = ColorPalette.Blue400,
        white = Color.White,
        black = Color.Black,
        brand = ColorPalette.Blue500
    )
}

fun textColorsForLight() = TextColors(
    primary = ColorPalette.NavyBlue800,
    secondary = ColorPalette.Gray600,
    tertiary = ColorPalette.Gray500,
    inverse = ColorPalette.Gray100,
    success = ColorPalette.Green500,
    warning = ColorPalette.Yellow700,
    error = ColorPalette.Pink700,
    link = Brand.Primary,
    white = Color.White,
    black = Color.Black,
    brand = ColorPalette.Blue500
)

fun textColorsForDark() = TextColors(
    primary = ColorPalette.NavyBlue800,
    secondary = ColorPalette.Gray400,
    tertiary = ColorPalette.Gray500,
    inverse = ColorPalette.Gray900,
    success = ColorPalette.Green500,
    warning = ColorPalette.Yellow300,
    error = ColorPalette.Pink700,
    link = Brand.Primary,
    white = Color.White,
    black = Color.Black,
    brand = ColorPalette.Blue500
)
