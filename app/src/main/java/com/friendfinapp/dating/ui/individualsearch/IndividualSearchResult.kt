package com.friendfinapp.dating.ui.individualsearch

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.friendfinapp.dating.R
import com.friendfinapp.dating.application.BaseActivity
import com.friendfinapp.dating.databinding.ActivityIndividualSearchResultBinding
import com.friendfinapp.dating.ui.common.Common
import com.friendfinapp.dating.ui.individualsearch.adapter.IndividualSearchResultAdapter
import com.friendfinapp.dating.ui.individualsearch.serachresultmodel.IndividualSearchResultPostingModel
import com.friendfinapp.dating.ui.individualsearch.viewmodel.IndividualSearchResultViewModel
import com.friendfinapp.dating.ui.othersprofile.OthersUsersProfileActivity
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.friendfinapp.dating.helper.*


class IndividualSearchResult : BaseActivity<ActivityIndividualSearchResultBinding>(),
    IndividualSearchInterface {


    var value: String? = null

    companion object {

        @JvmStatic
        var instance: IndividualSearchResult? = null
    }

    override fun viewBindingLayout(): ActivityIndividualSearchResultBinding =
        ActivityIndividualSearchResultBinding.inflate(layoutInflater)

    private lateinit var viewModel: IndividualSearchResultViewModel
    var customDialog: ProgressCustomDialog? = null

    private lateinit var adapter: IndividualSearchResultAdapter
    private lateinit var sessionManager: SessionManager


    //ads

    private var mInterstitialAd: InterstitialAd? = null


    var userName = ""
    var minAge = ""
    var maxAge = ""
    var country = ""
    var city = ""
    var state = ""
    var eye = ""
    var drinking = ""
    var hair = ""
    var smoking = ""
    var bodyType = ""
    var lookingFor = ""
    var gender = ""
    var isOnline = false
    var isPhotoRequire = false


    var pageNo = 0


    private var canScroll = true
    private var totalitem = 0

    private var firstTime = true

    override fun initializeView(savedInstanceState: Bundle?) {
        instance = this

        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            userName = bundle.getString("userName").toString() // 1
            minAge = bundle.getString("minAge").toString() // 1
            maxAge = bundle.getString("maxAge").toString() // 1
            country = bundle.getString("country").toString() // 1
            city = bundle.getString("city").toString() // 1
            state = bundle.getString("state").toString() // 1
            eye = bundle.getString("eye").toString() // 1
            drinking = bundle.getString("drinking").toString() // 1
            hair = bundle.getString("hair").toString() // 1
            smoking = bundle.getString("smoking").toString() // 1
            bodyType = bundle.getString("bodyType").toString() // 1
            lookingFor = bundle.getString("lookingFor").toString() // 1
            gender = bundle.getString("gender").toString() // 1
            isOnline = bundle.getBoolean("isOnline") // 1
            isPhotoRequire = bundle.getBoolean("isPhotoRequire") // 1
        }

        setUpView()

        setUpClickListener()
        setUpRecyclerView()


        binding.recyclerSearch.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                val totalItem = layoutManager!!.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                val endHasBeenReached = lastVisibleItem + 1 >= totalItem
//                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                imm.hideSoftInputFromWindow(this, InputMethodManager.RESULT_UNCHANGED_SHOWN)
                if (totalItem >= 10 && endHasBeenReached && !Common.isDataLodingState) {

                    Common.isDataLodingState = true
                    onPageScrolled(
                        userName,
                        minAge,
                        maxAge,
                        country,
                        city,
                        state,
                        eye,
                        drinking,
                        hair,
                        smoking,
                        bodyType,
                        lookingFor,
                        gender,
                        isOnline,
                        isPhotoRequire
                    )
                }
            }
        })

        customDialog?.show()

        getSearchResult(
            userName,
            minAge,
            maxAge,
            country,
            city,
            state,
            eye,
            drinking,
            hair,
            smoking,
            bodyType,
            lookingFor,
            gender,
            isOnline,
            isPhotoRequire
        )
    }

    private fun setUpClickListener() {
        binding.imageBack.setOnClickListener {
            finish()
        }
    }

    private fun onPageScrolled(
        userName: String,
        minAge: String,
        maxAge: String,
        country: String,
        city: String,
        state: String,
        eye: String,
        drinking: String,
        hair: String,
        smoking: String,
        bodyType: String,
        lookingFor: String,
        gender: String,
        online: Boolean,
        photoRequire: Boolean
    ) {
        pageNo += 1
        var eyes = ""
        var drinkings = ""
        var hairs = ""
        var smokings = ""
        var bodyTypes = ""
        var lookingFors = ""
        var genders = ""


        genders = when (gender) {
            "Male" -> {
                "1"
            }

            else -> {
                "2"
            }
        }

        bodyTypes = if (bodyType == "Select") {
            ""
        } else {
            bodyType
        }

        lookingFors = if (lookingFor == "Select") {
            ""
        } else {
            lookingFor
        }
        eyes = if (eye == "Select") {
            ""
        } else {
            eye
        }
        hairs = if (hair == "Select") {
            ""
        } else {
            hair
        }

        smokings = if (smoking == "Select") {
            ""
        } else {
            smoking
        }

        drinkings = if (drinking == "Select") {
            ""
        } else {
            drinking
        }
        var postingModel: IndividualSearchResultPostingModel = IndividualSearchResultPostingModel(
            genders.toInt(),
            minAge.toInt(),
            maxAge.toInt(),
            country,
            state,
            city,
            photoRequire,
            userName,
            online,
            bodyTypes,
            lookingFors,
            eyes,
            hairs,
            smokings,
            drinkings,
            pageNo
        )

        viewModel.getIndividualSearch(postingModel, this)

    }

    private fun getSearchResult(
        userName: String,
        minAge: String,
        maxAge: String,
        country: String,
        city: String,
        state: String,
        eye: String,
        drinking: String,
        hair: String,
        smoking: String,
        bodyType: String,
        lookingFor: String,
        gender: String,
        online: Boolean,
        photoRequire: Boolean
    ) {

        var eyes = ""
        var drinkings = ""
        var hairs = ""
        var smokings = ""
        var bodyTypes = ""
        var lookingFors = ""
        var genders = ""


        genders = when (gender) {
            "Male" -> {
                "1"
            }

            else -> {
                "2"
            }
        }

        bodyTypes = if (bodyType == "Select") {
            ""
        } else {
            bodyType
        }

        lookingFors = if (lookingFor == "Select") {
            ""
        } else {
            lookingFor
        }
        eyes = if (eye == "Select") {
            ""
        } else {
            eye
        }
        hairs = if (hair == "Select") {
            ""
        } else {
            hair
        }

        smokings = if (smoking == "Select") {
            ""
        } else {
            smoking
        }

        drinkings = if (drinking == "Select") {
            ""
        } else {
            drinking
        }
        var postingModel: IndividualSearchResultPostingModel = IndividualSearchResultPostingModel(
            genders.toInt(),
            minAge.toInt(),
            maxAge.toInt(),
            country,
            state,
            city,
            photoRequire,
            userName,
            online,
            bodyTypes,
            lookingFors,
            eyes,
            hairs,
            smokings,
            drinkings,
            pageNo
        )

        viewModel.getIndividualSearch(postingModel, this)

        viewModel.individualSearchResult.observe(this, {

            Common.isDataLodingState = false
            binding.layoutLoader.visibility = View.GONE
            customDialog?.dismiss()

            if (firstTime) {
                it.data.let {
                    adapter.addDataForFirstTime(it!!)
                    firstTime = false
                }
            } else {
                it.data?.let { adapter.addData(it) }
            }
        })

        viewModel.endHasBeenReached.observe(this, {

            if (it) {
                binding.layoutLoader.visibility = View.GONE
                canScroll = false
            } else
                canScroll = true
        })
    }


    private fun setUpRecyclerView() {

        val layoutManager = GridLayoutManager(this, 2)
        binding.recyclerSearch.layoutManager = layoutManager
        // binding.recyclerSearch.isNestedScrollingEnabled = false


        adapter = IndividualSearchResultAdapter(this) { username, userimage ->

            var bundel = Bundle()
            value = userimage.toString()

            bundel.putString("username", username)
            bundel.putString("from", "2")

            changeActivity(
                OthersUsersProfileActivity::class.java,
                bundel
            )

//            if (Constants.IS_SUBSCRIBE) {
//                var bundel = Bundle()
//                value = userimage.toString()
//
//                bundel.putString("username", username)
//                bundel.putString("from", "2")
//
//                changeActivity(
//                    OthersUsersProfileActivity::class.java,
//                    bundel
//                )
//            } else {
//                if (mInterstitialAd != null) {
//                    mInterstitialAd?.show(this)
//
//                    mInterstitialAd?.fullScreenContentCallback =
//                        object : FullScreenContentCallback() {
//                            override fun onAdDismissedFullScreenContent() {
//                                Log.d("TAG", "Ad was dismissed.")
//                                var bundel = Bundle()
//                                value = userimage.toString()
//
//                                bundel.putString("username", username)
//                                bundel.putString("from", "2")
//
//                                changeActivity(
//                                    OthersUsersProfileActivity::class.java,
//                                    bundel
//                                )
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
//                    var bundel = Bundle()
//                    value = userimage.toString()
//
//                    bundel.putString("username", username)
//                    bundel.putString("from", "2")
//
//
////                    val userList: ArrayList<ImageSendHelper> = ArrayList<ImageSendHelper>()
////                    userList.add(ImageSendHelper(username, userimage))
////                    val bundle = Bundle()
////                    bundle.putParcelableArrayList("myUser", userList)
//                    changeActivity(OthersUsersProfileActivity::class.java, bundel)
//                    Log.d("TAG", "The interstitial ad wasn't ready yet.")
//                }
//            }


        }

        // adapter.setHasStableIds(true)
        // binding.recyclerSearch.hasFixedSize()
        binding.recyclerSearch.adapter = adapter

        // detectScroll()

    }

    private fun setUpView() {


        viewModel = ViewModelProvider(this).get(IndividualSearchResultViewModel::class.java)
        customDialog = this.let { ProgressCustomDialog(it) }
        sessionManager = this.let { SessionManager(it) }

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

    override fun onProductLoading() {
        binding.layoutLoader.visibility = View.VISIBLE
    }

    override fun onProductLoadingFinish() {
        binding.layoutLoader.visibility = View.GONE
    }

    override fun onProductLoadError(message: String) {
        Common.isDataLodingState = false
        Toast.makeText(this, "" + message, Toast.LENGTH_SHORT)
            .show()
    }
}