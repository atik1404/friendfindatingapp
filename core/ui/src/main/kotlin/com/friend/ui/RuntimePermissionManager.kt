package com.friend.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun rememberPermissionAction(
    permission: String,
    onOpenSetting: () -> Unit,
): (onGranted: () -> Unit) -> Unit {
    val permissionState = rememberPermissionState(permission)

    return { onGranted ->
        when {
            permissionState.status.isGranted -> onGranted()
            !permissionState.status.shouldShowRationale -> onOpenSetting.invoke()
            permissionState.status is PermissionStatus.Denied -> permissionState.launchPermissionRequest()
            else -> permissionState.launchPermissionRequest()
        }
    }
}

fun openSetting(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    context.startActivity(intent)
}