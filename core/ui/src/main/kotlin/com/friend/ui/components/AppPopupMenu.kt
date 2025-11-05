package com.friend.ui.components

import androidx.compose.foundation.layout.Box
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

@Composable
fun AppPopupMenu(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    menuItems: List<String>,
    onClick: (String) -> Unit
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
                    text = { Text(it) },
                    onClick = {
                        expanded = false
                        onClick.invoke(it)
                    }
                )
            }
        }
    }
}