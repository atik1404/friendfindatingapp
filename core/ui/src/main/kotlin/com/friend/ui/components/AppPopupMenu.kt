package com.friend.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.friend.designsystem.R as Res

enum class PopupMenuType {
    ReportAbuse,
    ReportUser,
    MessageSearch,
    DeleteMessage,
    ForwardMessage,
    BlockUser,
    UnblockUser
}

data class PopupMenu(
    val icon: ImageVector,
    val menu: Int,
    val menuType: PopupMenuType,
)

val ChatRoomPopupMenu = listOf(
    PopupMenu(
        icon = Icons.Default.Report,
        menu = Res.string.menu_report_abuse,
        menuType = PopupMenuType.ReportAbuse
    ),
    PopupMenu(
        icon = Icons.Default.Search,
        menu = Res.string.menu_message_search,
        menuType = PopupMenuType.MessageSearch
    ),
    PopupMenu(
        icon = Icons.Default.Refresh,
        menu = Res.string.menu_message_forward,
        menuType = PopupMenuType.ForwardMessage
    ),
    PopupMenu(
        icon = Icons.Default.Delete,
        menu = Res.string.menu_delete_message,
        menuType = PopupMenuType.DeleteMessage
    ),
)

val ProfilePopupMenu = listOf(
    PopupMenu(
        icon = Icons.Default.Report,
        menu = Res.string.menu_report_user,
        menuType = PopupMenuType.ReportUser
    ),
    PopupMenu(
        icon = Icons.Default.Search,
        menu = Res.string.menu_block_user,
        menuType = PopupMenuType.BlockUser
    ),
)

@Composable
fun AppPopupMenu(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    menuItems: List<PopupMenu>,
    onClick: (PopupMenuType) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                icon,
                contentDescription = stringResource(Res.string.msg_image_content_description)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            menuItems.forEach {
                DropdownMenuItem(
                    text = { Text(stringResource(it.menu)) },
                    onClick = {
                        expanded = false
                        onClick.invoke(it.menuType)
                    }
                )
            }
        }
    }
}