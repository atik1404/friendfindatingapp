package com.friendfinapp.dating.ui.landingpage

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

import com.friendfinapp.dating.R
import com.friendfinapp.dating.databinding.ActivityLandingBinding
import com.friendfinapp.dating.helper.Constants
import com.friendfinapp.dating.helper.Constants.IS_SUBSCRIBE
import com.friendfinapp.dating.helper.Constants.USER_INFO
import com.friendfinapp.dating.helper.InternetHelper
import com.friendfinapp.dating.helper.ProgressCustomDialog
import com.friendfinapp.dating.helper.SessionManager
import com.friendfinapp.dating.helper.showEventLog
import com.friendfinapp.dating.notification.ReceivedNotifications
import com.friendfinapp.dating.ui.TestActivity
import com.friendfinapp.dating.ui.individualsearch.IndividualSearchActivity
import com.friendfinapp.dating.ui.landingpage.fragments.chatfragment.ChatFragment
import com.friendfinapp.dating.ui.landingpage.fragments.profilefragment.ProfileFragment
import com.friendfinapp.dating.ui.landingpage.fragments.profilefragment.viewmodel.LogoutViewModel
import com.friendfinapp.dating.ui.landingpage.fragments.serachfragment.SearchFragment
import com.friendfinapp.dating.ui.landingpage.viewmodel.NotificationViewModel
import com.friendfinapp.dating.ui.privacypolicy.PrivacyPolicyActivity
import com.friendfinapp.dating.ui.settings.SettingsActivity
import com.friendfinapp.dating.ui.signin.SignInActivity
import com.friendfinapp.dating.ui.signin.viewmodel.LogInViewModel
import com.friendfinapp.dating.ui.vipactivity.VipPremiumActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*


class LandingActivity : AppCompatActivity() {

    private val fragment1: Fragment = SearchFragment()
    private val fragment2: Fragment = ChatFragment()
    private val fragment3: Fragment = ProfileFragment()

    //private val fragment4: Fragment = ProfileFragment()
    private val fm = supportFragmentManager
    private var active = fragment1

    private lateinit var sessionManager: SessionManager
    var customDialog: ProgressCustomDialog? = null
    private lateinit var viewModel: LogInViewModel
    private lateinit var viewModel2: LogoutViewModel

    public lateinit var binding: ActivityLandingBinding

    private lateinit var model: NotificationViewModel

    private var fromNotification = ""


    private var mInterstitialAd: InterstitialAd? = null


    private lateinit var internetHelper: InternetHelper

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_landing)

        instance = this

        val componentName = ComponentName(this, ReceivedNotifications::class.java)
        packageManager.setComponentEnabledSetting(
            componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )

        if (intent.getStringExtra("notification") != null) {
            fromNotification = intent.getStringExtra("notification")!!
        }
        // fm.beginTransaction().add(R.id.home_fragment, fragment4, "4").hide(fragment4).commit()
        fm.beginTransaction().add(R.id.home_fragment, fragment3, "3").hide(fragment3).commit()
        fm.beginTransaction().add(R.id.home_fragment, fragment2, "2").hide(fragment2).commit()
        fm.beginTransaction().add(R.id.home_fragment, fragment1, "1").commit()
        setUpBottomNav()

        setUpView()
        setUpAds()

        onNewIntent(intent)


        setUpClickListener()
        setUpNavView()

        getCurrentToken()

        if (fromNotification == "yes") {

            binding.homeContent.bottomNav.menu.findItem(R.id.nav_livechat).isChecked = true

            fm.beginTransaction().hide(active).show(fragment2).commit()
            active = fragment2
        } else {
            Log.d("TAG", "onBindViewHolder33: " + fromNotification)
        }
    }

    private fun getCurrentToken() {

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            Log.d("TAG", token.toString())

            updateToken(token)
        })

    }

    private fun updateToken(token: String?) {

        val userName = sessionManager.username.toString()

        model.notificationTokenUpdate(token!!, userName).observe(this) {

            sessionManager.setTokens(token)

        }


    }


    private fun setUpView() {

        internetHelper = InternetHelper(this)
        if (verifyInstallerId(this)) {
            LandingActivity.instance?.binding?.includeToolbar?.vip?.visibility = View.VISIBLE
        } else {
            LandingActivity.instance?.binding?.includeToolbar?.vip?.visibility = View.INVISIBLE
        }


        customDialog = ProgressCustomDialog(this)
        sessionManager = SessionManager(this)
        viewModel = ViewModelProvider(this).get(LogInViewModel::class.java)
        viewModel2 = ViewModelProvider(this).get(LogoutViewModel::class.java)
        model = ViewModelProvider(this).get(NotificationViewModel::class.java)
    }

    private fun setUpAds() {


        MobileAds.initialize(
            this
        ) { }

        val testDeviceIds = listOf("33BE2250B43518CCDA7DE426D04EE231")
        val configuration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
        MobileAds.setRequestConfiguration(configuration)
        var adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            this,
            getString(R.string.Interestitial_Ads_ID),
            adRequest,
            object : InterstitialAdLoadCallback() {
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


    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUpNavView() {


//        binding.includeNavDrawer.versionname.text =
//            "App Version " + BuildConfig.VERSION_NAME.toString()
        var profile = sessionManager.getProfileImage.toString()

        //   var imageBytes = profile.toByteArray()

        if (profile.isNotEmpty()) {

            Glide.with(this)
                .load(Constants.BaseUrl + profile)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.logo)

                .into(binding.includeNavDrawer.userProfileImage)
//            Glide.with(this)
//                .load(Base64.getDecoder().decode(imageBytes))
//                .placeholder(R.drawable.logo)
//                .into(binding.includeNavDrawer.userProfileImage)
        } else {
            Glide.with(this)
                .load(R.drawable.logo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.logo)
                .into(binding.includeNavDrawer.userProfileImage)
        }


        binding.includeNavDrawer.userName.text = USER_INFO.username
        binding.includeNavDrawer.searchLayout.setOnClickListener {
            handleDrawer()
            if (verifyInstallerId(this)) {
                LandingActivity.instance?.binding?.includeToolbar?.vip?.visibility = View.VISIBLE
            } else {
                LandingActivity.instance?.binding?.includeToolbar?.vip?.visibility = View.INVISIBLE
            }
            LandingActivity.instance?.binding?.includeToolbar?.address?.visibility = View.VISIBLE
            LandingActivity.instance?.binding?.includeToolbar?.homeDrawerMenu?.visibility =
                View.VISIBLE
            LandingActivity.instance?.binding?.includeToolbar?.imageBack?.visibility = View.GONE
            logEvent("nutrition_page")
            //BOTTOM_NAV_INDICATOR = 1
            // sessionManager.navIndicator = 3
            fm.beginTransaction().hide(active).show(fragment1).commit()
            active = fragment1
        }
        binding.includeNavDrawer.messageLayout.setOnClickListener {
            handleDrawer()

            if (internetHelper.isOnline()) {


                if (IS_SUBSCRIBE) {
                    if (verifyInstallerId(this)) {
                        LandingActivity.instance?.binding?.includeToolbar?.vip?.visibility =
                            View.VISIBLE
                    } else {
                        LandingActivity.instance?.binding?.includeToolbar?.vip?.visibility =
                            View.INVISIBLE
                    }
                    LandingActivity.instance?.binding?.includeToolbar?.address?.visibility =
                        View.VISIBLE
                    LandingActivity.instance?.binding?.includeToolbar?.homeDrawerMenu?.visibility =
                        View.VISIBLE
                    LandingActivity.instance?.binding?.includeToolbar?.imageBack?.visibility = View.GONE
                    logEvent("livechat_page")

                    binding.homeContent.bottomNav.menu[1].isChecked = true
//
//                    fm.beginTransaction().hide(active).show(fragment2).commit()
//                    active = fragment2
                    fm.beginTransaction().hide(active).show(fragment2).commit()
                    active = fragment2
                    ChatFragment.instance?.page = 0
                    ChatFragment.instance?.getChatData()

                } else {
                    if (mInterstitialAd != null) {
                        mInterstitialAd?.show(this)

                        mInterstitialAd?.fullScreenContentCallback =
                            object : FullScreenContentCallback() {
                                override fun onAdDismissedFullScreenContent() {
                                    Log.d("TAG", "Ad was dismissed.")
                                    if (verifyInstallerId(this@LandingActivity)) {
                                        LandingActivity.instance?.binding?.includeToolbar?.vip?.visibility =
                                            View.VISIBLE
                                    } else {
                                        LandingActivity.instance?.binding?.includeToolbar?.vip?.visibility =
                                            View.INVISIBLE
                                    }
                                    LandingActivity.instance?.binding?.includeToolbar?.address?.visibility =
                                        View.VISIBLE
                                    LandingActivity.instance?.binding?.includeToolbar?.homeDrawerMenu?.visibility =
                                        View.VISIBLE
                                    LandingActivity.instance?.binding?.includeToolbar?.imageBack?.visibility = View.GONE
                                    logEvent("livechat_page")

                                    binding.homeContent.bottomNav.menu[1].isChecked = true
//
//                    fm.beginTransaction().hide(active).show(fragment2).commit()
//                    active = fragment2
                                    fm.beginTransaction().hide(active).show(fragment2).commit()
                                    active = fragment2
                                    ChatFragment.instance?.page = 0
                                    ChatFragment.instance?.getChatData()
                                }


                                override fun onAdShowedFullScreenContent() {
                                    Log.d("TAG", "Ad showed fullscreen content.")
                                    mInterstitialAd = null
                                }
                            }
                    } else {
                        if (verifyInstallerId(this)) {
                            LandingActivity.instance?.binding?.includeToolbar?.vip?.visibility =
                                View.VISIBLE
                        } else {
                            LandingActivity.instance?.binding?.includeToolbar?.vip?.visibility =
                                View.INVISIBLE
                        }
                        LandingActivity.instance?.binding?.includeToolbar?.address?.visibility =
                            View.VISIBLE
                        LandingActivity.instance?.binding?.includeToolbar?.homeDrawerMenu?.visibility =
                            View.VISIBLE
                        LandingActivity.instance?.binding?.includeToolbar?.imageBack?.visibility = View.GONE
                        logEvent("livechat_page")

                        binding.homeContent.bottomNav.menu[1].isChecked = true
//
//                    fm.beginTransaction().hide(active).show(fragment2).commit()
//                    active = fragment2
                        fm.beginTransaction().hide(active).show(fragment2).commit()
                        active = fragment2
                        ChatFragment.instance?.page = 0
                        ChatFragment.instance?.getChatData()
                        Log.d("TAG", "The interstitial ad wasn't ready yet.")
                    }
                }


            } else {
                Toast.makeText(
                    this,
                    "No Internet Connection. Please try again!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.includeNavDrawer.profileLayout.setOnClickListener {
            handleDrawer()
            logEvent("workout_deals_page")
            LandingActivity.instance?.binding?.includeToolbar?.homeDrawerMenu?.visibility =
                View.GONE
            if (verifyInstallerId(this)) {
                LandingActivity.instance?.binding?.includeToolbar?.vip?.visibility = View.VISIBLE
            } else {
                LandingActivity.instance?.binding?.includeToolbar?.vip?.visibility = View.INVISIBLE
            }
            LandingActivity.instance?.binding?.includeToolbar?.address?.visibility = View.GONE
            LandingActivity.instance?.binding?.includeToolbar?.imageBack?.visibility = View.VISIBLE

            ProfileFragment.instance?.binding?.userName?.text = USER_INFO.username
            ProfileFragment.instance?.binding?.userEmail?.text = USER_INFO.email
            binding.homeContent.bottomNav.menu[2].isChecked = true
            fm.beginTransaction().hide(active).show(fragment3).commit()
            active = fragment3
        }
        binding.includeNavDrawer.settingsLayout.setOnClickListener {

            if (IS_SUBSCRIBE) {
                startActivity(
                    Intent(
                        this@LandingActivity,
                        SettingsActivity::class.java
                    )
                )
                handleDrawer()

            } else {
                if (mInterstitialAd != null) {
                    mInterstitialAd?.show(this)

                    mInterstitialAd?.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                Log.d("TAG", "Ad was dismissed.")

                                startActivity(
                                    Intent(
                                        this@LandingActivity,
                                        SettingsActivity::class.java
                                    )
                                )
                                handleDrawer()
                            }


                            override fun onAdShowedFullScreenContent() {
                                Log.d("TAG", "Ad showed fullscreen content.")
                                mInterstitialAd = null
                            }
                        }
                } else {
                    startActivity(
                        Intent(
                            this@LandingActivity,
                            SettingsActivity::class.java
                        )
                    )
                    handleDrawer()

                    Log.d("TAG", "The interstitial ad wasn't ready yet.")
                }
            }


        }

        binding.includeNavDrawer.privacyLayout.setOnClickListener {
            startActivity(
                Intent(
                    this@LandingActivity,
                    PrivacyPolicyActivity::class.java
                )
            )
            handleDrawer()
        }
        binding.includeNavDrawer.helpLayout.setOnClickListener {

            try {
                val intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "contactus@FriendFin.com"))
                intent.putExtra(Intent.EXTRA_SUBJECT, "Seeking For Help from App")
                intent.putExtra(Intent.EXTRA_TEXT, "your_text")
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                //TODO smth
            }

//            startActivity(
//                Intent(
//                    this@LandingActivity,
//                    HelpActivity::class.java
//                )
//            )
            handleDrawer()
        }
        binding.includeNavDrawer.logoutLayout.setOnClickListener {


            customDialog?.show()


            viewModel2.getLogOut().observe(this) {
                if (it.status_code == 200) {
                    customDialog?.dismiss()
                    Toast.makeText(this, "" + it.message.toString(), Toast.LENGTH_SHORT).show()
                    sessionManager.login = false
                    startActivity(Intent(this, SignInActivity::class.java))
                    LandingActivity.instance?.finish()
                }else{
                    customDialog?.dismiss()

                }
            }
        }
        binding.includeNavDrawer.exitLayout.setOnClickListener {

            finishAffinity()
        }


//        binding.includeNavDrawer.photosLayout.setOnClickListener{
//            Intent(
//                this@LandingActivity,
//                PhotosActivity::class.java
//            )
//            handleDrawer()
//        }
    }

    private fun setUpClickListener() {


        binding.includeToolbar.homeDrawerMenu.setOnClickListener { handleDrawer() }

        binding.includeToolbar.vip.setOnClickListener {
            startActivity(Intent(this@LandingActivity, VipPremiumActivity::class.java))
        }


        binding.includeToolbar.searchIndividual.setOnClickListener {
            startActivity(Intent(this@LandingActivity, IndividualSearchActivity::class.java))
        }
        binding.includeToolbar.address.setOnClickListener {
            startActivity(Intent(this@LandingActivity, IndividualSearchActivity::class.java))
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun setUpBottomNav() {
        binding.homeContent.bottomNav.setOnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.nav_nutrition -> {

                    if (verifyInstallerId(this)) {
                        LandingActivity.instance?.binding?.includeToolbar?.vip?.visibility =
                            View.VISIBLE
                    } else {
                        LandingActivity.instance?.binding?.includeToolbar?.vip?.visibility =
                            View.INVISIBLE
                    }
                    LandingActivity.instance?.binding?.includeToolbar?.address?.visibility =
                        View.VISIBLE
                    LandingActivity.instance?.binding?.includeToolbar?.homeDrawerMenu?.visibility =
                        View.VISIBLE
                    LandingActivity.instance?.binding?.includeToolbar?.imageBack?.visibility =
                        View.GONE
                    logEvent("nutrition_page")
                    //BOTTOM_NAV_INDICATOR = 1
                    // sessionManager.navIndicator = 3
                    fm.beginTransaction().hide(active).show(fragment1).commit()
                    active = fragment1
                    // NutritionFragment.instance?.backToTop()
                }

                R.id.nav_livechat -> {


                    if (internetHelper.isOnline()) {


//
                        if (verifyInstallerId(this)) {
                            LandingActivity.instance?.binding?.includeToolbar?.vip?.visibility =
                                View.VISIBLE
                        } else {
                            LandingActivity.instance?.binding?.includeToolbar?.vip?.visibility =
                                View.INVISIBLE
                        }
                        LandingActivity.instance?.binding?.includeToolbar?.address?.visibility =
                            View.VISIBLE
                        LandingActivity.instance?.binding?.includeToolbar?.homeDrawerMenu?.visibility =
                            View.VISIBLE
                        LandingActivity.instance?.binding?.includeToolbar?.imageBack?.visibility =
                            View.GONE
                        logEvent("livechat_page")
//
//                    fm.beginTransaction().hide(active).show(fragment2).commit()
//                    active = fragment2
                        fm.beginTransaction().hide(active).show(fragment2).commit()
                        active = fragment2
                        ChatFragment.instance?.page = 0
                        ChatFragment.instance?.getChatData()
//                    if (Constant.USER_DETAILS.usertype == "trainer") {
//                        logEvent("livechat_page")
//
//                        fm.beginTransaction().hide(active).show(fragment2).commit()
//                        active = fragment2
//                    } else {
//                        if (Constant.USER_DETAILS.issub) {
//                            logEvent("livechat_page")
//
//                            fm.beginTransaction().hide(active).show(fragment2).commit()
//                            active = fragment2
//                        } else {
//                            changeActivity(SubscriptionActivity::class.java, Bundle())
//                        }
//                    }
                        //CouponsDealFragment.instance?.backToTop()
                    } else {
                        Toast.makeText(
                            this,
                            "No Internet Connection. Please try again!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
//                R.id.nav_workout -> {
//
//                    logEvent("workout_deals_page")
//
//                    fm.beginTransaction().hide(active).show(fragment3).commit()
//                    active = fragment3
//                    //ProductDealsFragment.instance?.backToTop()
//                }
                R.id.nav_profile -> {

                    ProfileFragment.instance?.binding?.userName?.text = USER_INFO.username
                    ProfileFragment.instance?.binding?.userEmail?.text = USER_INFO.email
                    logEvent("workout_deals_page")
                    LandingActivity.instance?.binding?.includeToolbar?.homeDrawerMenu?.visibility =
                        View.GONE
                    LandingActivity.instance?.binding?.includeToolbar?.vip?.visibility = View.GONE
                    LandingActivity.instance?.binding?.includeToolbar?.address?.visibility =
                        View.GONE
                    LandingActivity.instance?.binding?.includeToolbar?.imageBack?.visibility =
                        View.VISIBLE
                    fm.beginTransaction().hide(active).show(fragment3).commit()
                    active = fragment3


                }

            }
            true
        }
//        when (BOTTOM_NAV_INDICATOR) {
//            1 -> binding.bottomNav.selectedItemId = R.id.nav_promo
//            2 -> binding.bottomNav.selectedItemId = R.id.nav_coupon
//            3 -> binding.bottomNav.selectedItemId = R.id.nav_product
//        }
    }

    fun logEvent(event: String) {
        val params = Bundle()
        params.putInt(event, 0)
        // mFirebaseAnalytics.logEvent(event, params)
        showEventLog(event)
    }

    companion object {

        @JvmStatic
        var instance: LandingActivity? = null
    }

    private fun handleDrawer() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    fun clickBackPress() {
        binding.includeToolbar.imageBack.setOnClickListener {
            if (verifyInstallerId(this)) {
                LandingActivity.instance?.binding?.includeToolbar?.vip?.visibility = View.VISIBLE
            } else {
                LandingActivity.instance?.binding?.includeToolbar?.vip?.visibility = View.INVISIBLE
            }
            LandingActivity.instance?.binding?.includeToolbar?.address?.visibility = View.VISIBLE
            LandingActivity.instance?.binding?.includeToolbar?.homeDrawerMenu?.visibility =
                View.VISIBLE
            LandingActivity.instance?.binding?.includeToolbar?.imageBack?.visibility = View.GONE
            logEvent("nutrition_page")
            //BOTTOM_NAV_INDICATOR = 1
            // sessionManager.navIndicator = 3
            binding.homeContent.bottomNav.menu[0].isChecked = true
            fm.beginTransaction().hide(active).show(fragment1).commit()
            active = fragment1
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
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

    override fun onResume() {
        super.onResume()
        setUpAds()
    }

}