package com.friendfinapp.dating.ui.othersprofile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.friendfinapp.dating.R
import com.friendfinapp.dating.databinding.ActivityOthersUsersProfileBinding
import com.friendfinapp.dating.helper.Constants
import com.friendfinapp.dating.helper.InternetHelper
import com.friendfinapp.dating.helper.NetworkCheckWorker
import com.friendfinapp.dating.helper.ProgressCustomDialog
import com.friendfinapp.dating.helper.SessionManager
import com.friendfinapp.dating.notification.*
import com.friendfinapp.dating.ui.individualsearch.IndividualSearchResult
import com.friendfinapp.dating.ui.landingpage.fragments.serachfragment.SearchFragment
import com.friendfinapp.dating.ui.othersprofile.model.BlockedUnblockedPostingModel
import com.friendfinapp.dating.ui.othersprofile.model.MessageViewModel
import com.friendfinapp.dating.ui.othersprofile.model.SendMessagePostingModel
import com.friendfinapp.dating.ui.profileImageViewer.ProfileImageViewer
import com.friendfinapp.dating.ui.reportanabuse.ReportAnAbuse
import com.friendfinapp.dating.ui.signin.viewmodel.LogInViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.concurrent.TimeUnit


class OthersUsersProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOthersUsersProfileBinding
    var customDialog: ProgressCustomDialog? = null

    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: LogInViewModel
    private lateinit var messageViewModel: MessageViewModel
    var username = ""
    var userimage = ""
    var from = ""

    var blocked = false

    private lateinit var apiService: APIService
    private lateinit var apiService2: APIService2

    private lateinit var internetHelper: InternetHelper

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_others_users_profile)
        val bundle: Bundle? = intent.extras
        if (bundle != null) {

//            val userList: ArrayList<ImageSendHelper>? = bundle.getParcelableArrayList("myUser")
//             username = userList?.get(0)?.username.toString()
//             userimage = userList?.get(0)?.userimage.toString()
            username = bundle.getString("username").toString() // 1
            from = bundle.getString("from").toString() // 1


            if (from == "2") {
                userimage = IndividualSearchResult.instance?.value.toString()
            } else {
                userimage = SearchFragment.instance?.value.toString()
            }


            //  userimage = bundle.getString("userimage").toString() // 1

        }

        internetHelper = InternetHelper(this)
        apiService =
            Client.getClient(resources.getString(R.string.fcmlink))!!.create(APIService::class.java)
        apiService2 = Client.getClient(resources.getString(R.string.fcmlink))!!
            .create(APIService2::class.java)

        setUpView()
        loadBlockUnBlock()
        setClickListeners()



        loadUserProfile(username, Constants.USER_INFO.username)
    }

    private fun loadBlockUnBlock() {
        customDialog?.show()
        viewModel.fetchProfile(Constants.USER_INFO.username).observe(this) {

            Log.d("TAG", "loadUserProfile: " + it.data?.size)
            // customDialog!!.dismiss()
            //  Constants.USER_OTHER_PROFILE_INFO.addAll(it.data!!)
            Constants.USER_BLOCKED.clear()
            Constants.USER_BLOCKED.addAll(it.blockedUser!!)

            for (i in it.blockedUser) {
                if (i.blockedUser.equals(username)) {
                    binding.textBlock.text = "Unblock this User"
                    blocked = true
                }
            }


        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    private fun loadUserProfile(otherUserName: String, ownUserName: String?) {


        Log.d("TAG", "loadUserProfile: " + username)
        customDialog?.show()

        if (internetHelper.isOnline()){
            messageViewModel.otherUserProfile(ownUserName, otherUserName).observe(this) {


                Log.d("TAG", "loadUserProfile: " + it.data?.size)
                Log.d("TAG", "loadUserProfile: " + it.status_code.toString())
                customDialog!!.dismiss()


                if (it.viewProfile!!) {
                    binding.mainView.visibility = View.VISIBLE
                    binding.NoProfileView.visibility = View.GONE
                } else {
                    binding.mainView.visibility = View.INVISIBLE
                    binding.NoProfileView.visibility = View.VISIBLE
                }
                if (it.isBlocked!!) {
                    binding.messageView.visibility = View.GONE
                } else {
                    binding.messageView.visibility = View.VISIBLE
                }
                Constants.USER_OTHER_PROFILE_INFO.addAll(it.data!!)
//            if (!Constants.USER_OTHER_PROFILE_INFO.isNullOrEmpty()){

                for (i in it.data!!.indices) {
                    Log.d("TAG", "a: " + it.data!![i].questionID)
                    Log.d("TAG", "a: " + it.data!![i].value.toString())
                    if (it.data!![i].questionID == 1) {
                        binding.height.text = it.data!![i].value.toString()
                    } else if (it.data!![i].questionID == 2) {
                        binding.weight.text = it.data!![i].value.toString() + " KG"
                    } else if (it.data!![i].questionID == 3) {
                        binding.body.text = it.data!![i].value.toString()
                    } else if (it.data!![i].questionID == 4) {
                        binding.looking.text = it.data!![i].value.toString()
                    } else if (it.data!![i].questionID == 5) {
                        binding.eyes.text = it.data!![i].value.toString()
                    } else if (it.data!![i].questionID == 6) {
                        binding.hair.text = it.data!![i].value.toString()
                    } else if (it.data!![i].questionID == 7) {
                        binding.smoking.text = it.data!![i].value.toString()

                    } else if (it.data!![i].questionID == 8) {
                        binding.drinking.text = it.data!![i].value.toString()
                    } else if (it.data!![i].questionID == 9) {
                        binding.title.text = it.data!![i].value.toString()
                    } else if (it.data!![i].questionID == 10) {
                        binding.about.text = it.data!![i].value.toString()
                    } else if (it.data!![i].questionID == 11) {
                        binding.whatareyou.text = it.data!![i].value.toString()
                    } else if (it.data!![i].questionID == 12) {
                        binding.genderLooking.text = it.data!![i].value.toString()
                    }
                }


//            if (it.data!!.size>=1) {
//                if (it.data!![0].questionID == 1) {
//                    binding.height.text = it.data!![0].value.toString()
//                } else if (it.data!![0].questionID == 2){
//                    binding.weight.text= it.data!![0].value.toString()+" KG"
//                }else if (it.data!![0].questionID == 3){
//                    binding.body.text= it.data!![0].value.toString()
//                }else if (it.data!![0].questionID == 4){
//                    binding.looking.text= it.data!![0].value.toString()
//                }else if (it.data!![0].questionID == 5){
//                    binding.eyes.text= it.data!![0].value.toString()
//                }else if (it.data!![0].questionID == 6){
//                    binding.hair.text= it.data!![0].value.toString()
//                }else if (it.data!![0].questionID == 7){
//                    binding.smoking.text= it.data!![0].value.toString()
//
//                }else if (it.data!![0].questionID == 8){
//                    binding.drinking.text= it.data!![0].value.toString()
//                }else if (it.data!![0].questionID == 9){
//                    binding.title.text = it.data!![0].value.toString()
//                }else if (it.data!![0].questionID == 10){
//                    binding.about.text = it.data!![0].value.toString()
//                }else if (it.data!![0].questionID == 11){
//                    binding.whatareyou.text = it.data!![0].value.toString()
//                }else if (it.data!![0].questionID == 12){
//                    binding.genderLooking.text= it.data!![0].value.toString()
//                }
//            }
//
//            if (it.data!!.size>=2)
//                binding.weight.text= it.data!![1].value.toString()+" KG"
//
//            if (it.data!!.size>=3)
//                binding.body.text= it.data!![2].value.toString()
//
//            if (it.data!!.size>=4)
//                binding.looking.text= it.data!![3].value.toString()
//
//            if (it.data!!.size>=5)
//                binding.eyes.text= it.data!![4].value.toString()
//
//            if (it.data!!.size>=6)
//                binding.hair.text= it.data!![5].value.toString()
//
//            if (it.data!!.size>=7)
//                binding.smoking.text= it.data!![6].value.toString()
//
//            if (it.data!!.size>=8)
//                binding.drinking.text= it.data!![7].value.toString()
////                binding.title.text= Constants.USER_PROFILE_INFO[8].value.toString()
////                binding.about.text= Constants.USER_PROFILE_INFO[9].value.toString()
//            if (it.data!!.size >= 9)
//                binding.title.text = it.data!![8].value.toString()
//            if (it.data!!.size >= 10)
//                binding.about.text = it.data!![9].value.toString()
//            if (it.data!!.size >= 11)
//                binding.whatareyou.text = it.data!![10].value.toString()
//            if (it.data!!.size>=12)
//                binding.genderLooking.text= it.data!![11].value.toString()

                // }

            }
        }else{
            customDialog?.dismiss()
            finish()
            Toast.makeText(this,"No Internet Connection. Please try again!",Toast.LENGTH_SHORT).show()
        }

        messageViewModel.failureResult.observe(this) {
            if (it) {
                customDialog!!.dismiss()
                finish()
                Toast.makeText(this,"There is Connection Error. Please try again.If problem persist then close the app and again run!",Toast.LENGTH_SHORT).show()
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun setClickListeners() {

        binding.imageCard.setOnClickListener {


            startActivity(
                Intent(this, ProfileImageViewer::class.java).putExtra("userimage",userimage))


        }

        binding.imageBack.setOnClickListener {
            finish()
        }

        binding.send.setOnClickListener {
            if (!validMessageField()) {
                return@setOnClickListener
            } else {
                sendMessage(binding.edit.text.toString())
            }
        }


        binding.report.setOnClickListener {
            startActivity(
                Intent(this, ReportAnAbuse::class.java).putExtra(
                    "reportedUserName",
                    username
                )
            )
        }
        binding.blockUnblock.setOnClickListener {
            customDialog?.show()
            if (blocked) {

                Log.d("TAG", "my name : " + Constants.USER_INFO.username)
                Log.d("TAG", "my name : " + username)

                var model: BlockedUnblockedPostingModel =
                    BlockedUnblockedPostingModel(Constants.USER_INFO.username, username)

                messageViewModel.blockedUnblockedUser(model).observe(this) {

                    if (it.statusCode == 200) {
                        blocked = false
                        binding.textBlock.text = "Block this User"

                        viewModel.fetchProfile(Constants.USER_INFO.username).observe(this) {

                            Log.d("TAG", "loadUserProfile: " + it.data?.size)
                            customDialog!!.dismiss()
                            //  Constants.USER_OTHER_PROFILE_INFO.addAll(it.data!!)
                            Constants.USER_BLOCKED.clear()
                            Constants.USER_BLOCKED.addAll(it.blockedUser!!)


                        }
                    }
                }

            } else {

                Log.d("TAG", "my name : " + Constants.USER_INFO.username)
                Log.d("TAG", "my name : " + username)
                var model: BlockedUnblockedPostingModel =
                    BlockedUnblockedPostingModel(Constants.USER_INFO.username, username)

                messageViewModel.blockedUnblockedUser(model).observe(this) {

                    if (it.statusCode == 200) {
                        blocked = true
                        binding.textBlock.text = "Unblock this User"

                        viewModel.fetchProfile(Constants.USER_INFO.username).observe(this, {

                            Log.d("TAG", "loadUserProfile: " + it.data?.size)
                            customDialog!!.dismiss()
                            // Constants.USER_OTHER_PROFILE_INFO.addAll(it.data!!)
                            Constants.USER_BLOCKED.clear()
                            Constants.USER_BLOCKED.addAll(it.blockedUser!!)


                        })
                    }
                }
            }
        }
    }

    private fun sendMessage(message: String) {

        var myUserName = sessionManager.username
        var sendUserName = username

        val sendMessage: SendMessagePostingModel =
            SendMessagePostingModel(myUserName, sendUserName, message, 0, 1)


        customDialog?.show()
        messageViewModel.getMessage(sendMessage).observe(this) {
            if (it.status_code == 200) {
                customDialog?.dismiss()
                binding.edit.setText("")
                getUserToken(sendUserName.trim(), message)
                // Toast.makeText(this,"message send "+it.message,Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getUserToken(username: String?, message: String) {

        Log.d("TAG", "getUserToken: " + username)
        messageViewModel.getUserNotificationToken(username).observe(this) {

//            Log.d("TAG", "getUserToken: " + it.data.toString())
//            if (it.data.isNullOrEmpty()) {
//                sendNotification(toUserToken, message)
//            } else {

            Log.d("TAG", "getUserToken: " + it.data.toString())
            sendNotification(it.data.toString(), message)
            //    }

        }
    }

    private fun sendNotification(toUserToken: String, message: String) {
        Log.d("TAG", "testNotificationImage: " + sessionManager.getProfileImage.toString())
        val data = Data(
            "ChatRoomActivity",
            username,
            sessionManager.username,
            sessionManager.username,
            username,
            sessionManager.username,
            Constants.myProfileImage.toString(),
            R.mipmap.ic_launcher_round,
            message,
            "New Message",
            "SMS",
            "individual",
            "default"
        )

        val data2 = Data(
            "ChatRoomActivity",
            username,
            sessionManager.username,
            sessionManager.username,
            username,
            sessionManager.username,
            Constants.myProfileImage.toString(),
            R.mipmap.ic_launcher_round,
            message,
            "New Message",
            "SMS",
            "individual",
            "default"
        )


        val sender3 = Sender3(data2, data, toUserToken, "high")

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

    private fun validMessageField(): Boolean {
        val firstnameInput: String = binding.edit.text.toString().trim()
        return when {
            firstnameInput.isEmpty() -> {
                Toast.makeText(this, "Message Field cant be empty", Toast.LENGTH_SHORT).show()
                false
            }

            else -> {
                true
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
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






        customDialog = ProgressCustomDialog(this)
        sessionManager = SessionManager(this)
        viewModel = ViewModelProvider(this).get(LogInViewModel::class.java)
        messageViewModel = ViewModelProvider(this).get(MessageViewModel::class.java)



        binding.userName.text = username
       // var imageBytes = userimage.toByteArray()
        //        val imageByteArray: ByteArray = Base64.getDecoder().decode(imageBytes)
//        val decodedString = String(imageByteArray)

        //val bmp = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes!!.size)
        //   holder.binding.userProfile.setImageBitmap(bmp)

        //  Log.d("TAG", "onBindViewHolder: "+decodedString)

        Glide.with(this)
            .load(Constants.BaseUrl+userimage)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.logo)
            .into(binding.userProfile)







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



    // Check network status
    private fun isConnected(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

    private fun showNoInternetMessage() {

        if (customDialog!=null){
            customDialog!!.dismiss()
            finish()
        }
        // Implement your "No Internet" UI logic here, e.g., display a Snackbar or Toast
        Snackbar.make(findViewById(android.R.id.content), "No Internet Connection", Snackbar.LENGTH_LONG).show()
    }

    // Create a one-time work request (optional)
    private fun startOneTimeNetworkCheckWorker() {
        val workRequest = OneTimeWorkRequest.Builder(NetworkCheckWorker::class.java).build()
        WorkManager.getInstance(this).enqueue(workRequest)
    }

}