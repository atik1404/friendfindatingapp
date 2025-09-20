package com.friendfinapp.dating.ui.profileactivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.friendfinapp.dating.R
import com.friendfinapp.dating.application.BaseActivity
import com.friendfinapp.dating.databinding.ActivityProfileBinding
import com.friendfinapp.dating.helper.Constants
import com.friendfinapp.dating.helper.Constants.USER_INFO
import com.friendfinapp.dating.helper.ProgressCustomDialog
import com.friendfinapp.dating.helper.SessionManager
import com.friendfinapp.dating.ui.saveprofile.SaveProfileActivity
import com.friendfinapp.dating.ui.signin.viewmodel.LogInViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView


class ProfileActivity : BaseActivity<ActivityProfileBinding>() {

    var customDialog: ProgressCustomDialog? = null

    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: LogInViewModel
    override fun viewBindingLayout(): ActivityProfileBinding =
        ActivityProfileBinding.inflate(layoutInflater)

    override fun initializeView(savedInstanceState: Bundle?) {
        setUpView()

        setUpClickListener()
    }

    override fun onResume() {
        super.onResume()
        loadUserProfile(USER_INFO.username.toString())
    }

    private fun setUpClickListener() {
        binding.imageBack.setOnClickListener {
            finish()
        }

        binding.edit.setOnClickListener {
            startActivity(
                Intent(
                    this@ProfileActivity,
                    SaveProfileActivity::class.java
                ).putExtra("profile", "profile")
            )
        }
    }

    private fun loadUserProfile(username: String) {

        Log.d("TAG", "loadUserProfile: " + username)
        customDialog?.show()
        viewModel.fetchProfile(username).observe(this) {

            Log.d("TAG", "loadUserProfile: " + it.data?.size)
            customDialog!!.dismiss()
            Constants.USER_PROFILE_INFO.addAll(it.data!!)
//            Constants.USER_BLOCKED.clear()
//            Constants.USER_BLOCKED.addAll(it.blockedUser!!)
//            if (!Constants.USER_OTHER_PROFILE_INFO.isNullOrEmpty()){
            if (it.data!!.size >= 1)
                binding.height.text = it.data!![0].value.toString()
            if (it.data!!.size >= 2)
                binding.weight.text = it.data!![1].value.toString()
            if (it.data!!.size >= 3)
                binding.body.text = it.data!![2].value.toString()
            if (it.data!!.size >= 4)
                binding.looking.text = it.data!![3].value.toString()
            if (it.data!!.size >= 5)
                binding.eyes.text = it.data!![4].value.toString()
            if (it.data!!.size >= 6)
                binding.hair.text = it.data!![5].value.toString()
            if (it.data!!.size >= 7)
                binding.smoking.text = it.data!![6].value.toString()
            if (it.data!!.size >= 8)
                binding.drinking.text = it.data!![7].value.toString()
            if (it.data!!.size >= 9)
                binding.title.text = it.data!![8].value.toString()
            if (it.data!!.size >= 10)
                binding.about.text = it.data!![9].value.toString()
            if (it.data!!.size >= 11)
                binding.whatareyou.text = it.data!![10].value.toString()
            if (it.data!!.size >= 12)
                binding.interest.text = it.data!![11].value.toString()

//            binding.height.text= it.data!![0].value.toString()
//            binding.weight.text= it.data!![1].value.toString()+" KG"
//            binding.body.text= it.data!![2].value.toString()
//            binding.looking.text= it.data!![3].value.toString()
//            binding.eyes.text= it.data!![4].value.toString()
//            binding.hair.text= it.data!![5].value.toString()
//            binding.smoking.text= it.data!![6].value.toString()
//                binding.title.text= Constants.USER_PROFILE_INFO[8].value.toString()
//                binding.about.text= Constants.USER_PROFILE_INFO[9].value.toString()
            // binding.genderLooking.text= it.data!![11].value.toString()
            // }


        }
    }

    private fun setUpView() {
        customDialog = ProgressCustomDialog(this)
        sessionManager = SessionManager(this)
        viewModel = ViewModelProvider(this).get(LogInViewModel::class.java)
        binding.name.text = USER_INFO.name.toString()
        binding.email.text = USER_INFO.email.toString()

        val strs = USER_INFO.birthdate.toString().split("T").toTypedArray()
        // binding.date.text = USER_INFO.birthdate.toString()
        binding.date.text = strs[0].toString()


        if (USER_INFO.gender == 1) {
            binding.gender.text = "Male"
        } else {
            binding.gender.text = "Female"
        }
        binding.language.text = "English"
        binding.country.text = USER_INFO.country.toString()
        binding.state.text = USER_INFO.state.toString()
        binding.city.text = USER_INFO.city.toString()



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