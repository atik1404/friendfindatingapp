package com.friendfinapp.dating.ui.signin

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.friendfinapp.dating.R
import com.friendfinapp.dating.databinding.ActivitySignInBinding
import com.friendfinapp.dating.helper.Constants
import com.friendfinapp.dating.helper.Constants.USER_PROFILE_INFO
import com.friendfinapp.dating.helper.ProgressCustomDialog
import com.friendfinapp.dating.helper.SessionManager
import com.friendfinapp.dating.ui.forgetpassword.ForgetPasswordActivity
import com.friendfinapp.dating.ui.landingpage.LandingActivity
import com.friendfinapp.dating.ui.saveprofile.SaveProfileActivity
import com.friendfinapp.dating.ui.signin.viewmodel.LogInViewModel
import com.friendfinapp.dating.ui.signup.SignUpActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import java.util.Arrays


class SignInActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignInBinding
    var customDialog: ProgressCustomDialog? = null

    private lateinit var sessionManager: SessionManager

    private lateinit var viewModel: LogInViewModel

    var check = true


    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val rcSignIn = 1

    //ads

    private var mInterstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)


        setUpAds()
        setUpView()
        setUpListener()
    }


    private fun setUpAds() {


        MobileAds.initialize(
           this
        ) { }

        val testDeviceIds = listOf("33BE2250B43518CCDA7DE426D04EE231")
        val configuration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
        MobileAds.setRequestConfiguration(configuration)
        var adRequest = AdRequest.Builder().build()
        InterstitialAd.load(this,getString(R.string.Interestitial_Ads_ID), adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d("TAG", adError.message)
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d("TAG", "Ad was loaded.")
                mInterstitialAd = interstitialAd
            }
        })
    }


    fun verifyInstallerId(context: Context): Boolean {
        // A list with valid installers package name
        val validInstallers: List<String> =
            ArrayList(Arrays.asList("com.android.vending", "com.google.android.feedback"))

        // The package name of the app that has installed your app
        val installer = context.packageManager.getInstallerPackageName(context.packageName)

        // true if your app has been downloaded from Play Store
        return installer != null && validInstallers.contains(installer)
    }
    private fun setUpView() {


        if (verifyInstallerId(this)){
            binding.continueGoogle.visibility=View.VISIBLE
            binding.linearLayout.visibility=View.VISIBLE
        }else{
            binding.continueGoogle.visibility=View.INVISIBLE
            binding.linearLayout.visibility=View.INVISIBLE
        }



        customDialog = ProgressCustomDialog(this)
        sessionManager = SessionManager(this)
        viewModel = ViewModelProvider(this).get(LogInViewModel::class.java)

        if (!Constants.IS_SUBSCRIBE) {
            binding.adView.visibility= View.VISIBLE
            val adView = AdView(this)
            val adRequest = AdRequest.Builder().build()

            adView.setAdSize(AdSize.BANNER)

            adView.adUnitId = getString(R.string.BannerAdsUnitId)
            binding.adView.loadAd(adRequest)
        }else{
            binding.adView.visibility= View.GONE
        }
    }

    private fun setUpListener() {


        binding.continueGoogle.setOnClickListener{
            googleSignIn()
        }
        binding.editEmail.editText!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(s: CharSequence, i: Int, i1: Int, i2: Int) {
                if (s != "" && binding.editPassword.editText!!.text.toString() != "") {
                    ViewCompat.setBackgroundTintList(
                        binding.logIn,
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                this@SignInActivity,
                                R.color.black
                            )
                        )
                    )
                    binding.logIn.setTextColor(
                        ContextCompat.getColor(
                            this@SignInActivity,
                            R.color.signInColor
                        )
                    )
                } else {
                    ViewCompat.setBackgroundTintList(
                        binding.logIn,
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                this@SignInActivity,
                                R.color.signInColor
                            )
                        )
                    )
                    binding.logIn.setTextColor(
                        ContextCompat.getColor(
                            this@SignInActivity,
                            R.color.black
                        )
                    )
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        binding.editPassword.editText!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(s: CharSequence, i: Int, i1: Int, i2: Int) {
                if (s != "" && binding.editEmail.editText!!.text.toString() != "") {
                    ViewCompat.setBackgroundTintList(
                        binding.logIn,
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                this@SignInActivity,
                                R.color.black
                            )
                        )
                    )
                    binding.logIn.setTextColor(
                        ContextCompat.getColor(
                            this@SignInActivity,
                            R.color.signInColor
                        )
                    )
                } else {
                    ViewCompat.setBackgroundTintList(
                        binding.logIn,
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                this@SignInActivity,
                                R.color.signInColor
                            )
                        )
                    )
                    binding!!.logIn.setTextColor(
                        ContextCompat.getColor(
                            this@SignInActivity,
                            R.color.black
                        )
                    )
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        binding.logIn.setOnClickListener(View.OnClickListener {
            customDialog!!.show()
            if (!isEmailValid(
                    binding.editEmail.editText!!.text.toString()
                ) || !validatePassword()
            ) {
                customDialog!!.dismiss()
                return@OnClickListener
            } else {
                signIn(binding.editEmail.editText?.text.toString().trim(),
                    binding.editPassword.editText?.text.toString().trim())
            }
        })
        binding.signUp.setOnClickListener {
            startActivity(
                Intent(
                    this@SignInActivity,
                    SignUpActivity::class.java
                )
            )
        }
        binding.forgetPassword.setOnClickListener {
//            startActivity(
//                Intent(
//                    this@SignInActivity,
//                    ForgetPasswordActivity::class.java
//                )
//            )
            if (!Constants.IS_SUBSCRIBE) {
                if (mInterstitialAd != null) {
                    mInterstitialAd?.show(this)

                    mInterstitialAd?.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                Log.d("TAG", "Ad was dismissed.")
                                startActivity(
                                    Intent(
                                        this@SignInActivity,
                                        ForgetPasswordActivity::class.java
                                    )
                                )
                            }


                            override fun onAdShowedFullScreenContent() {
                                Log.d("TAG", "Ad showed fullscreen content.")
                                mInterstitialAd = null
                            }
                        }
                } else {
                    startActivity(
                        Intent(
                            this@SignInActivity,
                            ForgetPasswordActivity::class.java
                        )
                    )
                }

            }else{
                startActivity(
                    Intent(
                        this@SignInActivity,
                        ForgetPasswordActivity::class.java
                    )
                )
            }
        }


        binding.editPassword.setEndIconOnClickListener {
            if (check) {
                check = false
                binding.editPassword.setEndIconDrawable(com.friendfinapp.dating.R.drawable.ic_hidden)
                binding.password.transformationMethod = null
            } else {
                check = true
                binding.editPassword.setEndIconDrawable(com.friendfinapp.dating.R.drawable.ic_show_eye)
                binding.password.transformationMethod = PasswordTransformationMethod()
            }
            binding.password.setSelection(binding.password.length())

        }

    }


    private fun googleSignIn() {
        val gso: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
               // .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, rcSignIn)

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == rcSignIn) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                perFormSocialSignIn(account)
                customDialog?.show()
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun perFormSocialSignIn(account: GoogleSignInAccount?) {
        if (account != null) {
            val personName: String? = account.displayName
            val personEmail: String? = account.email
            val personId: String? = account.id
            val personPhoto: Uri? = account.photoUrl

            Log.d("TAG", "perFormSocialSignIn: "+account.displayName)
            Log.d("TAG", "perFormSocialSignIn: "+account.email)
            Log.d("TAG", "perFormSocialSignIn: "+account.givenName)
            Log.d("TAG", "perFormSocialSignIn: "+account.id)


          //  requestSignInApi(personName, personEmail, personId, personPhoto)
            requestSignInApi(personEmail)

        }
    }

    private fun requestSignInApi(personEmail: String?) {

        viewModel.signInGoogleUser(personEmail!!).observe(this) {
            customDialog?.dismiss()
            it.status_code.let { statusCode ->
                if (statusCode == 121) {

                    startActivity(
                        Intent(
                            this@SignInActivity,
                            SignUpActivity::class.java
                        ).putExtra("email", personEmail)
                    )

                } else {
                    if (it.count == 1) {
                        sessionManager.login = true
                        Log.d("TAG", "signIn: " + it.data?.username)
                        Constants.USER_INFO.username = it.data?.username
                        Log.d("TAG", "signIn: " + Constants.USER_INFO.username)
                        Constants.USER_INFO.active = it.data?.active
                        Constants.USER_INFO.password = it.data?.password
                        Constants.USER_INFO.email = it.data?.email
                        Constants.USER_INFO.name = it.data?.name
                        Constants.USER_INFO.gender = it.data?.gender
                        Constants.USER_INFO.receiveEmails = it.data?.receiveEmails
                        Constants.USER_INFO.interestedIn = it.data?.interestedIn
                        Constants.USER_INFO.birthdate = it.data?.birthdate
                        Constants.USER_INFO.country = it.data?.country
                        Constants.USER_INFO.state = it.data?.state
                        Constants.USER_INFO.city = it.data?.city
                        Constants.USER_INFO.zipCode = it.data?.zipCode
                        Constants.USER_INFO.messageVerificationsLeft =
                            it.data?.messageVerificationsLeft
                        Constants.USER_INFO.languageId = it.data?.languageId
                        Constants.USER_INFO.userIP = it.data?.userIP
                        Constants.USER_INFO.tokenUniqueId = it.data?.tokenUniqueId
                        Constants.USER_INFO.profileSkin = it.data?.profileSkin
                        Constants.USER_ID = Constants.USER_INFO.username.toString()

                        //Log.d("TAG", "signIn: "+password.toString().trim())
                        sessionManager.setInfo(
                            "googleSignIn",
                            it.data?.email,
                            it.data?.username,
                            it.data?.name,
                            it.data?.interestedIn,
                            it.data?.gender,
                            it.data?.active,
                            it.data?.country,
                            it.data?.state,
                            it.data?.city,
                            it.data?.birthdate
                        )

                        checkSaveProfile(it.data?.username)


                    } else {
                        customDialog?.dismiss()
                        Toast.makeText(
                            this@SignInActivity,
                            "" + it.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            }
        }
    }

    private fun signIn(username: String, password: String) {


        viewModel.signInUser(username,password).observe(this) {

            if (it.count == 1) {
                sessionManager.login = true
                Log.d("BUR", "signIn1: " + it.data?.username)
                Constants.USER_INFO.username = it.data?.username
                Log.d("BUR", "signIn1: " + Constants.USER_INFO.username)
                Constants.USER_INFO.active = it.data?.active
                Constants.USER_INFO.password = it.data?.password
                Constants.USER_INFO.email = it.data?.email
                Constants.USER_INFO.name = it.data?.name
                Constants.USER_INFO.gender = it.data?.gender
                Constants.USER_INFO.receiveEmails = it.data?.receiveEmails
                Constants.USER_INFO.interestedIn = it.data?.interestedIn
                Constants.USER_INFO.birthdate = it.data?.birthdate
                Constants.USER_INFO.country = it.data?.country
                Constants.USER_INFO.state = it.data?.state
                Constants.USER_INFO.city = it.data?.city
                Constants.USER_INFO.zipCode = it.data?.zipCode
                Constants.USER_INFO.messageVerificationsLeft = it.data?.messageVerificationsLeft
                Constants.USER_INFO.languageId = it.data?.languageId
                Constants.USER_INFO.userIP = it.data?.userIP
                Constants.USER_INFO.tokenUniqueId = it.data?.tokenUniqueId
                Constants.USER_INFO.profileSkin = it.data?.profileSkin
                Constants.USER_ID = Constants.USER_INFO.username.toString()

                Log.d("TAG", "signIn: " + password.toString().trim())
                sessionManager.setInfo(
                    password.toString().trim(),
                    it.data?.email,
                    it.data?.username,
                    it.data?.name,
                    it.data?.interestedIn,
                    it.data?.gender,
                    it.data?.active,
                    it.data?.country,
                    it.data?.state,
                    it.data?.city,
                    it.data?.birthdate
                )

                checkSaveProfile(it.data?.username)


            } else {
                customDialog?.dismiss()
                Toast.makeText(this@SignInActivity, "" + it.message.toString(), Toast.LENGTH_SHORT)
                    .show()
                Toast.makeText(
                    this@SignInActivity,
                    "" + it.status_code.toString() + "  " + it.count.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun checkSaveProfile(username: String?) {
        viewModel.fetchProfile(username).observe(this) {
            customDialog!!.dismiss()
            if (it.count < 12) {
                startActivity(
                    Intent(
                        this@SignInActivity,
                        SaveProfileActivity::class.java
                    )
                )
                finish()
            } else {
                Constants.USER_PROFILE_INFO.clear()
                Constants.USER_PROFILE_INFO.addAll(it.data!!)
                Constants.USER_PROFILE_INFO_PICTURE.addAll(it.userimage!!)
                Constants.USER_BLOCKED.clear()
                Constants.USER_BLOCKED.addAll(it.blockedUser!!)

                if (!it.userimage.isNullOrEmpty()) {

                    Log.d("TAG", "checkSaveProfile: " + it.userimage!![0].userimage.toString())
                    sessionManager.setProfile(it.userimage!![0].userimage.toString())
                    Constants.myProfileImage = it.userimage!![0].userimage.toString()
                } else {
                    sessionManager.setProfile("")
                    Constants.myProfileImage = ""
                }

                Log.d("TAG", "checkSaveProfile: " + USER_PROFILE_INFO.size)
                Log.d("TAG", "checkSaveProfile: " + it.data!!.size)
                startActivity(
                    Intent(
                        this@SignInActivity,
                        LandingActivity::class.java
                    )
                )
                finish()
            }
        }
    }

    private fun isEmailValid(email: String): Boolean {
        val passwordInput = email.trim { it <= ' ' }
        return if (passwordInput == "") {
            binding.editEmail.error = "Field can't be empty"
            false
        } else {
            binding.editEmail.error = null
            true
        }
    }

    private fun validatePassword(): Boolean {
        val passwordInput = binding.editPassword.editText!!.text.toString().trim { it <= ' ' }
        return when {
            passwordInput == "" -> {
                binding.editPassword.error = "Field can't be empty"
                false
            }
            passwordInput.length < 10 -> {
                binding.editPassword.error = "Password needs to have minimum of 10 character"
                false
            }
            else -> {
                binding.editPassword.error = null
                true
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    override fun onResume() {
        super.onResume()
        setUpAds()
    }
}