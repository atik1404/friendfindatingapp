package com.friendfinapp.dating.ui.chatroom

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaMetadataRetriever
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewAnimationUtils
import android.view.Window
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.friendfinapp.dating.ForwardMessaging.ForwadCompaionList
import com.friendfinapp.dating.ForwardMessaging.ForwardMessageCompainion
import com.friendfinapp.dating.ForwardMessaging.MessageForward
import com.friendfinapp.dating.R
import com.friendfinapp.dating.application.AppController
import com.friendfinapp.dating.application.BaseActivity
import com.friendfinapp.dating.cropper.CropImage
import com.friendfinapp.dating.cropper.CropImageView
import com.friendfinapp.dating.databinding.ActivityChatRoomBinding
import com.friendfinapp.dating.databinding.DialogSearchMessageBinding
import com.friendfinapp.dating.helper.Constants
import com.friendfinapp.dating.helper.Constants.finalRecordTime
import com.friendfinapp.dating.helper.Constants.hidden
import com.friendfinapp.dating.helper.Constants.myProfileImage
import com.friendfinapp.dating.helper.Constants.videoCheck
import com.friendfinapp.dating.helper.FileUtils
import com.friendfinapp.dating.helper.PermissionHelper
import com.friendfinapp.dating.helper.ProgressCustomDialog
import com.friendfinapp.dating.helper.SessionManager
import com.friendfinapp.dating.helper.dateparser.DateTimeFormat
import com.friendfinapp.dating.helper.showViewAlertDialog
import com.friendfinapp.dating.notification.*
import com.friendfinapp.dating.ui.chatroom.adapter.LiveChatAdapter
import com.friendfinapp.dating.ui.chatroom.responsemodel.LiveChatPostingModel
import com.friendfinapp.dating.ui.chatroom.responsemodel.LiveChatResponseModel
import com.friendfinapp.dating.ui.chatroom.responsemodel.LiveChatSearchModel
import com.friendfinapp.dating.ui.chatroom.viewmodel.LiveChatViewModel
import com.friendfinapp.dating.ui.landingpage.fragments.chatfragment.ChatFragment
import com.friendfinapp.dating.ui.othersprofile.model.MessageViewModel
import com.friendfinapp.dating.ui.reportanabuse.ReportAnAbuse
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.iamkamrul.dateced.DateCed
import com.iamkamrul.dateced.TimeDifferenceUnit
import com.iamkamrul.dateced.TimeZoneId
import com.jerp.common.dateparser.DateTimeParser
import com.jerp.common.dateparser.parseUtcToLocalCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.hypot
import kotlin.math.roundToInt


class ChatRoomActivity : BaseActivity<ActivityChatRoomBinding>(), GestureDetector.OnGestureListener {

    //animation like whats up Audio click variable


    enum class UserBehaviour {
        CANCELING, LOCKING, NONE
    }

    enum class RecordingBehaviour {
        CANCELED, LOCKED, LOCK_DONE, RELEASED
    }

    interface RecordingListener {
        fun onRecordingStarted()
        fun onRecordingLocked()
        fun onRecordingCompleted()
        fun onRecordingCanceled()
    }

    private val TAG = "AudioRecordView"


    private var animBlink: Animation? =
        null
    private var animJump: Animation? = null
    private var isMessageSearch: Boolean = false
    private var animJumpFast: Animation? = null

    private var isDeleting = false
    private var stopTrackingAction = false
    private var handler: Handler? = null

    private var audioTotalTime: Long = 0
    private var timerTask: TimerTask? = null
    private var audioTimer: Timer? = null
    private var timeFormatter: SimpleDateFormat? = null

    private var lastX = 0f
    private var lastY: kotlin.Float = 0f
    private var firstX = 0f
    private var firstY: kotlin.Float = 0f

    private val directionOffset = 0f
    private var cancelOffset: kotlin.Float = 0f
    private var lockOffset: kotlin.Float = 0f
    private var dp = 0f
    private var isLocked = false

    private var userBehaviour = UserBehaviour.NONE


    private val recordingListener: RecordingListener? = null

    var isLayoutDirectionRightToLeft = false

    var screenWidth = 0
    var screenHeight: Int = 0

    private var showCameraIcon = true
    private var showAttachmentIcon = true
    private var showEmojiIcon = true


    //end animation variable

    var fromUserName = ""
    var toUserImage = ""
    var toUserName = ""
    var toUserToken = ""

    private var uri: Uri? = null
    private var videoUri: Uri? = null


    private lateinit var viewModel: LiveChatViewModel
    private lateinit var messageViewModel: MessageViewModel

    var customDialog: ProgressCustomDialog? = null
    private var permissionHelper: PermissionHelper? = null

    private lateinit var adapter: LiveChatAdapter
    private lateinit var sessionManager: SessionManager


    private lateinit var apiService: APIService
    private lateinit var apiService2: APIService2

    var check = false


    //coroutine

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    //audio recording
    private var isTextSending = false
    private lateinit var mediaRecorder: MediaRecorder
    private var isRecording = false
    private var outputFile: File? = null
    private var recordingStartTime = 0L
    private var recordingTimerHandler: Handler? = null

    private var animatorSet: AnimatorSet? = null


    private lateinit var gestureDetector: GestureDetector
    override fun viewBindingLayout(): ActivityChatRoomBinding = ActivityChatRoomBinding.inflate(layoutInflater)

    @RequiresApi(Build.VERSION_CODES.R)
    override fun initializeView(savedInstanceState: Bundle?) {
        binding.forward.setOnClickListener {
            val bundle: Bundle? = intent.extras

            startActivity(

                Intent(applicationContext, MessageForward::class.java)
                    .putExtra("fromUserName", bundle!!.getString("fromUserName").toString())
                    .putExtra(
                        "toUserToken", bundle.getString("toUserToken").toString()

                    )

            )
        }

        instance = this
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            fromUserName = bundle.getString("fromUserName").toString() // 1
            toUserImage = bundle.getString("toUserImage").toString() // 1
            toUserName = bundle.getString("toUserName").toString() // 1
            toUserToken = bundle.getString("toUserToken").toString() // 1

            Log.d("TAG", "onCreate: $fromUserName")
            Log.d("TAG", "onCreate: $toUserName")
        }


        apiService =
            Client.getClient(resources.getString(R.string.fcmlink))!!.create(APIService::class.java)
        apiService2 = Client.getClient(resources.getString(R.string.fcmlink))!!
            .create(APIService2::class.java)

        //setup some initial view

        setUpView()

        //end


        // recording animation work done with that setup

        setupRecording()

        // end


        //setup recycler view

        setUpRecyclerView()

        //end


        //get chat list data for the first time


        getChatList()

        //end


        //set up all click listener in this screen

        setUpClickListener()

        //end


        // set up coroutine system start polling

        startPolling()

        //end


        //initial permission asking for notification

        if (!permissionHelper!!.checkPermissionForNotification()) {
            permissionHelper!!.requestPermissionNotification()
        }
        if (!permissionHelper!!.checkPermissionForRecordAudio()) {
            permissionHelper!!.requestPermissionForAudio()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUpView() {

        //set up all view here

        gestureDetector = GestureDetector(this, this)

        viewModel = ViewModelProvider(this).get(LiveChatViewModel::class.java)
        messageViewModel = ViewModelProvider(this).get(MessageViewModel::class.java)
        customDialog = ProgressCustomDialog(this)
        permissionHelper = PermissionHelper(this)
        sessionManager = SessionManager(this)

        binding.userName.text = toUserName


        //set user image at the top

        Glide.with(this)
            // .load(Base64.getDecoder().decode(imageBytes))
            .load(Constants.BaseUrl + toUserImage).diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.logo).into(binding.userProfileImage)

        //end


        // check and change audio mic and send icon for text

        binding.edit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if (text.isNullOrEmpty()) {
                    isTextSending = false
                    binding.image.visibility = View.VISIBLE
                    binding.send.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@ChatRoomActivity, R.drawable.baseline_mic_none_24
                        )
                    )
                } else {
                    isTextSending = true
                    binding.image.visibility = View.GONE
                    binding.send.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@ChatRoomActivity, R.drawable.baseline_send_24
                        )
                    )
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        //end


        // audio recording animation start touch listener

//        if (uri == null && binding.edit.text.toString().isEmpty()) {
//            binding.send.setOnTouchListener { view, motionEvent ->
//                handleMicrophoneButtonTouch(view, motionEvent)
//            }
//        }

        //end


        //setUpExoPlayer

//        //bandwisthmeter is used for getting default bandwidth
//        val bandwidthMeter: BandwidthMeter = DefaultBandwidthMeter()
//        // track selector is used to navigate between video using a default seekbar.
//        val trackSelector: TrackSelector =
//            DefaultTrackSelector(AdaptiveTrackSelection.Factory(bandwidthMeter))
//        //we are ading our track selector to exoplayer.
//        exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector)


        binding.livechatRcv.addOnItemTouchListener(
            RecyclerViewTouchListener(
                this,
                binding.revealView
            )
        )


        //whats up animation

        timeFormatter = SimpleDateFormat("m:ss", Locale.getDefault())

        val displayMetrics: DisplayMetrics = this.getResources().getDisplayMetrics()
        screenHeight = displayMetrics.heightPixels
        screenWidth = displayMetrics.widthPixels

        isLayoutDirectionRightToLeft =
            this.getResources().getBoolean(R.bool.is_right_to_left)



        handler = Handler(Looper.getMainLooper())

        dp = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            1f,
            this.getResources().getDisplayMetrics()
        )

        animBlink = AnimationUtils.loadAnimation(
            this,
            com.friendfinapp.dating.R.anim.blink
        )
        animJump = AnimationUtils.loadAnimation(
            this,
            com.friendfinapp.dating.R.anim.jump
        )
        animJumpFast = AnimationUtils.loadAnimation(
            this,
            com.friendfinapp.dating.R.anim.jump_fast
        )


        //end whats up animation

    }


    private fun clearSearchResult(){
        isMessageSearch = false
        binding.searchResultTv.visibility = View.GONE
        getChatList()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun setUpClickListener() {
        binding.searchResultTv.setOnClickListener {
            clearSearchResult()
        }

        binding.imageBack.setOnClickListener {
            adapter.stopPlayback()
            adapter.exitSelectionMode()
            ChatFragment.instance?.getChatDataWhenBackFromChatRoom()
            finish()
        }

        binding.send.setOnClickListener {
            clearSearchResult()

            if (permissionHelper!!.checkPermissionForNotification()) {

                if (uri != null && binding.edit.text.toString().isNotEmpty()) {

                    sendMessageWithImageAndText(binding.edit.text.toString(), uri)

                } else if (uri != null && binding.edit.text.toString().isEmpty()) {

                    Log.d("TAG", "send message with empty text: ")
                    sendMessageWithImageAndText(" ", uri)

                } else if (videoUri != null && binding.edit.text.toString().isNotEmpty()) {

                    Log.d("TAG", "send message with empty text: ")
                    videoCheck = false
                    sendMessageWithVideoAndText(binding.edit.text.toString(), videoUri)

                } else if (videoUri != null && binding.edit.text.toString().isEmpty()) {

                    videoCheck = false
                    Log.d("TAG", "send message with empty text: ")
                    sendMessageWithVideoAndText(" ", videoUri)

                } else if (uri == null && videoUri == null && binding.edit.text.toString()
                        .isNotEmpty()
                ) {

                    sendMessageWithTextMessage(binding.edit.text.toString())

                } else {
                    if (outputFile == null) {

                        Toast.makeText(this, "input field first", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {

                permissionHelper!!.requestPermissionNotification()

            }
        }

        binding.image.setOnClickListener {
            if (permissionHelper!!.checkPermissionForCamera()) {
                pickImageCamera()
            } else {
                permissionHelper!!.requestPermissionForCamera()
            }
        }


        hideRevealView()
        binding.attachFile.setOnClickListener {
            showRevealView()
        }

        binding.galleryItemFile.setOnClickListener {
            hideRevealView()
            if (permissionHelper!!.checkPermissionForUploadScreenshot()) {

                pickImageGallery()

            } else {
                permissionHelper!!.requestPermissionUploadScreenshot()
            }
        }

        binding.cameraItemFile.setOnClickListener {
            hideRevealView()
            if (permissionHelper!!.checkPermissionForCamera()) {
                pickImageCamera()
            } else {
                permissionHelper!!.requestPermissionForCamera()
            }
        }


        //videos
        binding.videoItemFile.setOnClickListener {
            hideRevealView()
            if (permissionHelper!!.checkPermissionForUploadVideo()) {

                pickVideosGallery()

            } else {
                permissionHelper!!.requestPermissionUploadVideo()
            }
        }

        binding.menuIv.setOnClickListener {
            showPopupMenuItems()
        }

        binding.imageHoldDelete.setOnClickListener {
            binding.imageHoldSection.visibility = View.GONE
            uri = null
            isTextSending = false
            binding.send.setImageDrawable(
                ContextCompat.getDrawable(
                    this@ChatRoomActivity, R.drawable.baseline_mic_none_24
                )
            )
        }
        binding.videoHoldDelete.setOnClickListener {
            binding.videoHoldSection.visibility = View.GONE
            videoUri = null
            isTextSending = false
            binding.send.setImageDrawable(
                ContextCompat.getDrawable(
                    this@ChatRoomActivity, R.drawable.baseline_mic_none_24
                )
            )
        }

        binding.menuDot.setOnClickListener {
            showPopupMenu()
        }
    }


    val stringsFromLoop = mutableListOf<String>()
    var selectedMessageList: MutableList<LiveChatResponseModel.Data> = ArrayList()


    //chat room chat list recycler view set up

    private fun setUpRecyclerView() {

        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = false
        binding.livechatRcv.layoutManager = layoutManager


        adapter = LiveChatAdapter(this@ChatRoomActivity, customDialog, fromUserName)
        //adapter.setHasStableIds(true)
        binding.livechatRcv.adapter = adapter

        adapter.onItemLongClick = { item->
            selectedMessageList.clear()
            selectedMessageList.addAll(item)
            stringsFromLoop.clear()


            ForwardMessageCompainion.selectedMessageList.clear()
            ForwardMessageCompainion.selectedMessageList.addAll(item)

            ForwadCompaionList.messageList.clear()
            ForwadCompaionList.messageList.addAll(item.map { msg-> msg.id.toString() })

            for (id in item) {
                stringsFromLoop.add(id.id!!.toString())
                Log.d("bu", "setUpRecyclerView: " + stringsFromLoop.toString())
            }

            if (item.isEmpty()) {
                binding.userProfileImage.visibility = View.VISIBLE
                binding.userName.visibility = View.VISIBLE
                binding.menuIv.visibility = View.VISIBLE
                binding.forward.visibility = View.GONE
                binding.menuDot.visibility = View.GONE
            } else {
                binding.userProfileImage.visibility = View.GONE
                binding.userName.visibility = View.GONE
                binding.menuIv.visibility = View.GONE
                binding.forward.visibility = View.VISIBLE
                binding.menuDot.visibility = View.VISIBLE
            }
        }
    }

    //end


    //popupmenu
    private fun showPopupMenu() {
        // Create PopupMenu instance
        val popupMenu = PopupMenu(this, binding.menuDot)

        // Inflate your menu resource here
        popupMenu.inflate(R.menu.pop_up_menu)

        // Set menu item click listener
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_delete -> {
                    // Handle menu item 1 click
                    openDeleteDialog()
                    true
                }

                R.id.nav_report -> {
                    // Handle menu item 2 click
                    true
                }
                // Add more menu item cases as needed
                else -> false
            }
        }

        // Show the PopupMenu
        popupMenu.show()
    }

    private fun showPopupMenuItems() {
        // Create PopupMenu instance
        val popupMenu = PopupMenu(this, binding.menuIv)

        // Inflate your menu resource here
        popupMenu.inflate(R.menu.pop_up_chat_room_menu)

        // Set menu item click listener
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_report_user -> {
                    showReportDialog()
                    true
                }

                R.id.menu_search -> {
                    showSearchMessageDialog()
                    true
                }
                // Add more menu item cases as needed
                else -> false
            }
        }

        // Show the PopupMenu
        popupMenu.show()
    }

    private fun openDeleteDialog() {
        val btnSheet: View = layoutInflater.inflate(R.layout.dialog_delete_chat, null)
        val dialog: Dialog = BottomSheetDialog(this, R.style.AppBottomSheetDialogThemeNew)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(btnSheet)

        val close = dialog.findViewById<View>(R.id.close) as ImageView
        val deleteMessages = dialog.findViewById<View>(R.id.deleteMessages) as TextView


        close.setOnClickListener { dialog.dismiss() }

        deleteMessages.setOnClickListener {


            Log.d("TAG", "openDeleteDialog: " + stringsFromLoop.toString())

            customDialog?.show()
            viewModel.deleteChatList(stringsFromLoop, customDialog, this).observe(this) {
                if (it.statusCode == 200) {
                    customDialog?.dismiss()
                    dialog.dismiss()
                    adapter.exitSelectionMode()
                    stringsFromLoop.clear()
                    adapter.removeData(selectedMessageList)
                    Toast.makeText(this, "Message(s) deleted.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        this,
                        "something went wrong!" + it.statusCode,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }


        dialog.show()
    }


    //

    //start coroutine

    private fun startPolling() {
        coroutineScope.launch {
            while (isActive) {
                // Make an API call to fetch new messages
                //fetchNewMessages()

                instance?.getChatList2()
                // Delay for the specified polling interval
                delay(TimeUnit.SECONDS.toMillis(5))
            }
        }
    }
    private fun stopPolling() {
        coroutineScope.cancel()
    }

    private fun sendMessageWithTextMessage(message: String) {

        var myUserName = sessionManager.username
        var sendUserName = toUserName.trim()
        try {
            customDialog?.show()
            messageViewModel.getMessageWithOnlyText(myUserName!!, sendUserName, message, 0, 0)
                .observe(this) {
                    if (it.status_code == 200) {
                        customDialog?.dismiss()

                        getUserToken(toUserName.trim(), message)
                        startPolling()
                        binding.edit.setText("")
                        binding.edit.requestFocus()
                    } else {
                        Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: Exception) {       }
    }

    private fun uploadAudioFileToServer(outputFile: File, recordingDuration: Long) {
        var myUserName = sessionManager.username
        var sendUserName = toUserName.trim()
        Log.d("bur", "uploadAudioFileToServer: " + recordingDuration)
        try {
            val path = outputFile.absolutePath
            customDialog?.show()
messageViewModel.getMessageWithAudio(
                myUserName!!,
                sendUserName,
                Uri.parse(path),
                outputFile,
                recordingDuration
            )
                .observe(this) {
                    if (it.status_code == 200) {
                        customDialog?.dismiss()
                        getUserToken(toUserName.trim(), "Audio")
                        startPolling()
                    } else {
                        Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show()
                    }
                }

        } catch (e: Exception) {
            Toast.makeText(this, "Something went wrong." + e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendMessageWithImageAndText(message: String, imageUri: Uri?) {
        var imageStream = contentResolver.openInputStream(imageUri!!)
        var myUserName = sessionManager.username
        var sendUserName = toUserName.trim()
        try {
            val file = File(Objects.requireNonNull(FileUtils.getPath(this, imageUri)))
            val image = FileUtils.getPath(this, imageUri)
            customDialog?.show()
            messageViewModel.getMessageWithFile(
                myUserName!!, sendUserName, message, 0, 0, Uri.parse(image), file
            ) { progress ->
                runOnUiThread {
                    Log.d("TAG", "sendMessageWithImageAndText Progress: $progress")
                }
            }.observe(this) {
                if (it.status_code == 200) {
                    customDialog?.dismiss()
                    getUserToken(toUserName.trim(), message)
                    startPolling()
                    binding.edit.setText("")
                    binding.imageHoldSection.visibility = View.GONE
                    uri = null
                } else {
                    Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Something went wrong." + e.message, Toast.LENGTH_SHORT).show()
        } }

    private fun sendMessageWithVideoAndText(message: String, videoUris: Uri?) {
        Log.d("TAG", "sendMessageWithVideoAndText: " + videoUris)
        var imageStream = contentResolver.openInputStream(videoUris!!)
        var myUserName = sessionManager.username
        var sendUserName = toUserName.trim()
        try {
            val file = File(Objects.requireNonNull(FileUtils.getPath(this, videoUris)))
            val video = FileUtils.getPath(this, videoUris)
            customDialog?.show()
            messageViewModel.getMessageWithVideoFile(
                myUserName!!, sendUserName, message, 0, 0, Uri.parse(video), file
            ) { progress ->
                customDialog?.dismiss()
                runOnUiThread {
                    Log.d("TAG", "sendMessageWithImageAndText Progress: $progress")
                    if (progress == 100) {
                        val applicationContext = (applicationContext as AppController)
                        Toast.makeText(
                            applicationContext,
                            "Video Upload is successful. you can check this out now.",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else if (!videoCheck) {
                        videoCheck = true
                        binding.edit.setText("")
                        binding.videoHoldSection.visibility = View.GONE
                        videoUri = null
                        val applicationContext = (applicationContext as AppController)
                        Toast.makeText(
                            applicationContext,
                            "Video Upload is being progress. we will let you know when uploading is completed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }.observe(this) {
                if (it.status_code == 200) {
                    customDialog?.dismiss()
                    Log.d("TAG", "sendMessageWithVideoAndText: " + it.status_code)
                    getUserToken(toUserName.trim(), message)
                    startPolling()
                } else {
                    customDialog?.dismiss()
                    Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Log.d("TAG", "sendMessageWithVideoAndText: " + e.message + e.printStackTrace())
            Toast.makeText(this, "Something went wrongs." + e.printStackTrace(), Toast.LENGTH_SHORT)
                .show()
        }
    }


    private fun getUserToken(username: String?, message: String) {
        Log.d("TAG", "getUserToken: " + username)
        messageViewModel.getUserNotificationToken(username).observe(this) {

            Log.d("TAG", "getUserToken: " + it.data.toString())
            if (it.data.isNullOrEmpty()) {
                sendNotification(toUserToken, message)
            } else {

                Log.d("TAG", "getUserToken: " + it.data.toString())
                sendNotification(it.data.toString(), message)
            }

        }
    }

    private fun sendNotification(toUserToken: String, message: String) {
        Log.d("TAG", "testNotificationImage: " + sessionManager.getProfileImage.toString())
        val data = Data(
            "ChatRoomActivity",
            toUserName,
            fromUserName,
            fromUserName,
            toUserName,
            fromUserName,
            myProfileImage.toString(),
            R.mipmap.ic_launcher_round,
            message,
            "New Message",
            "SMS",
            "individual",
            "default"
        )
        val sender3 = Sender3(data, data, toUserToken, "high")
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


    private fun showSearchMessageDialog() {
        val dialogBinding = DialogSearchMessageBinding.inflate(layoutInflater)

        val dialog = this.showViewAlertDialog(
            dialogBinding.root,
            null,
            R.style.AlertDialogTransparentBg,
            true
        )

        dialogBinding.apply {
            searchBtn.setOnClickListener {
                val searchKey = searchEt.text.toString()
                if(searchKey.isEmpty()){
                    return@setOnClickListener
                }
                getChatListSearchResult(searchKey)
                isMessageSearch = true
                dialog.dismiss()
            }
            closeBtn.setOnClickListener {  dialog.dismiss() }
        }
    }

    fun getChatListSearchResult(searchKey: String) {
        val from = fromUserName.trim()
        val to = toUserName.trim()
        val params = LiveChatSearchModel(from.trim(), to.trim(), searchKey)
        if (searchKey.isEmpty()) {
            return
        }

        customDialog?.show()

        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                // Do something
                viewModel.getChatListSearchResult(params).observe(this@ChatRoomActivity) {
                    customDialog?.dismiss()
                    if(it == null){
                        showToastMessage("Something went wrong, please try again.")
                        return@observe
                    }
                    it.isBlocked.let { block ->
                        if (block!!) {
                            binding.sendViewMessageLayout.visibility = View.GONE
                            binding.blockedView.visibility = View.VISIBLE
                        } else {
                            binding.sendViewMessageLayout.visibility = View.VISIBLE
                            binding.blockedView.visibility = View.GONE
                        }
                    }
                    if (it.data?.isEmpty() == true) {
                        showToastMessage("No result found")
                        return@observe
                    }
                    binding.searchResultTv.text = "Search result showing for $searchKey"
                    binding.searchResultTv.isVisible = isMessageSearch
                    it.data.let {
                        adapter.addData(emptyList())
                        adapter.addData(getModifiedChatList(it!!))
                        Timber.e("modifiedList3: ${getModifiedChatList(it!!).map { it.effectiveDate }}")
                    }
                }
            }
        }
    }

    fun getChatList() {
        var from = fromUserName.trim()
        var to = toUserName.trim()
        var chatPostingModel: LiveChatPostingModel =
            LiveChatPostingModel(from.toString().trim(), to.toString().trim())


        if (!check) {
            customDialog?.show()
            check = true
        }
        //

        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                // Do something
                viewModel.getChatList(chatPostingModel).observe(this@ChatRoomActivity) {

                    it.isBlocked.let { block ->
                        if (block!!) {
                            binding.sendViewMessageLayout.visibility = View.GONE
                            binding.blockedView.visibility = View.VISIBLE
                        } else {
                            binding.sendViewMessageLayout.visibility = View.VISIBLE
                            binding.blockedView.visibility = View.GONE
                        }
                    }
                    it.data.let {
                        customDialog?.dismiss()
                        adapter.addData(getModifiedChatList(it!!))
                        Timber.e("modifiedList: ${getModifiedChatList(it!!).map { it.effectiveDate }}")
                    } } } }
    }

    fun getChatList2() {

        Log.d("TAG", "getChatList2: call")

        var from = fromUserName.trim()
        var to = toUserName.trim()
        var chatPostingModel: LiveChatPostingModel =
            LiveChatPostingModel(from.toString().trim(), to.toString().trim())


        if (!check) {
            customDialog?.show()
            check = true
        }
        //

        coroutineScope.launch {
            withContext(Dispatchers.Main) {
                // Do something
                viewModel.getChatList(chatPostingModel).observe(this@ChatRoomActivity) { chatList ->

                    chatList.isBlocked.let { block ->
                        if (block!!) {
                            binding.sendViewMessageLayout.visibility = View.GONE
                            binding.blockedView.visibility = View.VISIBLE
                        } else {
                            binding.sendViewMessageLayout.visibility = View.VISIBLE
                            binding.blockedView.visibility = View.GONE
                        }
                    }
                    chatList.data.let {
                        if(!isMessageSearch){
                            customDialog?.dismiss()
                            adapter.addDataNewItem(getModifiedChatList(it!!))

                            if (it.isNotEmpty()) {
                                if (Constants.fromUserName == it[it.size - 1].fromUsername.toString() && Constants.textMessage == it[it.size - 1].body.toString()
                                    && Constants.imageUrl == it[it.size - 1].imageURL.toString() && Constants.audioUrl == it[it.size - 1].audioURL.toString()
                                    && Constants.videoUrl == it[it.size - 1].videoURL.toString()
                                ) {
                                    Constants.fromUserName = it[it.size - 1].fromUsername.toString()
                                    Constants.textMessage = it[it.size - 1].body.toString()
                                    Constants.imageUrl = it[it.size - 1].imageURL.toString()
                                    Constants.audioUrl = it[it.size - 1].audioURL.toString()
                                    Constants.videoUrl = it[it.size - 1].videoURL.toString()
                                } else {
                                    binding.livechatRcv.postDelayed({

                                        binding.livechatRcv.smoothScrollToPosition(
                                            // scrollPosition
                                            adapter.chatList.size
                                        )
                                    }, 100)

                                    Constants.fromUserName = it[it.size - 1].fromUsername.toString()
                                    Constants.textMessage = it[it.size - 1].body.toString()
                                    Constants.imageUrl = it[it.size - 1].imageURL.toString()
                                    Constants.audioUrl = it[it.size - 1].audioURL.toString()
                                    Constants.videoUrl = it[it.size - 1].videoURL.toString()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getModifiedChatList(
        chats: List<LiveChatResponseModel.Data>
    ): List<LiveChatResponseModel.Data> {
        // Work on a mutable copy if Data has a mutable `var effectiveDate: String`
        val items = chats.toMutableList()
        var lastDate: String? = null

        try {
            for (i in items.indices) {
                val sendDate = items[i].sendTime
                val curDate  = sendDate?.parseUtcToLocalCompat(DateTimeFormat.outputDMy)
                if (curDate != null && curDate != lastDate) {
                    // first item of a new block → keep the date
                    val formattedDate = sendDate.parseUtcToLocalCompat(DateTimeFormat.sqlYMDHMS)
                    val dayDiff = DateCed.Factory.now().timeDifference(formattedDate, unit = TimeDifferenceUnit.DAY)
                    items[i].effectiveDate = DateTimeParser.formatRelativeDateLabel(dayDiff.toInt(), formattedDate)
                    lastDate = curDate
                } else {
                    // same as previous (or null) → blank
                    items[i].effectiveDate = ""
                }
            }
        }catch (ex : Exception){
            ex.printStackTrace()
        }
        return items
    }

    //end


    private fun showRevealView() {
        val cx = binding.fileView.x.toInt() + binding.fileView.width / 2
        val cy = binding.fileView.x.toInt() + binding.fileView.width / 2

        val endRadius = hypot(
            binding.revealView.width.toDouble(),
            binding.revealView.height.toDouble()
        ).toFloat()

        if (hidden) {
            val anim: Animator =
                ViewAnimationUtils.createCircularReveal(binding.revealView, cx, cy, 0f, endRadius)

            binding.revealView.setVisibility(View.VISIBLE)
            anim.duration = 500
            anim.start()
            hidden = false
        } else {
            val anim: Animator =
                ViewAnimationUtils.createCircularReveal(binding.revealView, cx, cy, endRadius, 0f)
            anim.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    binding.revealView.setVisibility(View.INVISIBLE)
                    hidden = true
                }
            })
            anim.duration = 500

            anim.start()
        }
    }

    fun hideRevealView() {
        if (binding.revealView.getVisibility() === View.VISIBLE) {
            showRevealView()
            hidden = true
        }
    }


    private fun startRecording() {
        outputFile = getOutputFile()

        Log.d("bur", "audio file : " + outputFile?.path)
        if (outputFile != null) {
            mediaRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(outputFile!!.absolutePath)

                try {
                    prepare()
                    start()
                    isRecording = true
                    // Start the recording timer
                    recordingStartTime = System.currentTimeMillis()
                    recordingTimerHandler = Handler(Looper.getMainLooper())
                    recordingTimerHandler?.postDelayed(updateTimerRunnable, TIMER_UPDATE_INTERVAL)
                    // recordButton.text = "Stop Recording"
                } catch (e: IOException) {
                    // Handle exception

                    Log.d("bur", "startRecording error: " + e.message)
                }
            }
        }
    }

    private val updateTimerRunnable = object : Runnable {
        override fun run() {
            val elapsedMillis = System.currentTimeMillis() - recordingStartTime
            val elapsedSeconds = elapsedMillis / 1000

            //  recordingTimer.text = String.format("%02d:%02d", elapsedSeconds / 60, elapsedSeconds % 60)

            // Schedule the next update
            recordingTimerHandler?.postDelayed(this, TIMER_UPDATE_INTERVAL)
        }
    }

    private fun stopRecording() {
        if (isRecording) {
            try {
                mediaRecorder.apply {
                    stop()
                    release()
                }
            } catch (e: Exception) {
                Log.d("bur", "stopRecording: " + e.message)
            }

            isRecording = false

            // recordButton.text = "Start Recording"


            // Calculate the recording duration
            val recordingDuration = System.currentTimeMillis() - recordingStartTime


            if (recordingDuration >= MIN_RECORDING_DURATION) {
                // Recording duration is at least 1 second, proceed with sending the recorded audio
                val audioFile = getOutputFile()
                audioFile?.let {

                    uploadAudioFileToServer(outputFile!!, finalRecordTime)


                }
            } else {
                fadeInHoldView(binding.holdTextView, 500)

                Handler().postDelayed({
                    fadeOutHoldView(binding.holdTextView, 1000)
                }, 3000)
            }
        }
    }

    fun fadeInHoldView(view: View, duration: Long) {
        val fadeAnimation = AlphaAnimation(0f, 1f)
        fadeAnimation.duration = duration
        view.startAnimation(fadeAnimation)
        view.visibility = View.VISIBLE
    }

    fun fadeOutHoldView(view: View, duration: Long) {
        val fadeAnimation = AlphaAnimation(1f, 0f)
        fadeAnimation.duration = duration
        view.startAnimation(fadeAnimation)
        view.visibility = View.INVISIBLE
    }

    private fun getOutputFile(): File? {
        val cacheDir = cacheDir // Get app's private cache directory
        return File.createTempFile("voice_", ".mp3", cacheDir)
    }


    //image choose from gallery
    fun pickImageGallery() {

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST_CODE)
    }

    //end

    private var selectedVideoUri: Uri? = null

    //video choose from gallery
    fun pickVideosGallery() {

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "video/*"
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        startActivityForResult(
            Intent.createChooser(intent, "Select Videos"),
            VIDEO_GALLERY_REQUEST_CODE
        )
    }

    //end


    fun convertBytesToMB(sizeInBytes: Long, decimalPlaces: Int): Double {
        val factor = BigDecimal(10).pow(decimalPlaces * 2)
        val result =
            BigDecimal(sizeInBytes).divide(BigDecimal(1024 * 1024), 2, BigDecimal.ROUND_HALF_UP)
        //return result.toDouble() / factor.toDouble()
        return result.toDouble()
    }

    // Get the size of the selected video file
    fun getVideoFileSize(uri: Uri): Long {
        val cursor = contentResolver.query(uri, null, null, null, null)
        val sizeIndex = cursor?.getColumnIndex(OpenableColumns.SIZE)
        cursor?.moveToFirst()
        val size = cursor?.getLong(sizeIndex ?: 0) ?: 0
        cursor?.close()
        return size
    }

    //chose image from camera
    fun pickImageCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, Camera_REQUEST_CODE)
    }
    private var imageUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {

            when (requestCode) {
                GALLERY_REQUEST_CODE -> {
                    CropImage.activity(data?.data).setGuidelines(CropImageView.Guidelines.ON)
                        .start(this)
                    imageUri = data?.data
                    selectedImageUri = imageUri
                }

                VIDEO_GALLERY_REQUEST_CODE -> {
                    val videoUri = data?.data
                    selectedVideoUri = videoUri

                    val videoSize = selectedVideoUri?.let { getVideoFileSize(it) }

                    if (videoSize != null) {
                        if (videoSize <= MAX_VIDEO_SIZE_BYTES) {
                            // Continue with video upload
                            // Implement your upload logic here

                            //  val videoSizeInMB: Double = convertBytesToMB(videoSize,2)

                            //  compressAndUploadVideo(selectedVideoUri!!)

                            generateThumbnail(selectedVideoUri!!)


                        } else {
                            val videoSizeInMB: Double = convertBytesToMB(videoSize, 2)

                            // Display an error message to the user about the file size limit
                            Toast.makeText(
                                this,
                                "Video size " + videoSizeInMB + " is bigger than recommended size of 25 MB ",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }


                }

                Camera_REQUEST_CODE -> {

                    val thumbnail = data?.extras?.get("data") as Bitmap
                    selectedImageBitmap = thumbnail

                    val imageUrl = selectedImageBitmap?.let { saveBitmapToCache2(it) }
                    binding.imageHoldSection.visibility = View.VISIBLE


                    CropImage.activity(imageUrl)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this)

                }
            }


        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (CropImage.getActivityResult(data) != null) {
                val result: CropImage.ActivityResult = CropImage.getActivityResult(data)

                if (resultCode == RESULT_OK) {
                    uri = result.uri
                    binding.imageHoldSection.visibility = View.VISIBLE
                    binding.imageHold.setImageURI(uri)
                    isTextSending = true
                    binding.send.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@ChatRoomActivity, R.drawable.baseline_send_24
                        )
                    )


                    //  binding.imageBan.setImageURI(uri)


                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    val exception: Exception = result.getError()
                    // CustomMessage(this, exception.message!!)
                    Log.d("bur", "onActivityResult: " + exception)
                    Toast.makeText(this, "imsge uri from cache " + exception, Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Log.d("bur", "onActivityResult: null")
                Toast.makeText(this, "null", Toast.LENGTH_SHORT).show()
            }


        }
    }

    //end


    private fun generateThumbnail(uri: Uri) {

        videoUri = uri
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(this, videoUri)
        val thumbnail =
            retriever.frameAtTime // You can also specify a specific time in microseconds
        retriever.release()

        binding.videoHoldSection.visibility = View.VISIBLE

        // Now you can use the generated thumbnail (Bitmap) as needed
        binding.videoHold.setImageBitmap(thumbnail)
        isTextSending = true
        binding.send.setImageDrawable(
            ContextCompat.getDrawable(
                this@ChatRoomActivity, R.drawable.baseline_send_24
            )
        )

    }
    private var selectedImageUri: Uri? = null

    private var selectedImageBitmap: Bitmap? = null

    private fun saveBitmapToCache2(bitmap: Bitmap): Uri? {
        val cacheDir = cacheDir ?: return null
        val imageFile = File(cacheDir, "image.jpg")

        try {
            val outputStream = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.close()

            val authority =
                "com.friendfinapp.dating.fileprovider" // Replace with your app's FileProvider authority
            return FileProvider.getUriForFile(this, authority, imageFile)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }


    //whats animation start from here

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("ClickableViewAccessibility")
    private fun setupRecording() {

        binding.send!!.setOnTouchListener(View.OnTouchListener { view, motionEvent ->
            if (isDeleting) {
                return@OnTouchListener true
            }
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                cancelOffset = (screenWidth / 2.8).toFloat()
                lockOffset = (screenWidth / 2.5).toFloat()
                if (firstX == 0f) {
                    firstX = motionEvent.rawX
                }
                if (firstY == 0f) {
                    firstY = motionEvent.rawY
                }


                if (permissionHelper!!.checkPermissionForRecordAudio()) {
                    if (!isTextSending) {
                        // startRecordingAnimation()
                        startRecord()
                        Log.d("bur", "start record: ")

                    }
                } else {
                    permissionHelper!!.requestPermissionForAudio()
                }


            } else if (motionEvent.action == MotionEvent.ACTION_UP
                || motionEvent.action == MotionEvent.ACTION_CANCEL
            ) {
                if (motionEvent.action == MotionEvent.ACTION_UP) {

                    if (permissionHelper!!.checkPermissionForRecordAudio()) {

                        if (!isTextSending) {
                            // stopRecordingAnimation()
                            stopRecordings(RecordingBehaviour.RELEASED)

                        }
                    }


                }
            } else if (motionEvent.action == MotionEvent.ACTION_MOVE) {
                if (stopTrackingAction) {
                    return@OnTouchListener true
                }
                var direction = UserBehaviour.NONE
                val motionX = Math.abs(firstX - motionEvent.rawX)
                val motionY = Math.abs(firstY - motionEvent.rawY)
                if (if (isLayoutDirectionRightToLeft) motionX > directionOffset && lastX > firstX && lastY > firstY else motionX > directionOffset && lastX < firstX && lastY < firstY) {
                    if (if (isLayoutDirectionRightToLeft) motionX > motionY && lastX > firstX else motionX > motionY && lastX < firstX) {
                        direction = UserBehaviour.CANCELING
                    } else if (motionY > motionX && lastY < firstY) {
                        direction = UserBehaviour.LOCKING
                    }
                } else if (if (isLayoutDirectionRightToLeft) motionX > motionY && motionX > directionOffset && lastX > firstX else motionX > motionY && motionX > directionOffset && lastX < firstX) {
                    direction = UserBehaviour.CANCELING
                } else if (motionY > motionX && motionY > directionOffset && lastY < firstY) {
                    direction = UserBehaviour.LOCKING
                }
                if (direction === UserBehaviour.CANCELING) {
                    if (userBehaviour === UserBehaviour.NONE || motionEvent.rawY + binding.send!!.width / 2 > firstY) {
                        userBehaviour = UserBehaviour.CANCELING
                    }
                    if (userBehaviour === UserBehaviour.CANCELING) {
                        translateX(-(firstX - motionEvent.rawX))
                    }
                } else if (direction === UserBehaviour.LOCKING) {
                    if (userBehaviour === UserBehaviour.NONE || motionEvent.rawX + binding.send!!.width / 2 > firstX) {
                        userBehaviour = UserBehaviour.LOCKING
                    }
                    if (userBehaviour === UserBehaviour.LOCKING) {
                        translateY(-(firstY - motionEvent.rawY))
                    }
                }
                lastX = motionEvent.rawX
                lastY = motionEvent.rawY
            }
            view.onTouchEvent(motionEvent)
            true
        })

        binding.sendLockAudio.setOnClickListener {
            isLocked = false
            stopRecordings(RecordingBehaviour.LOCK_DONE)

        }
        binding.deleteAudio.setOnClickListener {
            isLocked = false
            stopRecordings(RecordingBehaviour.CANCELED)
        }
        binding.pauseAudio.setOnClickListener {
            if (mediaRecorder != null && isRecordingPaused) {
                binding.pauseAudio.setImageResource(R.drawable.ic_playerplaybb)
                resumeRecording()
            } else {
                binding.pauseAudio.setImageResource(R.drawable.ic_playerplayb)
                pauseRecording()
            }
        }
    }

    private var isRecordingPaused = false

    @RequiresApi(Build.VERSION_CODES.N)
    private fun pauseRecording() {
        mediaRecorder.pause()
        isRecordingPaused = true
        stopTimer()
        pauseTime = audioTotalTime

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun resumeRecording() {
        mediaRecorder.resume()
        isRecordingPaused = false
        if (audioTimer == null) {
            audioTotalTime = pauseTime
            audioTimerUpdater()
        }
    }


    fun changeSlideToCancelText(textResourceId: Int) {
        binding.textViewSlide.setText(textResourceId)
    }

    private fun translateY(y: Float) {
        if (y < -lockOffset) {
            locked()
            binding.send.translationY = 0f
            return
        }
        if (binding.layoutLock.visibility != View.VISIBLE) {
            binding.layoutLock.visibility = View.VISIBLE
        }
        binding.send.translationY = y
        binding.layoutLock.translationY = y / 2
        binding.send.translationX = 0f
    }


    private fun translateX(x: Float) {
        if (if (isLayoutDirectionRightToLeft) x > cancelOffset else x < -cancelOffset) {
            canceled()
            binding.send!!.translationX = 0f
            binding.layoutSlideCancel!!.translationX = 0f
            return
        }
        binding.send!!.translationX = x
        binding.layoutSlideCancel!!.translationX = x
        binding.layoutLock!!.translationY = 0f
        binding.send!!.translationY = 0f
        if (Math.abs(x) < binding.imageViewMic!!.width / 2) {
            if (binding.layoutLock!!.visibility != View.VISIBLE) {
                binding.layoutLock!!.visibility = View.VISIBLE
            }
        } else {
            if (binding.layoutLock!!.visibility != View.GONE) {
                binding.layoutLock!!.visibility = View.GONE
            }
        }
    }


    private fun stopRecordings(recordingBehaviour: RecordingBehaviour) {
        stopTrackingAction = true
        firstX = 0f
        firstY = 0f
        lastX = 0f
        lastY = 0f
        userBehaviour = UserBehaviour.NONE
        binding.send!!.animate().scaleX(1f).scaleY(1f).translationX(0f).translationY(0f)
            .setDuration(100).setInterpolator(LinearInterpolator()).start()
        binding.layoutSlideCancel!!.translationX = 0f
        binding.layoutSlideCancel!!.visibility = View.GONE
        binding.layoutLock!!.visibility = View.GONE
        binding.layoutLock!!.translationY = 0f
        binding.imageViewLockArrow!!.clearAnimation()
        binding.imageViewLock!!.clearAnimation()
        if (isLocked) {
            return
        }
        if (recordingBehaviour === RecordingBehaviour.LOCKED) {
            binding.sendLockAudio!!.visibility = View.VISIBLE
            binding.deleteAudio.visibility = View.VISIBLE
            binding.pauseAudio.visibility = View.VISIBLE
            recordingListener?.onRecordingLocked()


            Log.d("bur", "stop record locked: ")
        } else if (recordingBehaviour === RecordingBehaviour.CANCELED) {
            Log.d("bur", "cancelled : ")
            binding.textViewTime.clearAnimation()
            binding.textViewTime.visibility = View.INVISIBLE
            binding.imageViewMic.visibility = View.INVISIBLE
            binding.sendLockAudio.visibility = View.GONE
            binding.deleteAudio.visibility = View.GONE
            binding.pauseAudio.visibility = View.GONE
            // binding.layoutEffect2!!.visibility = View.GONE
            // binding.layoutEffect1!!.visibility = View.GONE
            if (timerTask != null) {
                timerTask!!.cancel()
            }
            audioTotalTime = 0L
            delete()
            recordingListener?.onRecordingCanceled()
            releaseMediaPlayerWhenCancel()
            Log.d("bur", "stop record cancel: ")
        } else if (recordingBehaviour === RecordingBehaviour.RELEASED || recordingBehaviour === RecordingBehaviour.LOCK_DONE) {
            Log.d("bur", " ")
            binding.textViewTime.clearAnimation()
            binding.textViewTime.visibility = View.INVISIBLE
            binding.imageViewMic.visibility = View.INVISIBLE


            binding.edit.visibility = View.VISIBLE
            if (showAttachmentIcon) {
                binding.attachFile.visibility = View.VISIBLE
            }
            if (showCameraIcon) {
                binding.image.visibility = View.VISIBLE
            }
            if (showEmojiIcon) {
                //  binding.imageViewEmoji!!.visibility = View.VISIBLE
            }
            binding.sendLockAudio!!.visibility = View.GONE
            binding.deleteAudio.visibility = View.GONE
            binding.pauseAudio.visibility = View.GONE
            binding.edit.requestFocus()
            // binding.layoutEffect2!!.visibility = View.GONE
            // binding.layoutEffect1!!.visibility = View.GONE
            if (timerTask != null) {
                timerTask!!.cancel()
            }

            audioTotalTime = 0L
            recordingListener?.onRecordingCompleted()
            stopRecording()

            Log.d("bur", "stop record completed: ")

        }
    }

    private fun startRecord() {
        recordingListener?.onRecordingStarted()
        startRecording()
        //hideAttachmentOptionView()

        hideRevealView()
        stopTrackingAction = false
        binding.edit.visibility = View.INVISIBLE
        binding.attachFile!!.visibility = View.INVISIBLE
        binding.image!!.visibility = View.INVISIBLE
        //  binding.imageViewEmoji!!.visibility = View.INVISIBLE
        binding.send!!.animate().scaleXBy(1f).scaleYBy(1f).setDuration(200)
            .setInterpolator(OvershootInterpolator()).start()
        binding.textViewTime!!.visibility = View.VISIBLE
        binding.layoutLock!!.visibility = View.VISIBLE
        binding.layoutSlideCancel!!.visibility = View.VISIBLE
        binding.imageViewMic!!.visibility = View.VISIBLE

        binding.edit.visibility = View.INVISIBLE
        //  binding.layoutEffect2!!.visibility = View.VISIBLE
        //  binding.layoutEffect1!!.visibility = View.VISIBLE
        binding.textViewTime!!.startAnimation(animBlink)
        binding.imageViewLockArrow!!.clearAnimation()
        binding.imageViewLock!!.clearAnimation()
        binding.imageViewLockArrow!!.startAnimation(animJumpFast)
        binding.imageViewLock!!.startAnimation(animJump)

        audioTimerUpdater()

    }

    private var pauseTime: Long = 0
    private fun audioTimerUpdater() {
        if (audioTimer == null) {
            audioTimer = Timer()
            timeFormatter!!.timeZone = TimeZone.getTimeZone("UTC")
        }

        timerTask = object : TimerTask() {
            override fun run() {
                handler?.post {
                    binding.textViewTime.text =
                        timeFormatter!!.format(Date(audioTotalTime.toLong() * 1000))
                    audioTotalTime++
                    finalRecordTime = audioTotalTime * 1000

                }
            }
        }



        audioTimer!!.schedule(timerTask, 0, 1000)
    }

    // Function to stop the timer
    private fun stopTimer() {
        timerTask?.cancel()
        audioTimer?.cancel()
        timerTask = null
        audioTimer = null
    }

    private fun delete() {
        binding.imageViewMic!!.visibility = View.VISIBLE
        binding.imageViewMic!!.rotation = 0f
        isDeleting = true
        binding.send!!.isEnabled = false
        handler!!.postDelayed({
            isDeleting = false
            binding.send!!.isEnabled = true
            binding.edit!!.visibility = View.VISIBLE
            if (showAttachmentIcon) {
                binding.attachFile!!.visibility = View.VISIBLE
            }
            if (showCameraIcon) {
                binding.image!!.visibility = View.VISIBLE
            }
            if (showEmojiIcon) {
                // binding.imageViewEmoji!!.visibility = View.VISIBLE
            }
        }, 1250)


        binding.imageViewMic.animate().translationY(-dp * 150).rotation(180f).scaleXBy(0.6f)
            .scaleYBy(0.6f).setDuration(500)
            .setInterpolator(DecelerateInterpolator())
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator) {
                    var displacement = 0f

                    displacement = if (isLayoutDirectionRightToLeft) {
                        dp * 40
                    } else {
                        -dp * 40
                    }

                    binding.dustin.setTranslationX(displacement)
                    binding.dustinCover.setTranslationX(displacement)

                    binding.dustinCover.animate().translationX(0f).rotation(-120f).setDuration(350)
                        .setInterpolator(
                            DecelerateInterpolator()
                        ).start()

                    binding.dustin.animate().translationX(0f).setDuration(350).setInterpolator(
                        DecelerateInterpolator()
                    ).setListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animation: Animator) {
                            binding.dustin.setVisibility(View.VISIBLE)
                            binding.dustinCover.setVisibility(View.VISIBLE)
                        }

                        override fun onAnimationEnd(animation: Animator) {}
                        override fun onAnimationCancel(animation: Animator) {}
                        override fun onAnimationRepeat(animation: Animator) {}
                    }).start()
                }

                override fun onAnimationEnd(p0: Animator) {
                    binding.imageViewMic!!.animate().translationY(0f).scaleX(1f).scaleY(1f)
                        .setDuration(350)
                        .setInterpolator(LinearInterpolator())
                        .setListener(object : Animator.AnimatorListener {
                            override fun onAnimationStart(p0: Animator) {

                            }

                            override fun onAnimationEnd(p0: Animator) {
                                binding.imageViewMic!!.visibility = View.INVISIBLE
                                binding.imageViewMic!!.rotation = 0f
                                var displacement = 0f
                                displacement = if (isLayoutDirectionRightToLeft) {
                                    dp * 40
                                } else {
                                    -dp * 40
                                }
                                binding.dustinCover!!.animate().rotation(0f).setDuration(150)
                                    .setStartDelay(50)
                                    .start()
                                binding.dustin!!.animate().translationX(displacement)
                                    .setDuration(200)
                                    .setStartDelay(250).setInterpolator(
                                        DecelerateInterpolator()
                                    ).start()
                                binding.dustinCover!!.animate().translationX(displacement)
                                    .setDuration(200)
                                    .setStartDelay(250).setInterpolator(
                                        DecelerateInterpolator()
                                    ).setListener(object : Animator.AnimatorListener {

                                        override fun onAnimationStart(p0: Animator) {

                                        }

                                        override fun onAnimationEnd(p0: Animator) {
                                            Log.d("bur", "onAnimationEnd: ")

                                            // binding.editTextMessage!!.requestFocus()
                                        }

                                        override fun onAnimationCancel(p0: Animator) {

                                        }

                                        override fun onAnimationRepeat(p0: Animator) {

                                        }

                                    }).start()
                            }

                            override fun onAnimationCancel(p0: Animator) {

                            }

                            override fun onAnimationRepeat(p0: Animator) {

                            }

                        }).start()
                }

                override fun onAnimationCancel(p0: Animator) {

                }

                override fun onAnimationRepeat(p0: Animator) {

                }

            }).start()


    }


    private fun locked() {
        stopTrackingAction = true
        stopRecordings(RecordingBehaviour.LOCKED)
        isLocked = true
    }

    private fun canceled() {
        stopTrackingAction = true
        stopRecordings(RecordingBehaviour.CANCELED)
    }

    fun isShowCameraIcon(): Boolean {
        return showCameraIcon
    }

    fun showCameraIcon(showCameraIcon: Boolean) {
        this.showCameraIcon = showCameraIcon
        if (showCameraIcon) {
            binding.image.setVisibility(View.VISIBLE)
        } else {
            binding.image.setVisibility(View.GONE)
        }
    }

    fun isShowAttachmentIcon(): Boolean {
        return showAttachmentIcon
    }

    fun showAttachmentIcon(showAttachmentIcon: Boolean) {
        this.showAttachmentIcon = showAttachmentIcon
        if (showAttachmentIcon) {
            binding.attachFile.setVisibility(View.VISIBLE)
        } else {
            binding.attachFile.setVisibility(View.INVISIBLE)
        }
    }

    fun isShowEmojiIcon(): Boolean {
        return showEmojiIcon
    }

    fun showEmojiIcon(showEmojiIcon: Boolean) {
        this.showEmojiIcon = showEmojiIcon
        if (showEmojiIcon) {
            //  binding.imageViewEmoji.setVisibility(View.VISIBLE)
        } else {
            //binding.imageViewEmoji.setVisibility(View.INVISIBLE)
        }
    }


    fun releaseMediaPlayerWhenCancel() {
        if (isRecording) {
            try {
                mediaRecorder.apply {
                    stop()
                    release()
                }
            } catch (e: Exception) {
                Log.d("bur", "stopRecording: " + e.message)
            }

            isRecording = false

        }
    }


    // End of touch animation and programming of whats up like


    // here we do report user in the chat room as a dialog

    private fun showReportDialog() {
        val btnSheet: View = layoutInflater.inflate(R.layout.dialog_repot_an_abuse, null)
        val dialog: Dialog = BottomSheetDialog(this, R.style.AppBottomSheetDialogThemeNew)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(btnSheet)

        val close = dialog.findViewById<View>(R.id.close) as ImageView
        val report = dialog.findViewById<View>(R.id.report) as TextView


        close.setOnClickListener { dialog.dismiss() }

        report.setOnClickListener {

            startActivity(
                Intent(this, ReportAnAbuse::class.java).putExtra(
                    "reportedUserName", toUserName.trim()
                )
            )
        }


        dialog.show()
    }

    //end


    //getting gesture event while click on recycler view in item or touch any point in the screen

    class RecyclerViewTouchListener(context: Context, private val viewToHide: View) :
        RecyclerView.OnItemTouchListener {

        private val gestureDetector: GestureDetector

        init {
            gestureDetector =
                GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                    override fun onSingleTapUp(e: MotionEvent): Boolean {
                        // Hide the view here


                        ChatRoomActivity.instance?.hideRevealView()

//                    viewToHide.visibility=View.INVISIBLE
//                    hidden=true


                        return true
                    }
                })
        }

        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            gestureDetector.onTouchEvent(e)
            return false
        }

        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
    }

    //end


    //this dialog comes for permission valid for force user to system

    fun showSettingsDialog() {
        // we are displaying an alert dialog for permissions
        val builder = AlertDialog.Builder(this@ChatRoomActivity)

        // below line is the title for our alert dialog.
        builder.setTitle("Need Permissions")

        // below line is our message for our dialog
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")
        builder.setPositiveButton("GOTO SETTINGS") { dialog, which ->
            // this method is called on click on positive button and on clicking shit button
            // we are redirecting our user from our app to the settings page of our app.
            dialog.cancel()
            // below is the intent from which we are redirecting our user.
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivityForResult(intent, 101)
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            // this method is called when user click on negative button.
            dialog.cancel()
        }
        // below line is used to display our dialog
        builder.show()
    }
    //end


    //companion object and override methode

    companion object {
        const val GALLERY_REQUEST_CODE = 20001
        const val Camera_REQUEST_CODE = 20002
        const val VIDEO_GALLERY_REQUEST_CODE = 20003
        private const val MIN_RECORDING_DURATION = 1000L // 1 second
        private const val TIMER_UPDATE_INTERVAL = 1000L // 1 second
        private const val MAX_VIDEO_SIZE_BYTES = 25 * 1024 * 1024 // 10MB

        @JvmStatic
        var instance: ChatRoomActivity? = null
    }

    override fun onBackPressedDispatcher(handler: () -> Unit) {
        super.onBackPressedDispatcher {
            adapter.stopPlayback()
            adapter.exitSelectionMode()
            ChatFragment.instance?.getChatDataWhenBackFromChatRoom()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopPolling()
    }

    override fun onPause() {
        //    handler.removeCallbacks(runnable!!)
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        Log.d("TAG", "onResume: " + selectedImageUri)
        if(MessageForward.shouldReloadChatRoom){
            MessageForward.shouldReloadChatRoom = false
            this.recreate()
        }
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    override fun onDown(e: MotionEvent): Boolean {
        return true
    }

    override fun onShowPress(e: MotionEvent) {}

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        // Hide the view here
        hideRevealView()
        return true
    }

    override fun onScroll(p0: MotionEvent?, p1: MotionEvent, p2: Float, p3: Float): Boolean {
        return true
    }

    override fun onLongPress(e: MotionEvent) {}
    override fun onFling(p0: MotionEvent?, p1: MotionEvent, p2: Float, p3: Float): Boolean {
        return true
    }


}