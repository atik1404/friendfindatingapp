package com.friend.overview.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContactPage
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.friend.common.constant.PersonalMenu
import com.friend.designsystem.R as Res
import com.friend.designsystem.spacing.RadiusToken
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPaddingHorizontal
import com.friend.designsystem.spacing.appPaddingVertical
import com.friend.designsystem.theme.backgroundColors
import com.friend.designsystem.typography.AppTypography
import com.friend.ui.components.AppText

@Composable
fun MenuCard(
    clickedOnMenu: (PersonalMenu) -> Unit,
) {
    Column(
        modifier = Modifier
            .background(
                color = MaterialTheme.backgroundColors.white,
                shape = RoundedCornerShape(RadiusToken.medium)
            )
            .appPaddingHorizontal(SpacingToken.medium)
    ) {
        MenuItem(
            icon = Icons.Default.Person,
            menu = stringResource(Res.string.menu_personal_setting)
        ) {
            clickedOnMenu.invoke(PersonalMenu.PERSONAL_SETTING)
        }

        MenuItem(
            icon = Icons.Default.Lock,
            menu = stringResource(Res.string.menu_change_password)
        ) {
            clickedOnMenu.invoke(PersonalMenu.CHANGE_PASSWORD)
        }

        MenuItem(
            icon = Icons.Default.Verified,
            menu = stringResource(Res.string.menu_vip_membership)
        ) {
            clickedOnMenu.invoke(PersonalMenu.VIP_MEMBERSHIP)
        }

        MenuItem(
            icon = Icons.Default.Policy,
            menu = stringResource(Res.string.menu_privacy_policy)
        ) {
            clickedOnMenu.invoke(PersonalMenu.PRIVACY_POLICY)
        }

        MenuItem(
            icon = Icons.Default.Share,
            menu = stringResource(Res.string.menu_share_app)
        ) {
            clickedOnMenu.invoke(PersonalMenu.SHARE_APP)
        }

        MenuItem(
            icon = Icons.Default.StarRate,
            menu = stringResource(Res.string.menu_rate_app)
        ) {
            clickedOnMenu.invoke(PersonalMenu.RATE_APP)
        }

        MenuItem(
            icon = Icons.Default.ContactPage,
            menu = stringResource(Res.string.menu_contact_us)
        ) {
            clickedOnMenu.invoke(PersonalMenu.CONTACT_US)
        }

        MenuItem(
            icon = Icons.Default.Logout,
            menu = stringResource(Res.string.action_logout)
        ) {
            clickedOnMenu.invoke(PersonalMenu.LOGOUT)
        }
    }
}

@Composable
private fun MenuItem(
    icon: ImageVector,
    menu: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .appPaddingVertical(SpacingToken.medium)
            .clickable {
                onClick.invoke()
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, contentDescription = "")
        Spacer(modifier = Modifier.width(SpacingToken.small))
        AppText(menu, textStyle = AppTypography.bodyMedium)
        Spacer(modifier = Modifier.weight(1f))
        Icon(Icons.Default.KeyboardArrowRight, contentDescription = "")
    }
}