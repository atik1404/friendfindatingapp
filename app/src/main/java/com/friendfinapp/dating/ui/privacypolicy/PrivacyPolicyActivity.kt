package com.friendfinapp.dating.ui.privacypolicy

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import androidx.databinding.DataBindingUtil
import com.friendfinapp.dating.R
import com.friendfinapp.dating.application.BaseActivity

import com.friendfinapp.dating.databinding.ActivityPrivacyPolicyBinding
import com.friendfinapp.dating.helper.Constants
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

class PrivacyPolicyActivity : BaseActivity<ActivityPrivacyPolicyBinding>() {
    override fun viewBindingLayout(): ActivityPrivacyPolicyBinding =
        ActivityPrivacyPolicyBinding.inflate(layoutInflater)

    override fun initializeView(savedInstanceState: Bundle?) {
        binding.imageBack.setOnClickListener {
            finish()
        }


        if (!Constants.IS_SUBSCRIBE) {
            binding.adView.visibility = View.VISIBLE
            val adView = AdView(this)
            val adRequest = AdRequest.Builder().build()

            adView.setAdSize(AdSize.BANNER)

            adView.adUnitId = getString(R.string.BannerAdsUnitId)
            binding.adView.loadAd(adRequest)
        } else {
            binding.adView.visibility = View.GONE
        }
    }
}