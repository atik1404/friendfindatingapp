package com.friendfinapp.dating.ui.settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.friendfinapp.dating.R
import com.friendfinapp.dating.application.BaseActivity
import com.friendfinapp.dating.databinding.ActivitySettingsBinding
import com.friendfinapp.dating.helper.Constants
import com.friendfinapp.dating.ui.privacypolicy.PrivacyPolicyActivity
import com.friendfinapp.dating.ui.profileactivity.ProfileActivity
import com.friendfinapp.dating.ui.settingspersonal.PersonalSettingsActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

class SettingsActivity : BaseActivity<ActivitySettingsBinding>() {
    override fun viewBindingLayout(): ActivitySettingsBinding =
        ActivitySettingsBinding.inflate(layoutInflater)

    override fun initializeView(savedInstanceState: Bundle?) {


        setUpView()

        setUpClickListener()
    }

    private fun setUpView() {

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

    private fun setUpClickListener() {
        binding.imageBack.setOnClickListener {
            finish()
        }

        binding.privacy.setOnClickListener {
            startActivity(
                Intent(
                    this@SettingsActivity,
                    PrivacyPolicyActivity::class.java
                )
            )
        }

        binding.profile.setOnClickListener {
            startActivity(
                Intent(
                    this@SettingsActivity,
                    ProfileActivity::class.java
                )
            )
        }
        binding.personalSettings.setOnClickListener {
            startActivity(
                Intent(
                    this@SettingsActivity,
                    PersonalSettingsActivity::class.java
                )
            )
        }


    }
}