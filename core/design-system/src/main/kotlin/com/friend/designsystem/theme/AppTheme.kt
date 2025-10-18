package com.friend.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.friend.designsystem.colors.AppButtonColors
import com.friend.designsystem.colors.BackgroundColors
import com.friend.designsystem.colors.ColorPalette
import com.friend.designsystem.colors.DividerColors
import com.friend.designsystem.colors.IconColors
import com.friend.designsystem.colors.LocalBackgroundColors
import com.friend.designsystem.colors.LocalButtonColors
import com.friend.designsystem.colors.LocalDividerColors
import com.friend.designsystem.colors.LocalIconColors
import com.friend.designsystem.colors.LocalStrokeColors
import com.friend.designsystem.colors.LocalSurfaceColors
import com.friend.designsystem.colors.LocalTextColors
import com.friend.designsystem.colors.StrokesColors
import com.friend.designsystem.colors.SurfaceColors
import com.friend.designsystem.colors.TextColors
import com.friend.designsystem.colors.backgroundColorsForDark
import com.friend.designsystem.colors.backgroundColorsForLight
import com.friend.designsystem.colors.buttonColorsForDark
import com.friend.designsystem.colors.buttonColorsForLight
import com.friend.designsystem.colors.dividerColorsForDark
import com.friend.designsystem.colors.dividerColorsForLight
import com.friend.designsystem.colors.iconsColorsForDark
import com.friend.designsystem.colors.iconsColorsForLight
import com.friend.designsystem.colors.strokesColorsForDark
import com.friend.designsystem.colors.strokesColorsForLight
import com.friend.designsystem.colors.surfaceColorsForDark
import com.friend.designsystem.colors.surfaceColorsForLight
import com.friend.designsystem.colors.textColorsForDark
import com.friend.designsystem.colors.textColorsForLight
import com.friend.designsystem.typography.AppTypography

object Brand {
    val Primary = ColorPalette.Blue500
    val PrimaryContainer = ColorPalette.Blue100
    val Secondary = ColorPalette.Gold400
    val SecondaryContainer = ColorPalette.Gold100
    val Tertiary = ColorPalette.Yellow400
    val TertiaryContainer = ColorPalette.Yellow100

    val Error = ColorPalette.Red600
    val ErrorContainer = ColorPalette.Red100
}

val LightColors = lightColorScheme(
    primary = Brand.Primary,
    onPrimary = ColorPalette.White,
    primaryContainer = Brand.PrimaryContainer,
    onPrimaryContainer = ColorPalette.Green700,

    secondary = Brand.Secondary,
    onSecondary = ColorPalette.Black,
    secondaryContainer = Brand.SecondaryContainer,
    onSecondaryContainer = ColorPalette.Gold700,

    tertiary = Brand.Tertiary,
    onTertiary = ColorPalette.Black,
    tertiaryContainer = Brand.TertiaryContainer,
    onTertiaryContainer = ColorPalette.Yellow800,

    background = ColorPalette.Gray50,
    onBackground = ColorPalette.Gray900,

    surface = ColorPalette.Gray50,
    onSurface = ColorPalette.Gray900,
    surfaceVariant = ColorPalette.Gray100,
    onSurfaceVariant = ColorPalette.Gray600,
    surfaceTint = Brand.Primary,

    inverseSurface = ColorPalette.Gray900,
    inverseOnSurface = ColorPalette.Gray100,

    error = Brand.Error,
    onError = ColorPalette.White,
    errorContainer = Brand.ErrorContainer,
    onErrorContainer = ColorPalette.Red800,

    outline = ColorPalette.Gray300,
    outlineVariant = ColorPalette.Gray200,
    //scrim = Color(0x66000000),

    surfaceBright = ColorPalette.White,
    surfaceDim = ColorPalette.Gray100,
    surfaceContainerLowest = ColorPalette.White,
    surfaceContainerLow = ColorPalette.Gray50,
    surfaceContainer = ColorPalette.Gray100,
    surfaceContainerHigh = ColorPalette.Gray200,
    surfaceContainerHighest = ColorPalette.Gray300,
)

val DarkColors = darkColorScheme(
    primary = Brand.Primary,
    onPrimary = ColorPalette.Black,
    primaryContainer = ColorPalette.Green600,
    onPrimaryContainer = ColorPalette.Green25,

    secondary = Brand.Secondary,
    onSecondary = ColorPalette.Black,
    secondaryContainer = ColorPalette.Gold600,
    onSecondaryContainer = ColorPalette.Gold50,

    tertiary = Brand.Tertiary,
    onTertiary = ColorPalette.Black,
    tertiaryContainer = ColorPalette.Yellow700,
    onTertiaryContainer = ColorPalette.Yellow50,

    background = ColorPalette.Gray950,
    onBackground = ColorPalette.Gray100,

    surface = ColorPalette.Gray950,
    onSurface = ColorPalette.Gray100,
    surfaceVariant = ColorPalette.Gray800,
    onSurfaceVariant = ColorPalette.Gray400,
    surfaceTint = Brand.Primary,

    inverseSurface = ColorPalette.Gray100,
    inverseOnSurface = ColorPalette.Gray900,

    error = Brand.Error,
    onError = ColorPalette.White,
    errorContainer = ColorPalette.Red700,
    onErrorContainer = ColorPalette.Red50,

    outline = ColorPalette.Gray600,
    outlineVariant = ColorPalette.Gray700,

    surfaceBright = ColorPalette.Gray900,
    surfaceDim = ColorPalette.Gray950,
    surfaceContainerLowest = ColorPalette.Black,
    surfaceContainerLow = ColorPalette.Gray900,
    surfaceContainer = ColorPalette.Gray900,
    surfaceContainerHigh = ColorPalette.Gray800,
    surfaceContainerHighest = ColorPalette.Gray700,
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    typography: Typography = AppTypography,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    val textColors = if (darkTheme) textColorsForDark() else textColorsForLight()
    val buttonColors = if (darkTheme) buttonColorsForDark() else buttonColorsForLight()
    val backgroundColors = if (darkTheme) backgroundColorsForDark() else backgroundColorsForLight()
    val dividerColors = if (darkTheme) dividerColorsForDark() else dividerColorsForLight()
    val strokeColors = if (darkTheme) strokesColorsForDark() else strokesColorsForLight()
    val surfaceColors = if (darkTheme) surfaceColorsForDark() else surfaceColorsForLight()
    val iconColors = if (darkTheme) iconsColorsForDark() else iconsColorsForLight()

    CompositionLocalProvider(
        LocalTextColors provides textColors,
        LocalBackgroundColors provides backgroundColors,
        LocalDividerColors provides dividerColors,
        LocalStrokeColors provides strokeColors,
        LocalSurfaceColors provides surfaceColors,
        LocalIconColors provides iconColors,
        LocalButtonColors provides buttonColors,
    ) {
        MaterialTheme(
            colorScheme = colors,
            typography = typography,
            content = content
        )
    }
}

val MaterialTheme.textColors: TextColors
    @Composable get() = LocalTextColors.current

val MaterialTheme.dividerColors: DividerColors
    @Composable get() = LocalDividerColors.current

val MaterialTheme.strokeColors: StrokesColors
    @Composable get() = LocalStrokeColors.current

val MaterialTheme.backgroundColors: BackgroundColors
    @Composable get() = LocalBackgroundColors.current

val MaterialTheme.surfaceColors: SurfaceColors
    @Composable get() = LocalSurfaceColors.current

val MaterialTheme.iconColors: IconColors
    @Composable get() = LocalIconColors.current

val MaterialTheme.buttonColors: AppButtonColors
    @Composable get() = LocalButtonColors.current