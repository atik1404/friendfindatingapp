package com.friend.designsystem.spacing

import androidx.compose.ui.unit.dp

object RadiusToken {
    val none = 0.dp// no rounding (sharp corners)
    val small = 4.dp         // subtle rounding (buttons, inputs)
    val medium = 8.dp        // slightly rounded (cards, chips)
    val large = 12.dp        // medium rounding (modals, containers)
    val extraLarge = 16.dp   // strong rounding (surfaces, sheets)
    val xxl = 24.dp          // very rounded (banners, sections)
    val xxxl = 32.dp         // pill-like shapes
    val huge = 48.dp         // full rounded edges (special cases)
    val full = 100.dp        // circular / pill buttons
}