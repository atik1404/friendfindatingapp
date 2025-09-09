package com.friendfinapp.dating.ui.chatsearch

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.friendfinapp.dating.R
import com.friendfinapp.dating.databinding.ActivityChatSerachBinding
import com.friendfinapp.dating.helper.Constants
import com.friendfinapp.dating.helper.ProgressCustomDialog
import com.friendfinapp.dating.helper.SessionManager
import com.friendfinapp.dating.helper.changeActivity
import com.friendfinapp.dating.ui.chatroom.ChatRoomActivity
import com.friendfinapp.dating.ui.chatsearch.adapter.SearchResultAdapter
import com.friendfinapp.dating.ui.chatsearch.adapter.SearchSuggestionAdapter
import com.friendfinapp.dating.ui.chatsearch.model.SuggestionResponseModel
import com.friendfinapp.dating.ui.chatsearch.viewmodel.ChatSearchViewModel
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd

class ChatSerachActivity : AppCompatActivity() {


    private lateinit var binding:ActivityChatSerachBinding
    private var suggestions: List<SuggestionResponseModel.Data> = ArrayList()

    var customDialog: ProgressCustomDialog? = null
    private var mInterstitialAd: InterstitialAd? = null


    private lateinit var sessionManager: SessionManager
    private lateinit var adapter : SearchSuggestionAdapter
    private lateinit var searchResultAdapter : SearchResultAdapter

    private lateinit var viewModel: ChatSearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_chat_serach)

        setUpView()

        setUpSuggestionRecyclerView()
        setUpSearchResultRecyclerView()

        getSuggestionData()
        getSearchResult()

        setUpClickListener()


    }

    private fun setUpSearchResultRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvResult.layoutManager = layoutManager
        searchResultAdapter = SearchResultAdapter (this){username, userimage ->
            run {

//
//                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
//                imm!!.hideSoftInputFromWindow(binding.root.windowToken, 0)
//
//
//                binding.searchView.setQuery(it.toString(),false)
//                binding.rvSuggestions.visibility = View.GONE
//
//
//                getSerachResult(Constants.USER_INFO.username,it.toString())

//                (requireActivity() as SearchActivity).productList = productList
//                (requireActivity() as SearchActivity).loadFragment(SearchResultFragment())
//                (requireActivity() as SearchActivity).searchKey = title


                var fromUserName = sessionManager.username
                var bundel = Bundle()
                bundel.putString("toUserName", username)
                bundel.putString("toUserImage", userimage)
                bundel.putString("fromUserName", fromUserName)
                this.changeActivity(
                    ChatRoomActivity::class.java,
                    bundel
                )
                finish()
//                if (Constants.IS_SUBSCRIBE){
//
//
//                    var bundel = Bundle()
//                    bundel.putString("toUserName", username)
//                    bundel.putString("toUserImage", userimage)
//                    bundel.putString("fromUserName", fromUserName)
//                   this.changeActivity(
//                        ChatRoomActivity::class.java,
//                        bundel
//                    )
//                    finish()
//                }else {
//                    if (mInterstitialAd != null) {
//                        mInterstitialAd?.show(this)
//
//                        mInterstitialAd?.fullScreenContentCallback =
//                            object : FullScreenContentCallback() {
//                                override fun onAdDismissedFullScreenContent() {
//                                    Log.d("TAG", "Ad was dismissed.")
//                                    var bundel = Bundle()
//                                    bundel.putString("toUserName", username)
//                                    bundel.putString("toUserImage", userimage)
//                                    bundel.putString("fromUserName", fromUserName)
//                                    changeActivity(
//                                        ChatRoomActivity::class.java,
//                                        bundel
//                                    )
//                                    finish()
//                                }
//
//
//
//                                override fun onAdShowedFullScreenContent() {
//                                    Log.d("TAG", "Ad showed fullscreen content.")
//                                    mInterstitialAd = null
//                                }
//                            }
//                    }
//                    else {
//                        var bundel = Bundle()
//                        bundel.putString("toUserName", username)
//                        bundel.putString("toUserImage", userimage)
//                        bundel.putString("fromUserName", fromUserName)
//                        this.changeActivity(ChatRoomActivity::class.java, bundel)
//                        finish()
//                        Log.d("TAG", "The interstitial ad wasn't ready yet.")
//                    }
//                }

            }
        }
        searchResultAdapter.setHasStableIds(true)
        binding.rvResult.adapter = searchResultAdapter
    }


    private fun setUpSuggestionRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvSuggestions.layoutManager = layoutManager
        adapter = SearchSuggestionAdapter (this){
            run {


                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                imm!!.hideSoftInputFromWindow(binding.root.windowToken, 0)


                binding.searchView.setQuery(it.toString(),false)
                binding.rvSuggestions.visibility = View.GONE
                binding.rvResult.visibility = View.VISIBLE


                customDialog?.show()

                getSerachResult(Constants.USER_INFO.username,it.toString())

//                (requireActivity() as SearchActivity).productList = productList
//                (requireActivity() as SearchActivity).loadFragment(SearchResultFragment())
//                (requireActivity() as SearchActivity).searchKey = title


            }
        }
        adapter.setHasStableIds(true)
        binding.rvSuggestions.adapter = adapter
    }

    private fun getSerachResult(myUserName: String?, OtherUserName: String) {

        viewModel.getSearchResult(myUserName,OtherUserName)
    }

    private fun setUpClickListener() {

        binding.imageBack.setOnClickListener {
            finish()
        }



        val searchIcon = binding.searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_button)
        searchIcon.setColorFilter(Color.BLACK)
        val closeIcon = binding.searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
        closeIcon.setColorFilter(Color.DKGRAY)
        val searchAutoComplete: SearchView.SearchAutoComplete = binding.searchView.findViewById(androidx.appcompat.R.id.search_src_text)
        searchAutoComplete.setHintTextColor(Color.BLACK)
        searchAutoComplete.setTextColor(Color.BLACK)

        binding.searchView.setOnClickListener {
            binding.searchView.isIconified = false
        }


        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {


//                TOTAL_ITEM = ""
//                CheckedCheckBox = ""
//                PRICE_FILTER = ""
//                SELLER_STORE_FILTER = ""
//                CATEGORY_FILTER = ""
//                CATEGORY_SELECT_NAME = ""
//                STORE_SELECT_NAME = ""
//                PRICE_SELECT_NAME = ""
//                RESET_FILTER = ""
//                SEARCH_RESULT_STORE_DETAILS.clear()
//                SEARCH_RESULT_CATEGORY_DETAILS.clear()

                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                imm!!.hideSoftInputFromWindow(binding.root.windowToken, 0)
               // (requireActivity() as SearchActivity).productList = suggestions
                //(requireActivity() as SearchActivity).loadFragment(SearchResultFragment())
               // (requireActivity() as SearchActivity).searchKey = query

                //if (AUTH_TOKEN.isEmpty()) {
                  //  saveHistory(query)
              //  } else {
                //    viewModel.sendServerProductDataByKeyword(query)
               // }


                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {

                if (newText.isNotEmpty())
                    binding.rvResult.visibility = View.GONE
                    viewModel.getServerChatDataHintByKeyword(Constants.USER_INFO.username!!,newText)
                //SearchResultFragment.instance?.filterByKeyword(newText)

                if (binding.rvSuggestions.visibility != View.VISIBLE) {
                    binding.rvSuggestions.visibility = View.VISIBLE
                    Log.d("TAG", "onQueryTextChange:  vissible ")
                }

                if (newText.isEmpty() && binding.rvSuggestions.visibility == View.VISIBLE) {
                    binding.rvSuggestions.visibility = View.GONE
                    Log.d("TAG", "onQueryTextChange: not vissible ")
                }

                return true
            }
        })
    }

    private fun getSuggestionData() {


        viewModel.suggestionItem.observe(this,{
            it?.let {
                adapter.addData(it.data!!)

                Log.d("TAG", "onQueryTextChange: not vissible "+it.data.size)
                suggestions = it.data
            }
        })
    }

    private fun setUpView() {
        viewModel=ViewModelProvider(this).get(ChatSearchViewModel::class.java)
        customDialog =  ProgressCustomDialog(this)
        sessionManager = SessionManager(this)
//        adapter= SearchSuggestionAdapter(this){
//
//        }


        if (!Constants.IS_SUBSCRIBE) {

            binding.adView.visibility=View.VISIBLE
            val adView = AdView(this)
            val adRequest = AdRequest.Builder().build()

            adView.setAdSize(AdSize.BANNER)

            adView.adUnitId = getString(R.string.BannerAdsUnitId)
            binding.adView.loadAd(adRequest)
        }else{
            binding.adView.visibility=View.GONE
        }

    }

    private fun getSearchResult() {
        viewModel.searchResult.observe(this,{

            customDialog?.dismiss()
            it?.let {
                searchResultAdapter.addData(it.data!!)

                Log.d("TAG", "onQueryTextChange: not vissible "+it.data.size)

            }
        })
    }
}