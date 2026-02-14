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
import androidx.core.net.toUri

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

                    // Basic settings
                    settings.apply {
                        javaScriptEnabled =
                            false // Keep false for security if just displaying static HTML
                        setSupportZoom(true)      // Usually good for readability
                        builtInZoomControls = true
                        displayZoomControls = false
                    }

                    webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(
                            view: WebView?,
                            request: WebResourceRequest?
                        ): Boolean {
                            val url = request?.url?.toString() ?: return false

                            // If it's a web link or email, open it in an External App
                            if (url.startsWith("http://") || url.startsWith("https://") || url.startsWith(
                                    "mailto:"
                                )
                            ) {
                                val intent = android.content.Intent(
                                    android.content.Intent.ACTION_VIEW,
                                    url.toUri()
                                )
                                context.startActivity(intent)
                                return true // We handled it by opening the browser
                            }

                            return false // Allow the WebView to handle internal loads (like your local asset)
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