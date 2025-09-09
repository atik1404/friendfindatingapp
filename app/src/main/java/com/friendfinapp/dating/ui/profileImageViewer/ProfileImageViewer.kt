package com.friendfinapp.dating.ui.profileImageViewer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.friendfinapp.dating.R
import com.friendfinapp.dating.databinding.ActivityOthersUsersProfileBinding
import com.friendfinapp.dating.databinding.ActivityProfileImageViewerBinding
import com.friendfinapp.dating.helper.Constants
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

class ProfileImageViewer : AppCompatActivity() {
    var userimage = ""
    private lateinit var binding: ActivityProfileImageViewerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_image_viewer)

        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            userimage = bundle.getString("userimage").toString() // 1
        }


        setUpView()
        setUpClickListener()

    }

    private fun setUpClickListener() {
        binding.imageBack.setOnClickListener {
            finish()
        }
    }

    private fun setUpView() {

        Glide.with(this)
            .load(Constants.BaseUrl+userimage)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.friendfin_n)
            .into(binding.photoView)

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