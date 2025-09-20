package com.friendfinapp.dating.ui.splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.android.billingclient.api.*
import com.android.billingclient.api.BillingClient.BillingResponseCode.OK

import com.friendfinapp.dating.R
import com.friendfinapp.dating.application.BaseActivity
import com.friendfinapp.dating.databinding.ActivitySplashBinding
import com.friendfinapp.dating.helper.Constants
import com.friendfinapp.dating.helper.Constants.IS_SUBSCRIBE
import com.friendfinapp.dating.helper.Constants.SKUS
import com.friendfinapp.dating.helper.Constants.myProfileImage
import com.friendfinapp.dating.helper.NetworkCheckWorker
import com.friendfinapp.dating.helper.ProgressCustomDialog
import com.friendfinapp.dating.helper.SessionManager
import com.friendfinapp.dating.ui.landingpage.LandingActivity
import com.friendfinapp.dating.ui.saveprofile.SaveProfileActivity
import com.friendfinapp.dating.ui.signin.SignInActivity
import com.friendfinapp.dating.ui.signin.viewmodel.LogInViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import java.util.concurrent.TimeUnit


@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>(), PurchasesUpdatedListener {
    private lateinit var sessionManager: SessionManager
    var customDialog: ProgressCustomDialog? = null
    private lateinit var viewModel: LogInViewModel
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    override fun viewBindingLayout(): ActivitySplashBinding =
        ActivitySplashBinding.inflate(layoutInflater)

    override fun initializeView(savedInstanceState: Bundle?) {

        setUpView()


        if (sessionManager.login) {

            val googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this)

            if (googleSignInAccount != null) {
                viewModel.signInGoogleUser(googleSignInAccount.email!!).observe(this) {
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

                        sessionManager.login = false
                        Toast.makeText(
                            this@SplashActivity,
                            "Something Went Wrong! Please Close the app and start from again. Thanks.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                userInfoGet()
            }

        } else {
            val gso: GoogleSignInOptions =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_ids))
                    .requestEmail()
                    .build()
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
            mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, OnCompleteListener<Void?> {
                    // ...
                })
            val intent = Intent(this@SplashActivity, SignInActivity::class.java)

            val thread: Thread = object : Thread() {
                override fun run() {
                    try {
                        sleep(2000)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    } finally {
                        startActivity(intent)
                        finish()
                    }
                }
            }
            thread.start()
        }
        Constants.AUTHORIZATION_TOKEN = sessionManager.token ?: ""
    }


    private fun userInfoGet() {


        var username = sessionManager.username!!

        var password = sessionManager.password!!
        Log.d("TAG", "signIn22: " + username)
        Log.d("TAG", "signIn: " + password)
        viewModel.signInUser(username, password).observe(this) {


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
                Constants.USER_INFO.messageVerificationsLeft = it.data?.messageVerificationsLeft
                Constants.USER_INFO.languageId = it.data?.languageId
                Constants.USER_INFO.userIP = it.data?.userIP
                Constants.USER_INFO.tokenUniqueId = it.data?.tokenUniqueId
                Constants.USER_INFO.profileSkin = it.data?.profileSkin
                Constants.USER_ID = Constants.USER_INFO.username.toString()
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

                checkSaveProfile(username)
            } else {

                sessionManager.login = false
                Toast.makeText(
                    this@SplashActivity,
                    "Something Went Wrong! Please Close the app and start from again. Thanks.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun checkSaveProfile(username: String?) {
        viewModel.fetchProfile(username).observe(this) {
            // customDialog!!.dismiss()
            if (it.count < 12) {
                val intent2 = Intent(this@SplashActivity, SaveProfileActivity::class.java)
                startActivity(intent2)
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
                    myProfileImage = it.userimage!![0].userimage.toString()
                } else {
                    sessionManager.setProfile("")
                    myProfileImage = ""
                }


                val intent2 = Intent(this@SplashActivity, LandingActivity::class.java)
                startActivity(intent2)
                finish()
                Log.d("TAG", "checkSaveProfile: " + Constants.USER_PROFILE_INFO.size)
                Log.d("TAG", "checkSaveProfile: " + it.data!!.size)

            }
        }
    }


    private fun setUpView() {

        // Create a periodic work request (every 15 minutes)
        val networkCheckWorkRequest = PeriodicWorkRequest.Builder(
            NetworkCheckWorker::class.java, 15, TimeUnit.MINUTES
        ).build()

        // Enqueue the work request
        WorkManager.getInstance(this).enqueue(networkCheckWorkRequest)


        // Check network status directly in your activity
        if (!isConnected()) {
            // Show "No Internet" message in your activity
            showNoInternetMessage()
        }

        // Create a one-time work request (optional)
        startOneTimeNetworkCheckWorker()


        sessionManager = SessionManager(this)
        viewModel = ViewModelProvider(this).get(LogInViewModel::class.java)


        purchaseFinding()
    }

    // Check network status
    private fun isConnected(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

    private fun showNoInternetMessage() {
        // Implement your "No Internet" UI logic here, e.g., display a Snackbar or Toast
        Snackbar.make(
            findViewById(android.R.id.content),
            "No Internet Connection",
            Snackbar.LENGTH_LONG
        ).show()
    }

    // Create a one-time work request (optional)
    private fun startOneTimeNetworkCheckWorker() {
        val workRequest = OneTimeWorkRequest.Builder(NetworkCheckWorker::class.java).build()
        WorkManager.getInstance(this).enqueue(workRequest)
    }


    private var billingClient: BillingClient? = null
    private fun purchaseFinding() {

        billingClient = BillingClient
            .newBuilder(this)
            .enablePendingPurchases()
            .setListener(this)
            .build()
        billingClient!!.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == OK) {
                    Log.d("TAG", "Billing client successfully set up!")
                    // Query for existing user purchases
                    // Query for products for sale
                    queryPurchases()
                }
            }

            override fun onBillingServiceDisconnected() {
                Log.d("TAG", "Billing service disconnected")
                // Restart the connection with startConnection() so future requests don't fail.
            }
        })
    }


    fun queryPurchases() {


        billingClient!!.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        ) { billingResult, purchases -> // check billingResult
            // process returned purchase list, e.g. display the plans user owns
            if (purchases.isNullOrEmpty()) {
                Toast.makeText(this, "No existing in app purchases found", Toast.LENGTH_SHORT)
                    .show()
                Log.d("bur", "No existing in app purchases found.")
                IS_SUBSCRIBE = false
            } else {
                IS_SUBSCRIBE = true

                var skus = purchases[0].products[0].toString()

                // var skus= result.purchasesList!![0].skus[0].toString()
                SKUS = skus
                Toast.makeText(this, "Existing purchases: $purchases", Toast.LENGTH_SHORT).show()
                Log.d("bur", "Existing purchases: $purchases")
            }
        }


//        if (!billingClient!!.isReady) {
//            Log.e("TAG", "queryPurchases: BillingClient is not ready")
//        }
//        // Query for existing in app products that have been purchased. This does NOT include subscriptions.
//        val result = billingClient?.queryPurchases(BillingClient.SkuType.SUBS)
//
//
//
//        if (result?.purchasesList == null || result.purchasesList!!.isEmpty()) {
//
//            Log.d("TAG", "No existing in app purchases found.")
//            IS_SUBSCRIBE=false
//        } else {
//            IS_SUBSCRIBE=true
//            var skus= result.purchasesList!![0].skus[0].toString()
//            SKUS=skus
//
//            Log.d("TAG", "Existing purchases: ${result.purchasesList}")
//        }
    }

    override fun onPurchasesUpdated(p0: BillingResult, p1: MutableList<Purchase>?) {

    }
}