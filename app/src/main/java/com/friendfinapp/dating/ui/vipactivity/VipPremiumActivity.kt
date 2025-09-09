package com.friendfinapp.dating.ui.vipactivity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.android.billingclient.api.*
import com.friendfinapp.dating.R
import com.friendfinapp.dating.databinding.ActivityVipPremiumBinding
import com.friendfinapp.dating.helper.Constants.SKUS
import com.friendfinapp.dating.ui.vipactivity.vipadapter.VipPagerAdapter
import android.net.Uri

import android.content.Intent
import com.friendfinapp.dating.helper.Constants
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView


class VipPremiumActivity : AppCompatActivity() ,  ViewPager.OnPageChangeListener,
    PurchasesUpdatedListener{

    private lateinit var binding: ActivityVipPremiumBinding
    private var mContext: Context? = null
    private var mAdapter: VipPagerAdapter? = null

    private var dotsCount = 0
    private lateinit var dots: Array<ImageView?>



    //premium

    private var billingClient: BillingClient? = null
    private val skuList: MutableList<String> = ArrayList<String>()

    private val sku1 = "com.friendfin.basic"
    private val sku2 = "com.friendfin.standard"
    private val sku3 = "com.friendfin.premium"

    var mSkuDetails: SkuDetails? = null
    var userId: String? = null
    var acknowledgePurchaseResponseListener: AcknowledgePurchaseResponseListener? = null

    var check = false
    var mResources = intArrayOf(R.drawable.allvip, R.drawable.noads)
       // R.drawable.inboxvip,R.drawable.ic_intro11, R.drawable.ic_intro22, R.drawable.ic_intro33)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_vip_premium)
        mContext = this




        mAdapter = VipPagerAdapter(this@VipPremiumActivity, mContext as VipPremiumActivity, mResources)
        binding.viewpager.adapter = mAdapter
        binding.viewpager.currentItem = 0
        binding.viewpager.setOnPageChangeListener(this)
        setPageViewIndicator()
        setUpClickListener()

           if (!Constants.IS_SUBSCRIBE) {
               binding.adView.visibility= View.VISIBLE
               var adView = AdView(this)
               val adRequest = AdRequest.Builder().build()

               adView.setAdSize(AdSize.BANNER)


               adView.adUnitId = getString(R.string.BannerAdsUnitId)
               binding.adView.loadAd(adRequest)
           }else{
               binding.adView.visibility= View.GONE
           }
    }

    private fun setUpView(oneMonth: Button, yearly: Button) {

        skuList.add(sku1)
        skuList.add(sku2)
        skuList.add(sku3)
        setupBillingClient(oneMonth,yearly)


    }

    private fun setupBillingClient(oneMonth: Button, yearly: Button) {

        acknowledgePurchaseResponseListener =
            AcknowledgePurchaseResponseListener { billingResult -> println(billingResult.responseCode) }
        billingClient =
            BillingClient.newBuilder(this).enablePendingPurchases().setListener(this).build()
        billingClient!!.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is setup successfully


                    Log.d("TAG", "onBillingSetupFinished: ")
                    loadAllSKUs(oneMonth,yearly)
                }
            }

            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                Log.d("TAG", "disconnect: ")
            }
        })
    }

    private fun loadAllSKUs(oneMonth: Button, yearly: Button) {

        //Toast.makeText(InnAppProducts.this, "", Toast.LENGTH_SHORT).show();
        if (billingClient!!.isReady) {
            //Toast.makeText(InnAppProducts.this, "billingclient ready", Toast.LENGTH_SHORT).show();
            val params = SkuDetailsParams.newBuilder()
                .setSkusList(skuList)
                .setType(BillingClient.SkuType.SUBS)
                .build()
            billingClient!!.querySkuDetailsAsync(
                params
            ) { billingResult, skuDetailsList ->
                //Toast.makeText(InnAppProducts.this, "inside query" + billingResult.getResponseCode(), Toast.LENGTH_SHORT).show();
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK
                    && skuDetailsList!!.isNotEmpty()
                ) {
                    for (skuDetailsObject in skuDetailsList) {
                        val skuDetails = skuDetailsObject as SkuDetails
                        //Toast.makeText(InnAppProducts.this, "" + skuDetails.getSku(), Toast.LENGTH_SHORT).show();
                        //System.out.println(skuDetails.getSku());
                        println(skuDetails.price)
                        Log.d("TAG", "loadAllSKUs: sku 1  "+skuDetailsList.size)
                        if (skuDetails.sku == sku1) {
                            mSkuDetails = skuDetails
//                            buttonBuyBasic.setEnabled(true)
//                            buttonBuyBasic.setText(
//                                buttonBuyBasic.getText().toString() + " " + skuDetails.price
//                            )

                            Log.d("TAG", "loadAllSKUs: sku 1  "+skuDetailsList.size)

                            oneMonth.setOnClickListener{
                                Log.d("TAG", "loadAllSKUs: sku 111")
                                val billingFlowParams = BillingFlowParams
                                    .newBuilder()
                                    .setSkuDetails(skuDetails)
                                    .build()
                                billingClient!!.launchBillingFlow(
                                    this@VipPremiumActivity,
                                    billingFlowParams
                                )
                            }

                        }
                        if (skuDetails.sku == sku2) {
                            Log.d("TAG", "loadAllSKUs: sku 2")
                            mSkuDetails = skuDetails
//                            buttonBuyStandard.setEnabled(true)
//                            buttonBuyStandard.setText(
//                                buttonBuyStandard.getText().toString() + " " + skuDetails.price
//                            )
                            yearly.setOnClickListener(View.OnClickListener {
                                val billingFlowParams = BillingFlowParams
                                    .newBuilder()
                                    .setSkuDetails(skuDetails)
                                    .build()
                                billingClient!!.launchBillingFlow(
                                    this@VipPremiumActivity,
                                    billingFlowParams
                                )
                            })
                        } else if (skuDetails.sku == sku3) {
                            mSkuDetails = skuDetails
//                            buttonBuyPremium.setEnabled(true)
//                            buttonBuyPremium.setText(
//                                buttonBuyPremium.getText().toString() + " " + skuDetails.price
//                            )
//                            sixMonth?.setOnClickListener(View.OnClickListener {
//                                val billingFlowParams = BillingFlowParams
//                                    .newBuilder()
//                                    .setSkuDetails(skuDetails)
//                                    .build()
//                                billingClient!!.launchBillingFlow(
//                                    this@VipPremiumActivity,
//                                    billingFlowParams
//                                )
//                            })
                        }
                    }
                }
            }
        } else Toast.makeText(this@VipPremiumActivity, "billingclient not ready", Toast.LENGTH_SHORT)
            .show()

    }

    private fun setUpClickListener() {

        binding.imageBack.setOnClickListener{
            finish()
        }
        binding.getStarted.setOnClickListener{
            showDialogs()
        }
    }
    private lateinit var dialog:Dialog



    private fun showDialogs() {
//        if (dialog != null) {
//            dialog.dismiss()
//        }
        dialog = Dialog(this@VipPremiumActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_payment)
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        dialog.setCanceledOnTouchOutside(false)
        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        //  int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);
        dialog.getWindow()?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)



       var oneMonth = dialog.findViewById<Button>(R.id.onemotnh)

       var yearly = dialog.findViewById<Button>(R.id.yearly)
       var manageSubscription = dialog.findViewById<Button>(R.id.manageSubscription)



        manageSubscription.setOnClickListener{
            if (SKUS.isNotEmpty()){
                val browserIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/account/subscriptions?sku="+SKUS+"&package=com.friendfinapp.dating")
                )
                startActivity(browserIntent)
            }else{
                Toast.makeText(this@VipPremiumActivity, "You Dont Have Any Subscription", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        setUpView(oneMonth,yearly)

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

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {


        val responseCode: Int = billingResult.getResponseCode()
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
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
}