package com.friend.ui.ads

import AppDivider
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.friend.designsystem.spacing.SpacingToken
import com.friend.designsystem.spacing.appPaddingVertical
import com.friend.designsystem.theme.dividerColors
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.friend.designsystem.R as Res

@Composable
fun BannerAds(
    modifier: Modifier = Modifier,
    adUnitId: String = "ca-app-pub-3940256099942544/6300978111",
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var isLoaded by remember(adUnitId) { mutableStateOf(false) }

    val adView = remember(adUnitId) {
        AdView(context).apply {
            this.adUnitId = adUnitId
            setAdSize(AdSize.BANNER)

            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    isLoaded = true
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    isLoaded = false
                }
            }
        }
    }

    // Load once (don’t reload on every recomposition)
    LaunchedEffect(adUnitId) {
        isLoaded = false
        val request = AdRequest.Builder().build()
        adView.loadAd(request)
    }

    // Lifecycle handling (prevents leaks)
    DisposableEffect(lifecycleOwner, adView) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> adView.resume()
                Lifecycle.Event.ON_PAUSE -> adView.pause()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            adView.destroy()
        }
    }

    if (isLoaded){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .appPaddingVertical(SpacingToken.micro)
                .navigationBarsPadding() // Keeps ad visible above gestures
        ) {
            AppDivider(color = MaterialTheme.dividerColors.primary)
            Spacer(modifier = modifier.height(SpacingToken.micro))
            AndroidView(
                factory = { adView },
                modifier = modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )
        }
    }
}
