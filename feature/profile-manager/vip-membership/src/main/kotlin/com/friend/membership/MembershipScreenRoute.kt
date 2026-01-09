package com.friend.membership

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.friend.common.utils.BillingManager
import com.friend.ui.common.LoadingUi
import com.friend.ui.showToastMessage
import com.friend.designsystem.R as Res

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MembershipScreenRoute(
    onBackClick: () -> Unit,
    viewModel: BillingViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activity = context as? Activity

    val products by viewModel.productDetailsList.collectAsState()
    val isBillingReady by viewModel.isBillingReady.collectAsState()

    val isPro by BillingManager.isSubscribed.collectAsState()
    val activeSku by BillingManager.activeSku.collectAsState()

    val noActiveSubscription = stringResource(Res.string.error_no_active_subscription)

    if (!isBillingReady) {
        LoadingUi()
    } else
        MembershipScreen(
            onBackClick = onBackClick,
            monthlySubscription = {
                activity?.let { viewModel.launchPurchaseFlow(it, products.first()) }
            },
            yearlySubscription = {
                activity?.let { viewModel.launchPurchaseFlow(it, products[1]) }
            },
            manageSubscription = {
                if (isPro) {
                    val browserIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/account/subscriptions?sku=$activeSku&package=com.friendfinapp.dating")
                    )
                    activity?.startActivity(browserIntent)
                } else
                    context.showToastMessage(noActiveSubscription)
            }
        )
}