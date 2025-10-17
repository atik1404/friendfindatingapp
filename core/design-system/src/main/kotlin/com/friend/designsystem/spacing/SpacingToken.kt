package com.friend.designsystem.spacing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object SpacingToken {
    val none = 0.dp           // no space
    val micro = 4.dp          // very tight space
    val tiny = 8.dp           // small space, text-to-text
    val extraSmall = 10.dp    // uncommon but slightly bigger than tiny
    val small = 12.dp         // common compact spacing
    val medium = 16.dp        // default body padding
    val large = 24.dp         // section spacing
    val extraLarge = 32.dp    // screen-level spacing
    val huge = 48.dp          // banners, dialogs, big gaps
}


// Apply same padding on all sides
fun Modifier.appPadding(all: Dp): Modifier =
    this.then(Modifier.padding(all))

// Apply symmetric horizontal + vertical padding
fun Modifier.appPaddingSymmetric(horizontal: Dp = 0.dp, vertical: Dp = 0.dp): Modifier =
    this.then(Modifier.padding(horizontal = horizontal, vertical = vertical))

// Apply only horizontal padding
fun Modifier.appPaddingHorizontal(horizontal: Dp): Modifier =
    this.then(Modifier.padding(horizontal = horizontal))

// Apply only vertical padding
fun Modifier.appPaddingVertical(vertical: Dp): Modifier =
    this.then(Modifier.padding(vertical = vertical))

// Apply padding individually (like "only" in Flutter)
fun Modifier.appPaddingOnly(
    start: Dp = 0.dp,
    top: Dp = 0.dp,
    end: Dp = 0.dp,
    bottom: Dp = 0.dp
): Modifier = this.then(
    Modifier.padding(start = start, top = top, end = end, bottom = bottom)
)

fun appHorizontalArrangement(space: Dp = SpacingToken.none) = Arrangement.spacedBy(space)

fun appVerticalArrangement(space: Dp = SpacingToken.none) = Arrangement.spacedBy(space)
