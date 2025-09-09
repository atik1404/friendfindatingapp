package com.friendfinapp.dating.ui.landingpage.fragments.profilefragment

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.multidex.BuildConfig
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

import com.friendfinapp.dating.R
import com.friendfinapp.dating.databinding.FragmentProfileBinding
import com.friendfinapp.dating.helper.Constants
import com.friendfinapp.dating.helper.Constants.IS_SUBSCRIBE
import com.friendfinapp.dating.helper.Constants.myProfileImage
import com.friendfinapp.dating.helper.ProgressCustomDialog
import com.friendfinapp.dating.helper.SessionManager
import com.friendfinapp.dating.helper.changeActivity
import com.friendfinapp.dating.ui.landingpage.LandingActivity
import com.friendfinapp.dating.ui.landingpage.fragments.profilefragment.viewmodel.LogoutViewModel
import com.friendfinapp.dating.ui.profileImageViewer.ProfileImageViewer
import com.friendfinapp.dating.ui.profileactivity.ProfileActivity
import com.friendfinapp.dating.ui.settings.SettingsActivity
import com.friendfinapp.dating.ui.signin.SignInActivity
import com.friendfinapp.dating.ui.uploadphoto.UploadPhotoActivity
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import java.util.*


class ProfileFragment : Fragment() {


    lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: LogoutViewModel
    private lateinit var sessionManager: SessionManager
    var customDialog: ProgressCustomDialog? = null


    //ads

    private lateinit var mGoogleSignInClient: GoogleSignInClient

    private var mInterstitialAd: InterstitialAd? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        instance = this
        setUpView()
        setUpAds()

        setUpClickListener()

    }
    private fun setUpAds() {


        MobileAds.initialize(
            requireContext()
        ) { }

        val testDeviceIds = listOf("33BE2250B43518CCDA7DE426D04EE231")
        val configuration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
        MobileAds.setRequestConfiguration(configuration)
        var adRequest = AdRequest.Builder().build()
        InterstitialAd.load(requireContext(),getString(R.string.Interestitial_Ads_ID), adRequest, object : InterstitialAdLoadCallback() {
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
    private fun setUpClickListener() {

        binding.userProfileImage.setOnClickListener {
            var profile = sessionManager.getProfileImage.toString()

            startActivity(
                Intent(activity, ProfileImageViewer::class.java).putExtra("userimage",myProfileImage))

        }



        binding.profile.setOnClickListener {
            requireActivity().changeActivity(ProfileActivity::class.java, Bundle())
//            if (IS_SUBSCRIBE) {
//                requireActivity().changeActivity(ProfileActivity::class.java, Bundle())
//            }else{
//                if (mInterstitialAd != null) {
//                    mInterstitialAd?.show(requireActivity())
//
//                    mInterstitialAd?.fullScreenContentCallback =
//                        object : FullScreenContentCallback() {
//                            override fun onAdDismissedFullScreenContent() {
//                                requireActivity().changeActivity(ProfileActivity::class.java, Bundle())
//                            }
//
//
//
//                            override fun onAdShowedFullScreenContent() {
//                                Log.d("TAG", "Ad showed fullscreen content.")
//                                mInterstitialAd = null
//                            }
//                        }
//                } else {
//                    requireActivity().changeActivity(ProfileActivity::class.java, Bundle())
//                    Log.d("TAG", "The interstitial ad wasn't ready yet.")
//                }
//            }
        }



        LandingActivity.instance?.clickBackPress()


        binding.uploadPhoto.setOnClickListener {

            requireActivity().changeActivity(UploadPhotoActivity::class.java, Bundle())
        }

        binding.logout.setOnClickListener {
            customDialog?.show()
            val googleSignInAccount = GoogleSignIn.getLastSignedInAccount(requireActivity())
            if (googleSignInAccount!=null){
                val gso: GoogleSignInOptions =
                    GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_ids))
                        .requestEmail()
                        .build()
                mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
                mGoogleSignInClient.signOut()
                    .addOnCompleteListener(requireActivity(), OnCompleteListener<Void?> {
                        customDialog?.dismiss()
                        Toast.makeText(activity, "Sign out Successful" , Toast.LENGTH_SHORT).show()
                        sessionManager.login = false
                        startActivity(Intent(activity, SignInActivity::class.java))
                        LandingActivity.instance?.finish()
                    })
            }else{
                viewModel.getLogOut().observe(viewLifecycleOwner, {
                    if (it.status_code == 200) {
                        customDialog?.dismiss()
                        Toast.makeText(activity, "" + it.message.toString(), Toast.LENGTH_SHORT).show()
                        sessionManager.login = false
                        startActivity(Intent(activity, SignInActivity::class.java))
                        LandingActivity.instance?.finish()
                    }
                })
            }

        }

        binding.contactUS.setOnClickListener {
            try {
                val intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "contactus@FriendFin.com"))
                intent.putExtra(Intent.EXTRA_SUBJECT, "Seeking For Help from App")
                intent.putExtra(Intent.EXTRA_TEXT, "your_text")
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                //TODO smth
            }
        }

        binding.setting.setOnClickListener{
            requireActivity().changeActivity(SettingsActivity::class.java, Bundle())
        }

        binding.shareApp.setOnClickListener {
            try {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "FriendFin")
                var shareMessage = "\nLet me recommend you this application\n\n"
                shareMessage =
                    """
                    ${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}
                    
                    
                    """.trimIndent()
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                startActivity(Intent.createChooser(shareIntent, "choose one"))
            } catch (e: Exception) {
                //e.toString();
            }
        }

        binding.rateApp.setOnClickListener {
            val uri = Uri.parse("market://details?id=com.friendfinapp.dating")
            val myAppLinkToMarket = Intent(Intent.ACTION_VIEW, uri)
            try {
                startActivity(myAppLinkToMarket)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(activity, " unable to find market app", Toast.LENGTH_LONG).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUpView() {

        viewModel = ViewModelProvider(this).get(LogoutViewModel::class.java)

        customDialog = activity?.let { ProgressCustomDialog(it) }
        sessionManager = activity?.let { SessionManager(it) }!!

        var profile = sessionManager.getProfileImage.toString()

        var imageBytes = profile.toByteArray()
        //        val imageByteArray: ByteArray = Base64.getDecoder().decode(imageBytes)
//        val decodedString = String(imageByteArray)

//        val bmp = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes!!.size)
//        holder.binding.userProfile.setImageBitmap(bmp)

        //  Log.d("TAG", "onBindViewHolder: "+decodedString)
if (profile.isNotEmpty()){
    context?.let {
        Glide.with(it)
            .load(Constants.BaseUrl+profile)
            .placeholder(R.drawable.logo)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .skipMemoryCache(true)
            .into(binding.userProfileImage)
    }
}else{
    Glide.with(this)
        .load(R.drawable.logo)
        .placeholder(R.drawable.logo)
        .into(binding.userProfileImage)
}



        if (!IS_SUBSCRIBE) {
            binding.adView.visibility=View.VISIBLE
            val adView = AdView(requireContext())
            val adRequest = AdRequest.Builder().build()

            adView.setAdSize(AdSize.BANNER)

            adView.adUnitId = getString(R.string.BannerAdsUnitId)
            binding.adView.loadAd(adRequest)
        }else{
            binding.adView.visibility=View.GONE
        }

    }

    override fun onResume() {
        super.onResume()
//        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        (activity as LandingActivity?)!!

    }


    companion object {

        @JvmStatic
        var instance: ProfileFragment? = null
    }
}