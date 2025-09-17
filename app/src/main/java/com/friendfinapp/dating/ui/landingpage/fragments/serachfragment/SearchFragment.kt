package com.friendfinapp.dating.ui.landingpage.fragments.serachfragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.friendfinapp.dating.R
import com.friendfinapp.dating.databinding.FragmentSearchBinding
import com.friendfinapp.dating.helper.Constants
import com.friendfinapp.dating.helper.Constants.IS_SUBSCRIBE
import com.friendfinapp.dating.helper.ProgressCustomDialog
import com.friendfinapp.dating.helper.SessionManager
import com.friendfinapp.dating.helper.changeActivity
import com.friendfinapp.dating.ui.common.Common
import com.friendfinapp.dating.ui.landingpage.LandingActivity
import com.friendfinapp.dating.ui.landingpage.fragments.serachfragment.adapter.SearchAdapter
import com.friendfinapp.dating.ui.landingpage.fragments.serachfragment.model.SearchPostingModel
import com.friendfinapp.dating.ui.landingpage.fragments.serachfragment.viewModel.SearchViewModel
import com.friendfinapp.dating.ui.othersprofile.OthersUsersProfileActivity
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback


class SearchFragment : Fragment() ,SearchInterface{


    var value: String? = null

    companion object {

        @JvmStatic
        var instance: SearchFragment? = null
    }
    lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: SearchViewModel

    var customDialog: ProgressCustomDialog? = null

    private lateinit var adapter: SearchAdapter
    private lateinit var sessionManager: SessionManager


    //ads

    private var mInterstitialAd: InterstitialAd? = null


    //paging
    private var page = 0
    private var canScroll = true
    private var totalitem=0

    private var firstTime = true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding= FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        instance=this
        //setUpAds()
        setUpRecyclerView()
        setUpView()

        binding.recyclerSearch.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                val totalItem = layoutManager!!.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                val endHasBeenReached = lastVisibleItem + 1 >= totalItem
                val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.RESULT_UNCHANGED_SHOWN)
                if (totalItem >= 10 && endHasBeenReached && !Common.isDataLodingState) {

                    Common.isDataLodingState = true
                    onPageScrolled()
                }
            }
        })

        customDialog?.show()
        var interest = sessionManager.interest



        var postingModel: SearchPostingModel = SearchPostingModel(interest,100,page,10)
        viewModel.getTopSerach(postingModel,this)
        Constants.USER_INFO.username?.let { viewModel.getTopSearchWithLocationAndInterest(it) }

        Constants.USER_INFO.username?.let { viewModel.getUserOnlineUpdate(it) }?.observe(viewLifecycleOwner) {
            // Toast.makeText(activity,"you are in Online",Toast.LENGTH_SHORT).show()
        }

//        viewModel.SearchData.observe(viewLifecycleOwner) {
//
//            Common.isDataLodingState = false
//            binding.layoutLoader.visibility = View.GONE
//            customDialog?.dismiss()
//
//            if (firstTime) {
//                it.data.let {
//                    adapter.addDataForFirstTime(it!!)
//                    firstTime = false
//                }
//            } else {
//                it.data?.let { adapter.addData(it) }
//            }
//        }

        viewModel.searchDataWithLocationAndInterest.observe(viewLifecycleOwner) {

           // Common.isDataLodingState = false
            binding.layoutLoader.visibility = View.GONE
            customDialog?.dismiss()

            if (firstTime) {
                it.data.let {
                    adapter.addDataForFirstTime(it!!)
                    //firstTime = false
                }
            } else {
                it.data?.let { adapter.addData(it) }
            }
        }

        viewModel.endHasBeenReached.observe(viewLifecycleOwner) {

            if (it) {
                binding.layoutLoader.visibility = View.GONE
                canScroll = false
            } else
                canScroll = true
        }

        //getTopSearchData()

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

    private fun setUpRecyclerView() {

        val layoutManager = GridLayoutManager(activity, 2)
        binding.recyclerSearch.layoutManager = layoutManager
       // binding.recyclerSearch.isNestedScrollingEnabled = false


        adapter= SearchAdapter(requireContext()){ username, userimage->


            var bundel = Bundle()
            value=userimage.toString()
            bundel.putString("username", username)
            bundel.putString("from", "1")
            // bundel.putString("userimage", userimage)
            requireActivity().changeActivity(
                OthersUsersProfileActivity::class.java,
                bundel
            )



        }

     //   adapter.setHasStableIds(true)
        binding.recyclerSearch.adapter = adapter

       // detectScroll()

    }



    private fun onPageScrolled() {
        page += 1
        var interest = sessionManager.interest



        var postingModel: SearchPostingModel = SearchPostingModel(interest,100,page,10)
        viewModel.getTopSerach(postingModel,this)
    }
//    private fun detectScroll() {
//
//        binding.recyclerSearch.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
//                val totalItem = layoutManager!!.itemCount
//                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
//                val endHasBeenReached = lastVisibleItem + 1 >= totalItem
//                //UIHelper.setupUI(requireActivity(), binding.root)
//                if (totalItem >= 10 && endHasBeenReached && canScroll) {
//                   // binding.layoutLoader.visibility = View.VISIBLE
//                       if (page==0){
//                           binding.layoutLoader.visibility = View.VISIBLE
//                       }
//                    page++
//                    var interest = sessionManager.interest
//
//
//
//                    var postingModel: SearchPostingModel = SearchPostingModel(interest,100,page,10)
//                    viewModel.getTopSerach(postingModel).observe(viewLifecycleOwner,{
//
//                        customDialog?.dismiss()
//                        it.data.let {
//                            adapter.addData(it!!)
//                            binding.layoutLoader.visibility = View.GONE
//                        }
//                    })
//                }
//            }
//        })
//    }
    private fun getTopSearchData() {


    }

    private fun setUpView() {



        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        customDialog = activity?.let { ProgressCustomDialog(it) }
        sessionManager = activity?.let { SessionManager(it) }!!

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

    override fun onProductLoading() {
        binding.layoutLoader.visibility = View.VISIBLE
    }

    override fun onProductLoadingFinish() {
        binding.layoutLoader.visibility = View.GONE
    }

    override fun onProductLoadError(message: String) {

        if (context!=null){
            Toast.makeText(LandingActivity.instance, ""+message, Toast.LENGTH_SHORT)
                .show()
        }

        Common.isDataLodingState = false

    }

}