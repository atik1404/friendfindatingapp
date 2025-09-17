package com.friendfinapp.dating.ui.landingpage.fragments.chatfragment

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.friendfinapp.dating.R
import com.friendfinapp.dating.databinding.FragmentChatBinding
import com.friendfinapp.dating.helper.Constants
import com.friendfinapp.dating.helper.Constants.IS_SUBSCRIBE
import com.friendfinapp.dating.helper.InternetHelper
import com.friendfinapp.dating.helper.ProgressCustomDialog
import com.friendfinapp.dating.helper.SessionManager
import com.friendfinapp.dating.helper.changeActivity
import com.friendfinapp.dating.ui.chatroom.ChatRoomActivity
import com.friendfinapp.dating.ui.chatsearch.ChatSearchActivity
import com.friendfinapp.dating.ui.landingpage.fragments.chatfragment.chatadapter.UserListAdapter
import com.friendfinapp.dating.ui.landingpage.fragments.chatfragment.responsemodel.ChatResponseModel
import com.friendfinapp.dating.ui.landingpage.fragments.chatfragment.viewmodel.ChatViewModel
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback


class ChatFragment : Fragment() {

    lateinit var binding: FragmentChatBinding

    lateinit var viewModel: ChatViewModel

    var customDialog: ProgressCustomDialog? = null

    private lateinit var adapter: UserListAdapter
    private lateinit var sessionManager: SessionManager
    var chatUserList: MutableList<ChatResponseModel.Data> = ArrayList()

    //ads

    private var mInterstitialAd: InterstitialAd? = null

    private lateinit var internetHelper: InternetHelper

    //paging
    var page = 0
    private var canScroll = true
    private var totalitem = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        instance = this

        setUpView()
        //setUpAds()
        setUpRecyclerView()
        //getChatData()
        getChatData()
        setUpClickListener()



        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { adapter.filterList(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { adapter.filterList(it) }
                return true
            }
        })
    }

    private fun setUpClickListener() {

//        binding.lin2.setOnClickListener {
//
//            startActivity(Intent(requireActivity(),ChatSearchActivity::class.java))
//        }


//        binding.search.setOnQueryTextListener(object :
//            SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String): Boolean {
//                filter(query)
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String): Boolean {
//                filter(newText)
//                return true
//            }
//        })
    }

    private fun filter(query: String) {
        val filterdNames: ArrayList<ChatResponseModel.Data> = ArrayList<ChatResponseModel.Data>()

        for (userItem in chatUserList) {
            if (userItem.toUsername!!.lowercase().contains(query.lowercase())) {
                filterdNames.add(userItem)
            }
        }
        //   adapter.filterList(filterdNames)

    }

    private fun setUpAds() {


        MobileAds.initialize(
            requireContext()
        ) { }

        val testDeviceIds = listOf("33BE2250B43518CCDA7DE426D04EE231")
        val configuration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
        MobileAds.setRequestConfiguration(configuration)
        var adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            requireContext(),
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


    private fun setUpView() {


        internetHelper = InternetHelper(requireContext())
        viewModel = ViewModelProvider(this).get(ChatViewModel::class.java)

        customDialog = activity?.let { ProgressCustomDialog(it) }
        sessionManager = activity?.let { SessionManager(it) }!!


        if (!IS_SUBSCRIBE) {
            binding.adView.visibility = View.VISIBLE
            val adView = AdView(requireContext())
            val adRequest = AdRequest.Builder().build()
            adView.setAdSize(AdSize.BANNER)
            adView.adUnitId = getString(R.string.BannerAdsUnitId)
            binding.adView.loadAd(adRequest)
        } else {
            binding.adView.visibility = View.GONE
        }
    }


    private fun setUpRecyclerView() {

        val layoutManager = LinearLayoutManager(activity)
        binding.recyclerChatUser.layoutManager = layoutManager
        // binding.recyclerSearch.isNestedScrollingEnabled = false


        adapter = UserListAdapter(requireContext()) { username, notificationToken, userimage ->


            if (internetHelper.isOnline()) {
                var fromUserName = sessionManager.username

                var bundel = Bundle()
                bundel.putString("toUserName", username)
                bundel.putString("toUserToken", notificationToken)
                bundel.putString("toUserImage", userimage)
                bundel.putString("fromUserName", fromUserName)
                requireActivity().changeActivity(
                    ChatRoomActivity::class.java,
                    bundel
                )
            } else {
                Toast.makeText(
                    requireContext(),
                    "No Internet Connection. Please try again!",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        //adapter.setHasStableIds(true)
        binding.recyclerChatUser.adapter = adapter
        adapter.notifyDataSetChanged()

//        detectScroll()

    }
    @RequiresApi(Build.VERSION_CODES.N)
    fun getChatData() {
        customDialog?.show()
        val username = sessionManager.username

        viewModel.getChatUser(username, page).observe(viewLifecycleOwner) { response ->
            customDialog?.dismiss()

            response.data?.let { newData ->
                // First load: Clear the list and add the data
                chatUserList.clear()
                val filteredData = newData.filterNot { newUser ->
                    chatUserList.any { existingUser -> existingUser.toUsername == newUser.toUsername }
                }

                if (filteredData.isNotEmpty()) {
                    adapter.addDataFirstTime(filteredData)
                }

                // Add the new (filtered) data to the list
                chatUserList.addAll(filteredData)
            }
        }
    }

//    private fun detectScroll() {
//
//        binding.recyclerChatUser.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            @RequiresApi(Build.VERSION_CODES.N)
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
//                val totalItem = layoutManager!!.itemCount
//                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
//                val endHasBeenReached = lastVisibleItem + 1 >= totalItem
//                //UIHelper.setupUI(requireActivity(), binding.root)
//                if (totalItem >= 10 && endHasBeenReached && canScroll) {
//                    // binding.layoutLoader.visibility = View.VISIBLE
//                    page++
//                    var interest = sessionManager.interest
//                    var username = sessionManager.username
//                    viewModel.getChatUser(username, page).observe(viewLifecycleOwner) {
//                        customDialog?.dismiss()
//                        it.data.let {
//                            chatUserList.clear()
//                            adapter.addData(it!!)
//                            chatUserList.addAll(it)
//                        }
//                    }
//                }
//            }
//        })
//    }

//    @RequiresApi(Build.VERSION_CODES.N)
//    fun getChatData() {
//        customDialog?.show()
//        var username = sessionManager.username
//        viewModel.getChatUser(username, page).observe(viewLifecycleOwner) {
//            customDialog?.dismiss()
//            it.data.let {
//                chatUserList.clear()
//                adapter.addDataFirstTime(it!!)
//                chatUserList.addAll(it)
//            }
//        }
//    }


    @RequiresApi(Build.VERSION_CODES.N)
    fun getChatDataWhenBackFromChatRoom() {
        page = 0
        getChatData()
    }

//    @RequiresApi(Build.VERSION_CODES.N)
//    fun getChatDataWhenBackFromChatRoom() {
//        //customDialog?.show()
//        var username = sessionManager.username
//        viewModel.getChatUser(username, page).observe(viewLifecycleOwner) {
//            //customDialog?.dismiss()
//            it.data.let {
//                chatUserList.clear()
//
//                adapter.addDataFirstTime(it!!)
//                chatUserList.addAll(it)
//            }
//        }
//    }

    companion object {
        @JvmStatic
        var instance: ChatFragment? = null
    }
}