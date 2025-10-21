package com.friend.designsystem.colors

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class TextFieldColors(
    val backgroundColor: Color,
    val textColor: Color,
    val placeholderColor: Color,
    val labelColor: Color,
    val leadingIconColor: Color,
    val trailingIconColor: Color,
    val borderColor: Color,
    val focusedBorderColor: Color,
    val errorBorderColor: Color,
)

data class AppTextFieldColors(
    val filledTextField: TextFieldColors,
    val outlinedTextField: TextFieldColors,
    val disabledFilledTextField: TextFieldColors,
    val disabledOutlinedTextField: TextFieldColors,
)

val LocalTextFieldColors = staticCompositionLocalOf {
    AppTextFieldColors(
        filledTextField = TextFieldColors(
            backgroundColor = ColorPalette.Gray100,
            textColor = ColorPalette.Gray950,
            placeholderColor = ColorPalette.Gray300,
            labelColor = ColorPalette.Gray500,
            leadingIconColor = ColorPalette.Gray500,
            trailingIconColor = ColorPalette.Gray500,
            borderColor = ColorPalette.Blue50,
            focusedBorderColor = ColorPalette.Blue100,
            errorBorderColor = ColorPalette.Red300,
        ),
        outlinedTextField = TextFieldColors(
            backgroundColor = ColorPalette.White,
            textColor = ColorPalette.Gray950,
            placeholderColor = ColorPalette.Gray300,
            labelColor = ColorPalette.Gray500,
            leadingIconColor = ColorPalette.Gray500,
            trailingIconColor = ColorPalette.Gray500,
            borderColor = ColorPalette.Blue50,
            focusedBorderColor = ColorPalette.Blue100,
            errorBorderColor = ColorPalette.Red300,
        ),
        disabledFilledTextField = TextFieldColors(
            backgroundColor = ColorPalette.Gray200,
            textColor = ColorPalette.Gray500,
            placeholderColor = ColorPalette.Gray300,
            labelColor = ColorPalette.Gray500,
            leadingIconColor = ColorPalette.Gray500,
            trailingIconColor = ColorPalette.Gray500,
            borderColor = ColorPalette.Blue50,
            focusedBorderColor = ColorPalette.Blue100,
            errorBorderColor = ColorPalette.Red300,
        ),
        disabledOutlinedTextField = TextFieldColors(
            backgroundColor = ColorPalette.White,
            textColor = ColorPalette.Gray500,
            placeholderColor = ColorPalette.Gray300,
            labelColor = ColorPalette.Gray500,
            leadingIconColor = ColorPalette.Gray500,
            trailingIconColor = ColorPalette.Gray500,
            borderColor = ColorPalette.Blue50,
            focusedBorderColor = ColorPalette.Blue100,
            errorBorderColor = ColorPalette.Red300,
        )
    )
}

fun textFieldColorForLight() = AppTextFieldColors(
    filledTextField = TextFieldColors(
        backgroundColor = ColorPalette.Gray100,
        textColor = ColorPalette.Gray950,
        placeholderColor = ColorPalette.Gray300,
        labelColor = ColorPalette.Gray500,
        leadingIconColor = ColorPalette.Gray500,
        trailingIconColor = ColorPalette.Gray500,
        borderColor = ColorPalette.Blue50,
        focusedBorderColor = ColorPalette.Blue100,
        errorBorderColor = ColorPalette.Red300,
    ),
    outlinedTextField = TextFieldColors(
        backgroundColor = ColorPalette.White,
        textColor = ColorPalette.Gray950,
        placeholderColor = ColorPalette.Gray300,
        labelColor = ColorPalette.Gray500,
        leadingIconColor = ColorPalette.Gray500,
        trailingIconColor = ColorPalette.Gray500,
        borderColor = ColorPalette.Blue50,
        focusedBorderColor = ColorPalette.Blue100,
        errorBorderColor = ColorPalette.Red300,
    ),
    disabledFilledTextField = TextFieldColors(
        backgroundColor = ColorPalette.Gray200,
        textColor = ColorPalette.Gray500,
        placeholderColor = ColorPalette.Gray300,
        labelColor = ColorPalette.Gray500,
        leadingIconColor = ColorPalette.Gray500,
        trailingIconColor = ColorPalette.Gray500,
        borderColor = ColorPalette.Blue50,
        focusedBorderColor = ColorPalette.Blue100,
        errorBorderColor = ColorPalette.Red300,
    ),
    disabledOutlinedTextField = TextFieldColors(
        backgroundColor = ColorPalette.White,
        textColor = ColorPalette.Gray500,
        placeholderColor = ColorPalette.Gray300,
        labelColor = ColorPalette.Gray500,
        leadingIconColor = ColorPalette.Gray500,
        trailingIconColor = ColorPalette.Gray500,
        borderColor = ColorPalette.Blue50,
        focusedBorderColor = ColorPalette.Blue100,
        errorBorderColor = ColorPalette.Red300,
    )
)

fun textFieldColorForDark() = AppTextFieldColors(
    filledTextField = TextFieldColors(
        backgroundColor = ColorPalette.Gray100,
        textColor = ColorPalette.Gray950,
        placeholderColor = ColorPalette.Gray300,
        labelColor = ColorPalette.Gray500,
        leadingIconColor = ColorPalette.Gray500,
        trailingIconColor = ColorPalette.Gray500,
        borderColor = ColorPalette.Blue50,
        focusedBorderColor = ColorPalette.Blue100,
        errorBorderColor = ColorPalette.Red300,
    ),
    outlinedTextField = TextFieldColors(
        backgroundColor = ColorPalette.White,
        textColor = ColorPalette.Gray950,
        placeholderColor = ColorPalette.Gray300,
        labelColor = ColorPalette.Gray500,
        leadingIconColor = ColorPalette.Gray500,
        trailingIconColor = ColorPalette.Gray500,
        borderColor = ColorPalette.Blue50,
        focusedBorderColor = ColorPalette.Blue100,
        errorBorderColor = ColorPalette.Red300,
    ),
    disabledFilledTextField = TextFieldColors(
        backgroundColor = ColorPalette.Gray200,
        textColor = ColorPalette.Gray500,
        placeholderColor = ColorPalette.Gray300,
        labelColor = ColorPalette.Gray500,
        leadingIconColor = ColorPalette.Gray500,
        trailingIconColor = ColorPalette.Gray500,
        borderColor = ColorPalette.Blue50,
        focusedBorderColor = ColorPalette.Blue100,
        errorBorderColor = ColorPalette.Red300,
    ),
    disabledOutlinedTextField = TextFieldColors(
        backgroundColor = ColorPalette.White,
        textColor = ColorPalette.Gray500,
        placeholderColor = ColorPalette.Gray300,
        labelColor = ColorPalette.Gray500,
        leadingIconColor = ColorPalette.Gray500,
        trailingIconColor = ColorPalette.Gray500,
        borderColor = ColorPalette.Blue50,
        focusedBorderColor = ColorPalette.Blue100,
        errorBorderColor = ColorPalette.Red300,
    )
)