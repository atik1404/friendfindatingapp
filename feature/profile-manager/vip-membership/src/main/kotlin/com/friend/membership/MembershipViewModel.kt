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
import com.friend.common.base.BaseViewModel
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BillingViewModel @Inject constructor(
    application: Application
) : BaseViewModel(), PurchasesUpdatedListener {
    private val sku1 = "com.friendfin.basic"
    private val sku2 = "com.friendfin.standard"
    private val sku3 = "com.friendfin.premium"

    // Initialize the BillingClient
    private val _billingClient: BillingClient = BillingClient.newBuilder(getApplication(application))
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
        startConnection()
    }

    private fun startConnection() {
        _billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Timber.e("onBillingSetupFinished")
                    _isBillingReady.value = true
                    querySkuDetails() // Replaces your loadAllSKUs
                }
            }

            override fun onBillingServiceDisconnected() {
                Timber.e("onBillingServiceDisconnected")
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
        }
    }

    // Inside BillingViewModel

    fun handlePurchase(purchase: Purchase) {
        // 1. Verify the purchase state
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
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
        val offer = productDetails.subscriptionOfferDetails?.firstOrNull()
        if(offer == null){
            Timber.e("Offer is null")
            return
        }
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