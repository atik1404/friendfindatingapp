package com.friendfinapp.dating.ui.vipactivity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.viewpager.widget.ViewPager
import com.android.billingclient.api.*
import com.friendfinapp.dating.R
import com.friendfinapp.dating.databinding.ActivityVipPremiumBinding
import com.friendfinapp.dating.helper.Constants.SKUS
import com.friendfinapp.dating.ui.vipactivity.vipadapter.VipPagerAdapter
import android.net.Uri

import android.content.Intent
import com.friendfinapp.dating.application.BaseActivity
import com.friendfinapp.dating.helper.Constants
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import timber.log.Timber


class VipPremiumActivity : BaseActivity<ActivityVipPremiumBinding>(),
    ViewPager.OnPageChangeListener,
    PurchasesUpdatedListener {

    private var mContext: Context? = null
    private var mAdapter: VipPagerAdapter? = null

    private var dotsCount = 0
    private lateinit var dots: Array<ImageView?>


    //premium

    private var billingClient: BillingClient? = null
    private val skuList: MutableList<String> = ArrayList()

    private val sku1 = "com.friendfin.basic"
    private val sku2 = "com.friendfin.standard"
    private val sku3 = "com.friendfin.premium"

    var mSkuDetails: ProductDetails? = null
    var userId: String? = null
    var acknowledgePurchaseResponseListener: AcknowledgePurchaseResponseListener? = null

    var check = false
    var mResources = intArrayOf(R.drawable.allvip, R.drawable.noads)
    override fun viewBindingLayout(): ActivityVipPremiumBinding =
        ActivityVipPremiumBinding.inflate(layoutInflater)

    override fun initializeView(savedInstanceState: Bundle?) {
        mContext = this

        mAdapter =
            VipPagerAdapter(this@VipPremiumActivity, mContext as VipPremiumActivity, mResources)
        binding.viewpager.adapter = mAdapter
        binding.viewpager.currentItem = 0
        binding.viewpager.setOnPageChangeListener(this)
        setPageViewIndicator()
        setUpClickListener()

        if (!Constants.IS_SUBSCRIBE) {
            binding.adView.visibility = View.VISIBLE
            var adView = AdView(this)
            val adRequest = AdRequest.Builder().build()

            adView.setAdSize(AdSize.BANNER)


            adView.adUnitId = getString(R.string.BannerAdsUnitId)
            binding.adView.loadAd(adRequest)
        } else {
            binding.adView.visibility = View.GONE
        }
    }

    private fun setUpView(oneMonth: Button, yearly: Button) {
        skuList.add(sku1)
        skuList.add(sku2)
        skuList.add(sku3)
        setupBillingClient(oneMonth, yearly)
    }

    private fun setupBillingClient(oneMonth: Button, yearly: Button) {
        Timber.e("setupBillingClient: $skuList")
        acknowledgePurchaseResponseListener = AcknowledgePurchaseResponseListener { billingResult -> println(billingResult.responseCode) }
        billingClient =
            BillingClient.newBuilder(this)
                .setListener(this)
                .enablePendingPurchases(
                PendingPurchasesParams.newBuilder()
                    .enableOneTimeProducts()
                    .build()
            ).build()
        billingClient?.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Timber.e("onBillingSetupFinished: ")
                    loadAllSKUs(oneMonth, yearly)
                }
            }

            override fun onBillingServiceDisconnected() {
                Timber.e("onBillingServiceDisconnected: ")
            }
        })
    }

    private fun loadAllSKUs(oneMonth: Button, yearly: Button) {

        //Toast.makeText(InnAppProducts.this, "", Toast.LENGTH_SHORT).show();
        if (billingClient!!.isReady) {
            val products = skuList.distinct().map {
                QueryProductDetailsParams.Product.newBuilder()
                    .setProductId(it)
                    .setProductType(BillingClient.ProductType.SUBS) // or INAPP if you sell one-time items
                    .build()
            }
            val params = QueryProductDetailsParams.newBuilder()
                .setProductList(products)
                .build()
            billingClient?.queryProductDetailsAsync(
                params
            ) { billingResult, skuDetails ->
                //Toast.makeText(InnAppProducts.this, "inside query" + billingResult.getResponseCode(), Toast.LENGTH_SHORT).show();
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    val skuDetailsList = skuDetails.productDetailsList
                    Timber.e("loadAllSKUs1: $skuDetailsList and ${skuDetailsList.size}")
                    for (skuDetailsObject in skuDetailsList) {
                        Timber.e("loadAllSKUs2: $skuDetailsObject}")
                        val productDetails = skuDetailsObject as ProductDetails
                        Timber.e("loadAllSKUs3: sku 1  " + skuDetailsList.size)
                        if (productDetails.productId == sku1) {
                            Timber.e("loadAllSKUs: $skuDetailsObject}")
                            mSkuDetails = productDetails

                            Timber.e("loadAllSKUs: sku 1  " + skuDetailsList.size)

                            oneMonth.setOnClickListener {
                                Timber.e("loadAllSKUs: sku 111")
                                val offer = productDetails.subscriptionOfferDetails?.firstOrNull()
                                if (offer == null) {
                                    Timber.e("No subscriptionOfferDetails for ${productDetails.productId}. Check Play Console base plan/offers & country availability.")
                                    return@setOnClickListener
                                }
                                val productDetailsParamsList = listOf(
                                    BillingFlowParams.ProductDetailsParams.newBuilder()
                                        .setProductDetails(productDetails)
                                        .setOfferToken(offer.offerToken)
                                        .build()
                                )

                                val billingFlowParams = BillingFlowParams
                                    .newBuilder()
                                    .setProductDetailsParamsList(productDetailsParamsList)
                                    .build()

                                billingClient!!.launchBillingFlow(
                                    this@VipPremiumActivity,
                                    billingFlowParams
                                )
                            }

                        }
                        if (productDetails.productId == sku2) {
                            Timber.e("loadAllSKUs: sku 2")
                            mSkuDetails = productDetails

                            yearly.setOnClickListener {
                                val offer = productDetails.subscriptionOfferDetails?.firstOrNull()
                                if (offer == null) {
                                    Timber.e("No subscriptionOfferDetails for ${productDetails.productId}. Check Play Console base plan/offers & country availability.")
                                    return@setOnClickListener
                                }
                                val productDetailsParamsList = listOf(
                                    BillingFlowParams.ProductDetailsParams.newBuilder()
                                        .setProductDetails(productDetails)
                                        .setOfferToken(offer.offerToken)
                                        .build()
                                )

                                val billingFlowParams = BillingFlowParams
                                    .newBuilder()
                                    .setProductDetailsParamsList(productDetailsParamsList)
                                    .build()
                                billingClient!!.launchBillingFlow(
                                    this@VipPremiumActivity,
                                    billingFlowParams
                                )
                            }
                        }
                        else if (productDetails.productId == sku3) {
                            mSkuDetails = productDetails
                        }
                    }
                }
            }
        } else Toast.makeText(
            this@VipPremiumActivity,
            "billingclient not ready",
            Toast.LENGTH_SHORT
        ).show()
    }


    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {


        val responseCode: Int = billingResult.responseCode
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK
            && purchases != null
        ) {
            for (purchase in purchases) {
                handlePurchase(purchase)
            }
        } else if (responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
        } else if (responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
            Toast.makeText(
                applicationContext,
                "You have recently bought this item. Try another item or try again later",
                Toast.LENGTH_LONG
            ).show()
        } else {
            //Log.d(TAG, "Other code" + responseCode);
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()
                billingClient!!.acknowledgePurchase(
                    acknowledgePurchaseParams,
                    acknowledgePurchaseResponseListener!!
                )
                if (purchase.skus.equals(sku1)) {
                    // buyCoinsViaServer(sku1)
                } else if (purchase.skus.equals(sku2)) {
                    //  buyCoinsViaServer(sku2)
                } else if (purchase.skus.equals(sku3)) {
                    // buyCoinsViaServer(sku3)
                }
            }
        }
    }

    private fun setUpClickListener() {

        binding.imageBack.setOnClickListener {
            finish()
        }
        binding.getStarted.setOnClickListener {
            showDialogs()
        }
    }

    private lateinit var dialog: Dialog


    private fun showDialogs() {
//        if (dialog != null) {
//            dialog.dismiss()
//        }
        dialog = Dialog(this@VipPremiumActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_payment)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        dialog.setCanceledOnTouchOutside(false)
        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        //  int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);
        dialog.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)


        var oneMonth = dialog.findViewById<Button>(R.id.onemotnh)

        var yearly = dialog.findViewById<Button>(R.id.yearly)
        var manageSubscription = dialog.findViewById<Button>(R.id.manageSubscription)



        manageSubscription.setOnClickListener {
            if (SKUS.isNotEmpty()) {
                val browserIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/account/subscriptions?sku=" + SKUS + "&package=com.friendfinapp.dating")
                )
                startActivity(browserIntent)
            } else {
                Toast.makeText(
                    this@VipPremiumActivity,
                    "You Dont Have Any Subscription",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
        setUpView(oneMonth, yearly)

        dialog.show()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setPageViewIndicator() {
        Log.d("###setPageViewIndicator", " : called")
        dotsCount = mAdapter!!.count
        dots = arrayOfNulls<ImageView>(dotsCount)
        for (i in 0 until dotsCount) {
            dots[i] = ImageView(mContext)
            dots[i]?.setImageDrawable(resources.getDrawable(R.drawable.nonselecteditem_dot))
            val params = LinearLayout.LayoutParams(
                70,
                70
            )
            params.setMargins(4, 20, 4, 0)
            dots[i]?.setOnTouchListener(View.OnTouchListener { v, event ->
                binding.viewpager.setCurrentItem(i)
                true
            })
            binding.viewPagerCountDots.addView(dots.get(i), params)
        }
        dots[0]?.setImageDrawable(resources.getDrawable(R.drawable.selecteditem_dot))
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        Log.d("TAG", "onPageSelected: " + position)
        for (i in 0 until dotsCount) {
            dots[i]!!.setImageDrawable(resources.getDrawable(R.drawable.nonselecteditem_dot))
        }


//        if (position == 2) {
//            binding.getStarted.setBackgroundResource(R.drawable.get_started_bg)
//            check = true
//        } else {
//            binding.getStarted.setBackgroundResource(R.drawable.get_started_bg2)
//            check = false
//        }

        dots[position]!!.setImageDrawable(resources.getDrawable(R.drawable.selecteditem_dot))

    }

    override fun onPageScrollStateChanged(state: Int) {

    }
//
//    override fun onBackPressed() {
//        super.onBackPressed()
//        finishAffinity()
//    }

    fun scrollPage(position: Int) {
        binding.viewpager.currentItem = position
    }
}