package com.friend.designsystem.colors

import androidx.compose.material3.ButtonColors
import androidx.compose.runtime.staticCompositionLocalOf

data class AppButtonColors(
    val filledButton: ButtonColors,
    val outlineButton: ButtonColors,
    val primaryButton: ButtonColors,
)

val LocalButtonColors = staticCompositionLocalOf {
    AppButtonColors(
        filledButton = ButtonColors(
            containerColor = ColorPalette.Blue500,
            contentColor = ColorPalette.White,
            disabledContainerColor = ColorPalette.Blue100,
            disabledContentColor = ColorPalette.Gray500,
        ),
        outlineButton = ButtonColors(
            containerColor = ColorPalette.White,
            contentColor = ColorPalette.Black,
            disabledContainerColor = ColorPalette.Gray400,
            disabledContentColor = ColorPalette.Gray200,
        ),
        primaryButton = ButtonColors(
            containerColor = ColorPalette.Blue500,
            contentColor = ColorPalette.White,
            disabledContainerColor = ColorPalette.Blue100,
            disabledContentColor = ColorPalette.Gray500,
        ),
    )
}

fun buttonColorsForLight() = AppButtonColors(
    filledButton = ButtonColors(
        containerColor = ColorPalette.Blue500,
        contentColor = ColorPalette.White,
        disabledContainerColor = ColorPalette.Blue100,
        disabledContentColor = ColorPalette.Gray500,
    ),
    outlineButton = ButtonColors(
        containerColor = ColorPalette.White,
        contentColor = ColorPalette.Black,
        disabledContainerColor = ColorPalette.Gray400,
        disabledContentColor = ColorPalette.Gray200,
    ),
    primaryButton = ButtonColors(
        containerColor = ColorPalette.Blue500,
        contentColor = ColorPalette.White,
        disabledContainerColor = ColorPalette.Blue100,
        disabledContentColor = ColorPalette.Gray500,
    ),
)

fun buttonColorsForDark() = AppButtonColors(
    filledButton = ButtonColors(
        containerColor = ColorPalette.Blue500,
        contentColor = ColorPalette.White,
        disabledContainerColor = ColorPalette.Blue100,
        disabledContentColor = ColorPalette.Gray500,
    ),
    outlineButton = ButtonColors(
        containerColor = ColorPalette.White,
        contentColor = ColorPalette.Black,
        disabledContainerColor = ColorPalette.Gray400,
        disabledContentColor = ColorPalette.Gray200,
    ),
    primaryButton = ButtonColors(
        containerColor = ColorPalette.Blue500,
        contentColor = ColorPalette.White,
        disabledContainerColor = ColorPalette.Blue100,
        disabledContentColor = ColorPalette.Gray500,
    ),
)
