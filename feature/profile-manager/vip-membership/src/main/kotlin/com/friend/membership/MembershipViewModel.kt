package com.friend.membership

import android.app.Activity
import android.app.Application
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.acknowledgePurchase
import com.friend.common.analytics.AnalyticsEvent
import com.friend.common.analytics.AnalyticsParam
import com.friend.common.analytics.AnalyticsService
import com.friend.common.analytics.UserType
import com.friend.common.base.BaseViewModel
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BillingViewModel @Inject constructor(
    application: Application,
    private val analytics: AnalyticsService,
) : BaseViewModel(), PurchasesUpdatedListener {
    private val sku1 = "com.friendfin.basic"
    private val sku2 = "com.friendfin.standard"
    private val sku3 = "com.friendfin.premium"

    // Initialize the BillingClient
    private val _billingClient: BillingClient =
        BillingClient.newBuilder(getApplication(application))
            .setListener(this)
            .enablePendingPurchases(
                PendingPurchasesParams.newBuilder()
                    .enableOneTimeProducts()
                    .build()
            )
            .build()

    // State to hold the list of products (SKUs)
    private val _productDetailsList = MutableStateFlow<List<ProductDetails>>(emptyList())
    val productDetailsList = _productDetailsList.asStateFlow()

    // State to track if billing is ready
    private val _isBillingReady = MutableStateFlow(false)
    val isBillingReady = _isBillingReady.asStateFlow()

    init {
        analytics.logEvent(AnalyticsEvent.SUBSCRIPTION_SCREEN_VIEWED)
        startConnection()
    }

    private fun startConnection() {
        _billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    _isBillingReady.value = true
                    querySkuDetails() // Replaces your loadAllSKUs
                }
            }

            override fun onBillingServiceDisconnected() {
                _isBillingReady.value = false
            }
        })
    }

    private fun querySkuDetails() {
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(
                listOf(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(sku1)
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build(),
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(sku2)
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build(),
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(sku3)
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build()
                )
            )
            .build()

        _billingClient.queryProductDetailsAsync(params) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                // Update the state. The UI will automatically react to this.
                _productDetailsList.value = productDetailsList.productDetailsList
            }
        }
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {
        // Handle purchase updates here
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                handlePurchase(purchase)
            }
        } else {
            analytics.logEvent(
                AnalyticsEvent.PURCHASE_FAILED,
                mapOf(AnalyticsParam.STATUS_CODE to billingResult.responseCode.toString()),
            )
        }
    }

    // Inside BillingViewModel

    fun handlePurchase(purchase: Purchase) {
        // 1. Verify the purchase state
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            val productId = purchase.products.firstOrNull().orEmpty()
            analytics.logEvent(
                AnalyticsEvent.PURCHASE_COMPLETED,
                mapOf(AnalyticsParam.PRODUCT_ID to productId),
            )
            analytics.logEvent(
                AnalyticsEvent.VIP_MEMBERSHIP_ACTIVATED,
                mapOf(AnalyticsParam.PRODUCT_ID to productId),
            )
            analytics.setUserType(UserType.VIP)
            // 2. Check if not yet acknowledged
            if (!purchase.isAcknowledged) {
                // 3. Launch a coroutine to handle the network operation
                execute {
                    val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                        .build()

                    // 4. Use the suspend function from billing-ktx (no more listeners!)
                    val result = _billingClient.acknowledgePurchase(acknowledgePurchaseParams)

                    if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                        Timber.e("Purchase Acknowledged Successfully")
                    } else {
                        Timber.e("Purchase Acknowledged Failed")
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        _billingClient.endConnection()
    }

    // Helper to launch purchase flow from UI
    fun launchPurchaseFlow(activity: Activity, productDetails: ProductDetails) {
        analytics.logEvent(
            AnalyticsEvent.PURCHASE_STARTED,
            mapOf(AnalyticsParam.PRODUCT_ID to productDetails.productId),
        )
        val offer = productDetails.subscriptionOfferDetails?.firstOrNull() ?: return
        val flowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(
                listOf(
                    BillingFlowParams.ProductDetailsParams.newBuilder()
                        .setProductDetails(productDetails)
                        .setOfferToken(offer.offerToken)
                        .build()
                )
            )
            .build()
        _billingClient.launchBillingFlow(activity, flowParams)
    }
}