package com.friendfinapp.dating.ForwardMessaging;


import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.friendfinapp.dating.R
import com.friendfinapp.dating.databinding.ForwardMessageBinding
import com.friendfinapp.dating.helper.Constants.IS_SUBSCRIBE
import com.friendfinapp.dating.helper.Constants.myProfileImage
import com.friendfinapp.dating.helper.FileUtils
import com.friendfinapp.dating.helper.InternetHelper
import com.friendfinapp.dating.helper.ProgressCustomDialog
import com.friendfinapp.dating.helper.SessionManager
import com.friendfinapp.dating.helper.changeActivity
import com.friendfinapp.dating.notification.APIService
import com.friendfinapp.dating.notification.APIService2
import com.friendfinapp.dating.notification.Client
import com.friendfinapp.dating.notification.Data
import com.friendfinapp.dating.notification.MyResponse
import com.friendfinapp.dating.notification.Sender3
import com.friendfinapp.dating.ui.chatroom.ChatRoomActivity
import com.friendfinapp.dating.ui.chatsearch.ChatSearchActivity
import com.friendfinapp.dating.ui.landingpage.fragments.chatfragment.responsemodel.ChatResponseModel
import com.friendfinapp.dating.ui.landingpage.fragments.chatfragment.viewmodel.ChatViewModel
import com.friendfinapp.dating.ui.othersprofile.model.ForwardMessagePostingModel
import com.friendfinapp.dating.ui.othersprofile.model.MessageViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.util.Objects

class MessageForward : AppCompatActivity() {
    companion object{
        var shouldReloadChatRoom = false
    }
    private lateinit var messageViewModel: MessageViewModel

    private lateinit var binding: ForwardMessageBinding
    private lateinit var viewModel: ChatViewModel
    private lateinit var sessionManager: SessionManager
    private lateinit var internetHelper: InternetHelper
    private lateinit var adapter: ForwardUserListAdapter
    private var customDialog: ProgressCustomDialog? = null
    private var chatUserList: MutableList<ChatResponseModel.Data> = ArrayList()
    private var page = 0
    private var canScroll = true
    private var mInterstitialAd: InterstitialAd? = null
    var toUserToken = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ForwardMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initialize()
        setupRecyclerView()
        setupAds()
        toUserToken = intent.getStringExtra("toUserToken").toString()
        loadChatUsers()
        setupClickListeners()













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

    private fun initialize() {
        viewModel = ViewModelProvider(this).get(ChatViewModel::class.java)
        messageViewModel = ViewModelProvider(this).get(MessageViewModel::class.java)
        sessionManager = SessionManager(this)
        internetHelper = InternetHelper(this)
        customDialog = ProgressCustomDialog(this)

        if (!IS_SUBSCRIBE) {
            setupBannerAds()
        } else {
            binding.adView.visibility = View.GONE
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerChatUser.layoutManager = LinearLayoutManager(this)
        ForwadCompaionList.userForwardList.clear()
        adapter = ForwardUserListAdapter(this) { username, notificationToken, userImage ->
            if (internetHelper.isOnline()) {
                val bundle = Bundle().apply {
                    putString("toUserName", username)
                    putString("toUserToken", notificationToken)
                    putString("toUserImage", userImage)
                    putString("fromUserName", sessionManager.username)
                }
                changeActivity(ChatRoomActivity::class.java, bundle)
            } else {
                Toast.makeText(
                    this,
                    "No Internet Connection. Please try again!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.recyclerChatUser.adapter = adapter
        adapter.notifyDataSetChanged()
//        setupScrollListener()
    }


    private fun setupScrollListener() {
        binding.recyclerChatUser.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val endReached = lastVisibleItemPosition + 1 >= totalItemCount
                if (totalItemCount >= 10 && endReached && canScroll) {
                    page++
                    loadChatUsers()
                }
            }
        })
    }

//    @RequiresApi(Build.VERSION_CODES.N)
//    private fun loadChatUsers() {
//        customDialog?.show()
//        viewModel.getChatUser(sessionManager.username, page).observe(this) {
//            customDialog?.dismiss()
//            it.data?.let { users ->
//                adapter.addData(users)
//                chatUserList.addAll(users)
//            }
//        }
//    }

    private fun loadChatUsers() {
        customDialog?.show()
        viewModel.getChatUser(sessionManager.username, 0).observe(this) { response ->
            customDialog?.dismiss()

            response.data?.let { users ->
                adapter.addData(users)
                chatUserList.addAll(users)
            } ?: run {
                Toast.makeText(this, "No users found.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupAds() {
        MobileAds.initialize(this) {}
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            this,
            getString(R.string.Interestitial_Ads_ID),
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    mInterstitialAd = null
                }
            }
        )
    }

    private fun setupBannerAds() {
        binding.adView.visibility = View.VISIBLE
        val adView = AdView(this).apply {
//            adSize = AdSize.BANNER
            adUnitId = getString(R.string.BannerAdsUnitId)
        }
        binding.adView.loadAd(AdRequest.Builder().build())
    }

    private fun setupClickListeners() {
//        binding.lin2.setOnClickListener {
//            startActivity(Intent(this, ChatSearchActivity::class.java))
//        }

        binding.sendFab.setOnClickListener {
            forwardMessageToMultipleUser()
            Log.e("button","Button clicked")
        }
    }

    private fun sendMessageWithTextMessage(
        message: String,
        toUserName: String,
        tousernames: String
    ) {
        //  Toast.makeText(this, "Image uri  found."+uri, Toast.LENGTH_SHORT).show()
        //   var imageStream = contentResolver.openInputStream(imageUri!!)

        var sessionManager = SessionManager(this)

        var myUserName = sessionManager.username
        var sendUserName = toUserName.trim()

//        val sendMessage: SendMessagePostingModel =
//            SendMessagePostingModel(myUserName, sendUserName, message, 0, 0)


        try {

            //  val file = File(Objects.requireNonNull(FileUtils.getPath(this, imageUri)))


            //   val image = FileUtils.getPath(this, imageUri)


            messageViewModel = ViewModelProvider(this).get(MessageViewModel::class.java)

            messageViewModel.getMessageWithOnlyText(myUserName!!, tousernames, message, 0, 0)
                .observe(this) {
                    if (it.status_code == 200) {
                        customDialog?.dismiss()

                        getUserToken(toUserName.trim(), message, tousernames)
                        //binding.edit.requestFocus()
                        // Get the InputMethodManager instance

                        // Get the InputMethodManager instance
                        // Get the InputMethodManager instance

//                        val handler = Handler()
//                        handler.postDelayed(Runnable {
//                            // TODO - Here is my logic
//                            val view = this.currentFocus
//                            val methodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                            assert(view != null)
//                            methodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
//
//                        }, 100)


//                        val inputMethodManager =
//                            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//                        inputMethodManager.toggleSoftInputFromWindow(
//                            binding.edit.applicationWindowToken,
//                            InputMethodManager.SHOW_FORCED,
//                            0
//                        )


                    } else {
                        Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: Exception) {

        }


    }

    private fun getUserToken(username: String?, message: String, tousernames: String) {

        Log.d("TAG", "getUserToken: " + username)
        messageViewModel.getUserNotificationToken(username).observe(this) {

            Log.d("TAG", "getUserToken: " + it.data.toString())
            if (it.data.isNullOrEmpty()) {
                sendNotification(toUserToken, message, tousernames)
            } else {

                Log.d("TAG", "getUserToken: " + it.data.toString())
                sendNotification(it.data.toString(), message, tousernames)
            }

        }
    }

    private fun sendNotification(toUserToken: String, message: String, toUserName: String) {
        Log.d("TAG", "testNotificationImage: " + sessionManager.getProfileImage.toString())
        val data = Data(
            "ChatRoomActivity",
            toUserName,
            intent.getStringExtra("fromUserName"),

            intent.getStringExtra("fromUserName"),
            toUserName,
            intent.getStringExtra("fromUserName"),
            myProfileImage.toString(),
            R.mipmap.ic_launcher_round,
            message,
            "New Message",
            "SMS",
            "individual",
            "default"
        )


        val sender3 = Sender3(data, data, toUserToken, "high")


        var apiService =
            Client.getClient(resources.getString(R.string.fcmlink))!!.create(APIService::class.java)
        var apiService2 = Client.getClient(resources.getString(R.string.fcmlink))!!
            .create(APIService2::class.java)
        apiService.sendNotification2(sender3)!!.enqueue(object : Callback<MyResponse> {
            override fun onResponse(call: Call<MyResponse>, response: Response<MyResponse>) {
                if (response.code() == 200) {
                    if (response.body()!!.success !== 1) {
                        Log.e("TAG", "unsuccess: " + response.message())
                    } else {
                        Log.e("TAG", "success: $response")
                    }
                } else {
                    Log.d("TAG", "onResponse: " + response.code())
                }
            }

            override fun onFailure(call: Call<MyResponse>, t: Throwable) {
                Log.e("TAG", "failur: " + t.message)
            }
        })

    }

    fun sendMessageWithImageAndText(message: String, imageUri: Uri? , toUserName: String) {
        //  Toast.makeText(this, "Image uri  found."+uri, Toast.LENGTH_SHORT).show()
        var imageStream = contentResolver.openInputStream(imageUri!!)

        var myUserName = sessionManager.username
        var sendUserName = toUserName.trim()


//        val sendMessage: SendMessagePostingModel =
//            SendMessagePostingModel(myUserName, sendUserName, message, 0, 0)


        try {

            val file = File(Objects.requireNonNull(FileUtils.getPath(this, imageUri)))
            val image = FileUtils.getPath(this, imageUri)
            customDialog?.show()
            messageViewModel.getMessageWithFile(
                myUserName!!, sendUserName, message, 0, 0, Uri.parse(image), file
            ) { progress ->
                // Update your progress bar in the UI
                //  binding.progressBar.progress = progress
                runOnUiThread {
                    Log.d("TAGerror", "sendMessageWithImageAndText Progress: $progress")
                    //binding.customProgressBar.visibility = View.VISIBLE

                    //  val animationProgress = progress.toFloat() / 100
                    //  binding.customProgressBar.progress=progress
//                    if (progress == 100) {
//                        binding.customProgressBar.visibility = View.GONE
//                    }
                }
            }.observe(this) {
                if (it.status_code == 200) {
                    customDialog?.dismiss()

                    getUserToken(toUserName.trim(), message , toUserName.trim())





                } else {
                    Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Something went wrong." + e.message, Toast.LENGTH_SHORT).show()
        }


    }

    private fun forwardMessageToMultipleUser() {
        val params = ForwardMessagePostingModel(
            fromUsername = sessionManager.username ?: "",
            toUsernames = ForwadCompaionList.userForwardList.map { it.toUsername ?: "" },
            ids = ForwadCompaionList.messageList
        )
        Log.d("params", params.toString())
        try {
            customDialog?.show()
            messageViewModel.messageForwardToMultipleUser(params)
                .observe(this) {
                    try {
                        if (it.status_code == 200) {
                            customDialog?.dismiss()

                            if (ForwadCompaionList.userForwardList.size > 1) {
                                shouldReloadChatRoom = true
                                this@MessageForward.finish()
                            } else {
                                val fromUserName = sessionManager.username

                                val bundle = Bundle().apply {
                                    putString(
                                        "toUserName",
                                        ForwadCompaionList.userForwardList.first().toUsername
                                    )
                                    putString(
                                        "toUserToken",
                                        ForwadCompaionList.userForwardList.first().notificationToken
                                    )
                                    putString(
                                        "toUserImage",
                                        ForwadCompaionList.userForwardList.first().userimage
                                    )
                                    putString("fromUserName", fromUserName)
                                }

                                // Start ChatRoomActivity with the bundle
                                val intent = Intent(this@MessageForward, ChatRoomActivity::class.java)
                                intent.putExtras(bundle)
                                startActivity(intent)
                                ForwadCompaionList.userForwardList.clear()

                                // Finish current activity
                                finish()
                            }
                            //getUserToken(toUserName.trim(), message)
                        } else {
                            //customDialog?.dismiss()
                            Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show()
                        }
                    }catch (error: Exception){
                        finish()
                    }
                }
        } catch (e: Exception) {
            //customDialog?.dismiss()
            Log.e("forwardMessageException", e.message.toString())
        }
    }
}
