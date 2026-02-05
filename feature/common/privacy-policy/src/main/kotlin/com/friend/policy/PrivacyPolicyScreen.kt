package com.friend.policy

import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.friend.ui.common.AppToolbar
import com.friend.ui.components.AppScaffold
import com.friend.ui.preview.LightPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(
    onBackClick: () -> Unit,
) {
    AppScaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            AppToolbar(
                title = "",
                onBackClick = onBackClick,
            )
        }
    ) { padding ->
        AndroidView(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    // Disable interactions
                    isClickable = false
                    isLongClickable = false
                    setOnLongClickListener { true }
                    isHapticFeedbackEnabled = false

                    settings.apply {
                        javaScriptEnabled = false
                        setSupportZoom(false)
                        displayZoomControls = false
                    }

                    webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(
                            view: WebView?,
                            request: WebResourceRequest?
                        ): Boolean {
                            // Block all link/email navigation
                            return true
                        }
                    }
                    loadUrl("file:///android_asset/privacy_policy.html")
                }
            }
        )
    }
}

@Composable
@LightPreview
private fun ScreenPreview() {
    PrivacyPolicyScreen {

    }
}