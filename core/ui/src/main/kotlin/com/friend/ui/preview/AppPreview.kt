package com.friend.ui.preview

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

// ---------- THEME VARIANTS ----------

// Light mode only
@Preview(
    name = "Light",
    showBackground = true,
    showSystemUi = false,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
annotation class LightPreview

// Dark mode only
@Preview(
    name = "Dark",
    showBackground = true,
    showSystemUi = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
annotation class DarkPreview

// Both light & dark
@Preview(
    name = "Light",
    showBackground = true,
    showSystemUi = false,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark",
    showBackground = true,
    showSystemUi = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
annotation class LightDarkPreview

// ---------- DEVICE VARIANTS ----------

// Phone
@Preview(
    name = "Phone",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420"
)
annotation class PhonePreview

// Foldable
@Preview(
    name = "Foldable",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=673dp,height=841dp,dpi=480"
)
annotation class FoldablePreview

// Tablet
@Preview(
    name = "Tablet",
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=1280dp,height=800dp,dpi=240"
)
annotation class TabletPreview
