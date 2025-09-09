package com.friendfinapp.dating.ui.privacypolicy

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import androidx.databinding.DataBindingUtil
import com.friendfinapp.dating.R

import com.friendfinapp.dating.databinding.ActivityPrivacyPolicyBinding
import com.friendfinapp.dating.helper.Constants
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

class PrivacyPolicyActivity : AppCompatActivity() {

    private lateinit var binding:ActivityPrivacyPolicyBinding


    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_privacy_policy)

//        binding.webview.settings.javaScriptEnabled = true
//
//        binding.webview.webViewClient = object : WebViewClient() {
//            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
//                if (url != null) {
//                    view?.loadUrl(url)
//                }
//                return true
//            }
//        }
//      //  binding.webview.loadUrl("https://www.friendfin.com/Lesbian-Dating-Sites.aspx")
    //    binding.webview.loadUrl("https://sites.google.com/view/friendfinprivacy/home")
        binding.imageBack.setOnClickListener{
            finish()
        }


        if (!Constants.IS_SUBSCRIBE) {
            binding.adView.visibility=View.VISIBLE
            val adView = AdView(this)
            val adRequest = AdRequest.Builder().build()

            adView.setAdSize(AdSize.BANNER)

            adView.adUnitId = getString(R.string.BannerAdsUnitId)
            binding.adView.loadAd(adRequest)
        }else{
            binding.adView.visibility= View.GONE
        }
    }
}