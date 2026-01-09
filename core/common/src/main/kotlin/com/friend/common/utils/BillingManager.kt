package com.friend.common.utils

import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.QueryPurchasesParams
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object BillingManager {
    private val _isSubscribed = MutableStateFlow(false)
    val isSubscribed = _isSubscribed.asStateFlow()

    private val _activeSku = MutableStateFlow("")
    val activeSku = _activeSku.asStateFlow()

    private var billingClient: BillingClient? = null

    // Call this once from your Application class or MainActivity
    fun start(context: Context) {
        if (billingClient != null) return // Already started

        billingClient = BillingClient.newBuilder(context.applicationContext)
            .setListener { billingResult, purchases ->
                // Handle purchase updates globally here
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                    checkPurchases(purchases)
                }
            }
            .enablePendingPurchases(
                PendingPurchasesParams.newBuilder().enableOneTimeProducts().build()
            )
            .build()

        connect()
    }

    private fun connect() {
        billingClient?.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    queryPurchases() // Check status immediately on connect
                }
            }

            override fun onBillingServiceDisconnected() {
                // Retry logic can go here
            }
        })
    }

    fun queryPurchases() {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.SUBS)
            .build()

        billingClient?.queryPurchasesAsync(params) { billingResult, purchases ->
            checkPurchases(purchases)
        }
    }

    private fun checkPurchases(purchases: List<Purchase>) {
        val hasSubscription = purchases.isNotEmpty() &&
                purchases.any { it.purchaseState == Purchase.PurchaseState.PURCHASED }

        _isSubscribed.value = hasSubscription
        _activeSku.value = purchases.first().products.first().toString()
    }
}