package com.friendfinapp.dating.ui.forgetpassword

import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.friendfinapp.dating.R
import com.friendfinapp.dating.databinding.ActivityForgetPasswordBinding
import com.friendfinapp.dating.helper.Constants
import com.friendfinapp.dating.helper.InternetHelper
import com.friendfinapp.dating.helper.ProgressCustomDialog
import com.friendfinapp.dating.ui.forgetpassword.viewmodel.ForgetPasswordViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import java.util.regex.Matcher
import java.util.regex.Pattern

class ForgetPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgetPasswordBinding
    private lateinit var customDialog: ProgressCustomDialog


    private lateinit var viewModel: ForgetPasswordViewModel

    private lateinit var internetHelper: InternetHelper

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_forget_password)
        setUpView()
        setUpListener()


        internetHelper = InternetHelper(this)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setUpListener() {

        binding.imageBack.setOnClickListener {
            finish()
        }
        binding.signIn.setOnClickListener {
            finish()
        }


//        binding.editEmail.editText?.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable) {}
//            override fun beforeTextChanged(
//                s: CharSequence, start: Int,
//                count: Int, after: Int
//            ) {
//            }
//
//            override fun onTextChanged(
//                s: CharSequence, start: Int,
//                before: Int, count: Int
//            ) {
//                if (s.isNotEmpty()) {
//                    ViewCompat.setBackgroundTintList(
//                        binding.logIn,
//                        ColorStateList.valueOf(
//                            ContextCompat.getColor(
//                                this@ForgetPasswordActivity,
//                                R.color.black
//                            )
//                        )
//                    )
//                    binding.logIn.setTextColor(
//                        ContextCompat.getColor(
//                            this@ForgetPasswordActivity,
//                            R.color.white
//                        )
//                    )
//                } else {
//                    ViewCompat.setBackgroundTintList(
//                        binding.logIn,
//                        ColorStateList.valueOf(
//                            ContextCompat.getColor(
//                                this@ForgetPasswordActivity,
//                                R.color.signInColor
//                            )
//                        )
//                    )
//                    binding.logIn.setTextColor(
//                        ContextCompat.getColor(
//                            this@ForgetPasswordActivity,
//                            R.color.black
//                        )
//                    )
//                }
//            }
//        })
        binding.forgetPasswordButton.setOnClickListener {


            if (internetHelper.isOnline()) {
                customDialog.show()

                if (!isEmailValid(
                        binding.editEmail.editText?.text.toString()
                    )
                ) {
                    customDialog.dismiss()
                    return@setOnClickListener
                } else {

                    viewModel.forgetPasswordChange(
                        binding.editEmail.editText?.text.toString().trim(), customDialog, this
                    ).observe(this, {
                        customDialog.dismiss()
                        if (it.status_code == 200) {
                            Toast.makeText(
                                this,
                                "Email sent to this account " + binding.editEmail.editText?.text.toString() + " successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })

            }
        }else{
            Toast.makeText(this,"No Internet Connection",Toast.LENGTH_SHORT).show()
            }

    }

}


private fun setUpView() {

    customDialog = ProgressCustomDialog(this)
    viewModel = ViewModelProvider(this).get(ForgetPasswordViewModel::class.java)

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

private fun isEmailValid(email: String): Boolean {
    val regExpn = ("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
            + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
            + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
            + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$")
    val emaill = email.trim { it <= ' ' }
    val inputStr: CharSequence = emaill
    val pattern: Pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE)
    val matcher: Matcher = pattern.matcher(inputStr)
    if (matcher.matches()) {
        binding.editEmail.error = null
        return true
    } else binding.editEmail.error = "Please Enter Valid Email!"
    return false
}
}