package com.friend.ui

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import android.provider.Settings
import com.google.accompanist.permissions.PermissionStatus

data class PermissionState(
    val permission: String,
    val isGranted: Boolean = false,
    val shouldShowRationale: Boolean = false,
    val isPermanentlyDenied: Boolean = false
)

class PermissionHandler(private val context: Context) {
    private val _permissionState = MutableStateFlow(PermissionState(""))
    val permissionState = _permissionState.asStateFlow()

    /**
     * Updates the state flow based on current system status
     */
    fun checkPermissionStatus(permission: String) {
        val isGranted = ContextCompat.checkSelfPermission(
            context, permission
        ) == PackageManager.PERMISSION_GRANTED

        val shouldShowRationale = if (context is ComponentActivity) {
            ActivityCompat.shouldShowRequestPermissionRationale(context, permission)
        } else false

        _permissionState.update {
            PermissionState(
                permission = permission,
                isGranted = isGranted,
                shouldShowRationale = shouldShowRationale,
                // If not granted and rationale is false, it's either first time or permanent
                isPermanentlyDenied = !isGranted && !shouldShowRationale
            )
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionGate(
    permission: String,
    rationaleContent: @Composable () -> Unit,
    deniedContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    val permissionState = rememberPermissionState(permission)

    when {
        permissionState.status.isGranted -> content()
        permissionState.status.shouldShowRationale -> rationaleContent()
        else -> deniedContent()
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionHandler(
    permission: String, onPermissionGranted: () -> Unit,
    // This allows you to pass any UI (Button, Icon, etc.) to trigger the check
    content: @Composable (onClick: () -> Unit) -> Unit
) {
    val permissionState = rememberPermissionState(permission)

    // Trigger the actual action or request based on state
    val performAction = {
        when {
            permissionState.status.isGranted -> {
                onPermissionGranted()
            }

            else -> {
                permissionState.launchPermissionRequest()
            }
        }
    }

    content(performAction)

    // Handle the logic for when permission is granted or denied
    when {
        permissionState.status.shouldShowRationale -> {
            // Optional: Show a small hint UI or Toast explaining why it's needed
        }

        !permissionState.status.isGranted && !permissionState.status.shouldShowRationale -> {
            // This happens if permanently denied.
            // You could show a "Go to Settings" Dialog here.
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun rememberPermissionAction(
    permission: String,
): (onGranted: () -> Unit) -> Unit {
    val context = LocalContext.current
    val permissionState = rememberPermissionState(permission)

    // Function to open app settings
    val openSettings = {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }

    return { onGranted ->
        when {
            // 1. Already Granted
            permissionState.status.isGranted -> {
                onGranted()
            }

            // 2. Permanently Denied (No rationale + not granted)
            // Note: On first run, shouldShowRationale is false,
            // but we check if it's the very first time using the permission state
            !permissionState.status.shouldShowRationale &&
                    permissionState.status is PermissionStatus.Denied -> {
                // Since launchPermissionRequest won't work, we redirect to settings
                openSettings()
            }

            // 3. First time or Rationale needed
            else -> {
                permissionState.launchPermissionRequest()
            }
        }
    }
}