package com.friendfinapp.dating.ui.reportanabuse

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.friendfinapp.dating.R
import com.friendfinapp.dating.application.BaseActivity
import com.friendfinapp.dating.databinding.ActivityReportAnAbuseBinding
import com.friendfinapp.dating.helper.Constants
import com.friendfinapp.dating.helper.InternetHelper
import com.friendfinapp.dating.helper.ProgressCustomDialog
import com.friendfinapp.dating.helper.SessionManager
import com.friendfinapp.dating.ui.reportanabuse.model.ReportAbusedPostingModel
import com.friendfinapp.dating.ui.reportanabuse.viewmodel.ReportViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView


class ReportAnAbuse : BaseActivity<ActivityReportAnAbuseBinding>() {
    var customDialog: ProgressCustomDialog? = null

    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: ReportViewModel

    var username = ""

    private lateinit var internetHelper: InternetHelper
    override fun viewBindingLayout(): ActivityReportAnAbuseBinding =
        ActivityReportAnAbuseBinding.inflate(layoutInflater)

    override fun initializeView(savedInstanceState: Bundle?) {
        customDialog = ProgressCustomDialog(this)
        sessionManager = SessionManager(this)
        viewModel = ViewModelProvider(this).get(ReportViewModel::class.java)

        internetHelper = InternetHelper(this)


//        val bundle :Bundle ?=intent.extras
        if (intent.getStringExtra("reportedUserName") != null) {
            username = intent.getStringExtra("reportedUserName").toString() // 1


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
        setUpClickListener()

    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun setUpClickListener() {


        binding.report.setOnClickListener {


            if (internetHelper.isOnline()) {

                if (!isNotValid(binding.edit.text.toString())) {
                    return@setOnClickListener
                } else {
                    customDialog?.show()
                    var model = ReportAbusedPostingModel(
                        Constants.USER_INFO.username,
                        username,
                        binding.edit.text.toString()
                    )

                    viewModel.reportAnAbuse(model, customDialog, this).observe(this, {
                        customDialog?.dismiss()
                        it.statusCode.let {
                            if (it == 200) {
                                Toast.makeText(this, "Reported successfully", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    })
                }
            } else {
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()
            }
        }


        binding.imageBack.setOnClickListener {
            finish()
        }
    }

    private fun isNotValid(email: String): Boolean {
        val passwordInput = email.trim { it <= ' ' }
        return if (passwordInput == "") {
            binding.edit.error = "Field can't be empty"
            false
        } else {
            binding.edit.error = null
            true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}