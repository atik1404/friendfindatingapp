package com.friendfinapp.dating

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.friend.common.analytics.AnalyticsEvent
import com.friend.common.analytics.AnalyticsService
import com.friend.common.extfun.showAlertDialog
import com.friend.common.utils.BillingManager
import com.friend.designsystem.theme.AppTheme
import com.friendfinapp.dating.navigation.AppNavConfiguration
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.friend.designsystem.R as Res

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var analytics: AnalyticsService

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted && !shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                showAlertDialog(
                    positiveBtn = getString(Res.string.action_go_to_setting),
                    negativeBtn = getString(Res.string.action_cancel),
                    message = getString(Res.string.msg_notification_permission),
                    positiveBtnCallback = {
                        startActivity(Intent().apply {
                            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                        })
                    },
                    negativeBtnCallback = {},
                    cancelable = false,
                    title = "Permission"
                )
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        BillingManager.start(this)
        setContent {
            AppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavConfiguration(analytics = analytics)
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        trackLaunchIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        trackLaunchIntent(intent)
    }

    override fun onResume() {
        super.onResume()
        BillingManager.queryPurchases()
    }

    /** Attribute how this activity was opened (notification tap / deep link). */
    private fun trackLaunchIntent(intent: Intent?) {
        intent ?: return
        when {
            intent.getBooleanExtra(EXTRA_FROM_NOTIFICATION, false) -> {
                analytics.logEvent(AnalyticsEvent.NOTIFICATION_OPENED)
            }

            intent.action == Intent.ACTION_VIEW && intent.data != null -> {
                analytics.logEvent(AnalyticsEvent.DEEP_LINK_OPENED)
            }
        }
    }

    companion object {
        /** Marker extra set on notification-launch intents. */
        const val EXTRA_FROM_NOTIFICATION = "from_notification"
    }
}
