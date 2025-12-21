package com.friend.otherprofile.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.friend.designsystem.R as Res
import com.friend.ui.common.AppToolbar
import com.friend.ui.components.AppPopupMenu
import com.friend.ui.components.PopupMenuType
import com.friend.ui.components.ProfilePopupMenu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppToolbarSection(
    modifier: Modifier = Modifier,
    isBlocked: Boolean,
    onBackButtonClicked: () -> Unit,
    navigateToReportAbuse: () -> Unit,
    onBlockMenuClicked: () -> Unit,
    onUnblockMenuClicked: () -> Unit,
) {
    AppToolbar(
        modifier = modifier,
        title = stringResource(Res.string.title_profile),
        onBackClick = {
            onBackButtonClicked.invoke()
        },
        actions = {
            AppPopupMenu(
                icon = Icons.Default.MoreVert,
                menuItems = if (isBlocked)
                    ProfilePopupMenu.filterNot { it.menuType == PopupMenuType.BlockUser }
                else ProfilePopupMenu.filterNot { it.menuType == PopupMenuType.UnblockUser },
                onClick = { menu ->
                    when (menu) {
                        PopupMenuType.ReportUser -> navigateToReportAbuse.invoke()
                        PopupMenuType.BlockUser -> onBlockMenuClicked.invoke()
                        PopupMenuType.UnblockUser -> onUnblockMenuClicked.invoke()
                        else -> {}
                    }
                }
            )
        }
    )
}