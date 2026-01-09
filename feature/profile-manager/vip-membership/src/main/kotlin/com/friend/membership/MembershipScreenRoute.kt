package com.friend.membership

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MembershipScreenRoute(
    onBackClick: () -> Unit,
) {
    val context = LocalContext.current
    MembershipScreen {

    }
}