package com.friend.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppToolbar(
    // Required
    title: String,
    modifier: Modifier = Modifier,

    // Optional
    leadingIcon: (@Composable (() -> Unit))? = null,   // custom override (e.g., Drawer)
    onBackClick: (() -> Unit)? = null,                 // default back icon when provided
    actions: @Composable RowScope.() -> Unit = {},
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    centerTitle: Boolean = false,
    titleTextStyle: TextStyle = MaterialTheme.typography.titleMedium,
    onTitleClick: (() -> Unit)? = null,
    elevation: Dp = 0.dp,
    scrollBehavior: TopAppBarScrollBehavior? = TopAppBarDefaults.pinnedScrollBehavior(),
) {
    val navigationIconComposable: @Composable () -> Unit = leadingIcon ?: {
        if (onBackClick != null) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = "Back"
                )
            }
        }
    }

    val titleComposable: @Composable () -> Unit = {
        if (onTitleClick != null) {
            Text(
                text = title,
                style = titleTextStyle,
                color = contentColor,
                modifier = Modifier.clickable { onTitleClick() }
            )
        } else {
            Text(
                text = title,
                style = titleTextStyle,
                color = contentColor
            )
        }
    }

    val colors = TopAppBarDefaults.topAppBarColors(
        containerColor = backgroundColor,
        titleContentColor = contentColor,
        navigationIconContentColor = contentColor,
        actionIconContentColor = contentColor
    )

    if (centerTitle) {
        CenterAlignedTopAppBar(
            modifier = modifier.shadow(elevation),
            title = titleComposable,
            navigationIcon = navigationIconComposable,
            actions = actions,
            colors = colors,
            scrollBehavior = scrollBehavior,
        )
    } else {
        TopAppBar(
            modifier = modifier.shadow(elevation),
            title = titleComposable,
            navigationIcon = navigationIconComposable,
            actions = actions,
            colors = colors,
            scrollBehavior = scrollBehavior,
        )
    }
}


@Composable
fun StatusBarScrim(color: Color = MaterialTheme.colorScheme.primary) {
    Spacer(
        Modifier
            .fillMaxWidth()
            .windowInsetsTopHeight(WindowInsets.statusBars)
            .background(color)
    )
}