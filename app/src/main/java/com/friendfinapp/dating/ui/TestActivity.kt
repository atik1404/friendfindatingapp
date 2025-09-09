package com.friendfinapp.dating.ui


import android.animation.Animator
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.friendfinapp.dating.R
import com.friendfinapp.dating.databinding.ActivityTestBinding
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import java.util.Timer
import java.util.TimerTask


class TestActivity : AppCompatActivity() {

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
    private  var animJump:Animation? = null
    private  var animJumpFast:Animation? = null

    private var isDeleting = false
    private var stopTrackingAction = false
    private var handler: Handler? = null

    private var audioTotalTime = 0
    private var timerTask: TimerTask? = null
    private var audioTimer: Timer? = null
    private var timeFormatter: SimpleDateFormat? = null

    private var lastX = 0f
    private  var lastY:kotlin.Float = 0f
    private var firstX = 0f
    private  var firstY:kotlin.Float = 0f

    private val directionOffset =
        0f
    private  var cancelOffset:kotlin.Float = 0f
    private  var lockOffset:kotlin.Float = 0f
    private var dp = 0f
    private var isLocked = false

    private var userBehaviour = UserBehaviour.NONE


    private val recordingListener: RecordingListener? = null

    var isLayoutDirectionRightToLeft = false

    var screenWidth = 0
    var screenHeight:kotlin.Int = 0

//    private val attachmentOptionList: List<AttachmentOption>? = null
//    private val attachmentOptionsListener: AttachmentOptionsListener? = null

    private val layoutAttachments: List<LinearLayout>? = null

    private val context: Context? = null

    private var showCameraIcon = true
    private  var showAttachmentIcon = true
    private  var showEmojiIcon = true
    private val removeAttachmentOptionAnimation = false


    private lateinit var binding: ActivityTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= DataBindingUtil.setContentView(this,R.layout.activity_test)



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

        setupRecording()


    }

    private fun setupRecording() {
       binding. imageViewSend!!.animate().scaleX(0f).scaleY(0f).setDuration(100)
            .setInterpolator(LinearInterpolator()).start()

        binding.imageViewAudio!!.setOnTouchListener(OnTouchListener { view, motionEvent ->
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
                startRecord()
            } else if (motionEvent.action == MotionEvent.ACTION_UP
                || motionEvent.action == MotionEvent.ACTION_CANCEL
            ) {
                if (motionEvent.action == MotionEvent.ACTION_UP) {
                    stopRecording(RecordingBehaviour.RELEASED)
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
                    if (userBehaviour === UserBehaviour.NONE || motionEvent.rawY + binding.imageViewAudio!!.width / 2 > firstY) {
                        userBehaviour = UserBehaviour.CANCELING
                    }
                    if (userBehaviour === UserBehaviour.CANCELING) {
                        translateX(-(firstX - motionEvent.rawX))
                    }
                } else if (direction === UserBehaviour.LOCKING) {
                    if (userBehaviour === UserBehaviour.NONE || motionEvent.rawX + binding.imageViewAudio!!.width / 2 > firstX) {
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

        binding.imageViewStop!!.setOnClickListener {
            isLocked = false
            stopRecording(RecordingBehaviour.LOCK_DONE)
        }
    }


    fun changeSlideToCancelText(textResourceId: Int) {
        binding.textViewSlide.setText(textResourceId)
    }
    private fun translateY(y: Float) {
        if (y < -lockOffset) {
            locked()
            binding.imageViewAudio!!.translationY = 0f
            return
        }
        if (binding.layoutLock!!.visibility != View.VISIBLE) {
            binding.layoutLock!!.visibility = View.VISIBLE
        }
        binding.imageViewAudio!!.translationY = y
        binding.layoutLock!!.translationY = y / 2
        binding.imageViewAudio!!.translationX = 0f
    }


    private fun translateX(x: Float) {
        if (if (isLayoutDirectionRightToLeft) x > cancelOffset else x < -cancelOffset) {
            canceled()
            binding.imageViewAudio!!.translationX = 0f
            binding.layoutSlideCancel!!.translationX = 0f
            return
        }
        binding.imageViewAudio!!.translationX = x
        binding.layoutSlideCancel!!.translationX = x
        binding.layoutLock!!.translationY = 0f
        binding.imageViewAudio!!.translationY = 0f
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

    private fun locked() {
        stopTrackingAction = true
        stopRecording(RecordingBehaviour.LOCKED)
        isLocked = true
    }

    private fun canceled() {
        stopTrackingAction = true
        stopRecording(RecordingBehaviour.CANCELED)
    }

    private fun stopRecording(recordingBehaviour: RecordingBehaviour) {
        stopTrackingAction = true
        firstX = 0f
        firstY = 0f
        lastX = 0f
        lastY = 0f
        userBehaviour = UserBehaviour.NONE
        binding.imageViewAudio!!.animate().scaleX(1f).scaleY(1f).translationX(0f).translationY(0f)
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
            binding.imageViewStop!!.visibility = View.VISIBLE
            recordingListener?.onRecordingLocked()
        } else if (recordingBehaviour === RecordingBehaviour.CANCELED) {
            Log.d("bur", "cancelled : ")
            binding.textViewTime!!.clearAnimation()
            binding.textViewTime!!.visibility = View.INVISIBLE
            binding.imageViewMic!!.visibility = View.INVISIBLE
            binding.imageViewStop!!.visibility = View.GONE
            binding.layoutEffect2!!.visibility = View.GONE
            binding.layoutEffect1!!.visibility = View.GONE
            timerTask!!.cancel()
            delete()
            recordingListener?.onRecordingCanceled()
        } else if (recordingBehaviour === RecordingBehaviour.RELEASED || recordingBehaviour === RecordingBehaviour.LOCK_DONE) {
            Log.d("bur", "release lock done  : ")
            binding.textViewTime!!.clearAnimation()
            binding.textViewTime!!.visibility = View.INVISIBLE
            binding.imageViewMic!!.visibility = View.INVISIBLE


            binding.editTextMessage!!.visibility = View.VISIBLE
            if (showAttachmentIcon) {
                binding.imageViewAttachment!!.visibility = View.VISIBLE
            }
            if (showCameraIcon) {
                binding.imageViewCamera!!.visibility = View.VISIBLE
            }
            if (showEmojiIcon) {
                binding.imageViewEmoji!!.visibility = View.VISIBLE
            }
            binding.imageViewStop!!.visibility = View.GONE
            binding.editTextMessage!!.requestFocus()
            binding.layoutEffect2!!.visibility = View.GONE
            binding.layoutEffect1!!.visibility = View.GONE
            timerTask!!.cancel()
            recordingListener?.onRecordingCompleted()
        }
    }

    private fun startRecord() {
        recordingListener?.onRecordingStarted()
        //hideAttachmentOptionView()
        stopTrackingAction = false
        binding.editTextMessage.visibility = View.INVISIBLE
        binding.imageViewAttachment!!.visibility = View.INVISIBLE
        binding.imageViewCamera!!.visibility = View.INVISIBLE
        binding.imageViewEmoji!!.visibility = View.INVISIBLE
        binding.imageViewAudio!!.animate().scaleXBy(1f).scaleYBy(1f).setDuration(200)
            .setInterpolator(OvershootInterpolator()).start()
        binding.textViewTime!!.visibility = View.VISIBLE
        binding.layoutLock!!.visibility = View.VISIBLE
        binding.layoutSlideCancel!!.visibility = View.VISIBLE
        binding.imageViewMic!!.visibility = View.VISIBLE

        binding.editTextMessage.visibility = View.INVISIBLE
        binding.layoutEffect2!!.visibility = View.VISIBLE
        binding.layoutEffect1!!.visibility = View.VISIBLE
        binding.textViewTime!!.startAnimation(animBlink)
        binding.imageViewLockArrow!!.clearAnimation()
        binding.imageViewLock!!.clearAnimation()
        binding.imageViewLockArrow!!.startAnimation(animJumpFast)
        binding.imageViewLock!!.startAnimation(animJump)
        if (audioTimer == null) {
            audioTimer = Timer()
            timeFormatter!!.timeZone = TimeZone.getTimeZone("UTC")
        }
        timerTask = object : TimerTask() {
            override fun run() {
                handler!!.post {
//                    timeText!!.text = timeFormatter!!.format(Date(audioTotalTime * 1000))
                   // binding.textViewTime!!.text = 0
                    audioTotalTime++
                }
            }
        }
        audioTotalTime = 0
        audioTimer!!.schedule(timerTask, 0, 1000)
    }

    private fun delete() {
        binding.imageViewMic!!.visibility = View.VISIBLE
        binding.imageViewMic!!.rotation = 0f
        isDeleting = true
        binding.imageViewAudio!!.isEnabled = false
        handler!!.postDelayed({
            isDeleting = false
            binding.imageViewAudio!!.isEnabled = true
            if (showAttachmentIcon) {
                binding.imageViewAttachment!!.visibility = View.VISIBLE
            }
            if (showCameraIcon) {
                binding.imageViewCamera!!.visibility = View.VISIBLE
            }
            if (showEmojiIcon) {
                binding.imageViewEmoji!!.visibility = View.VISIBLE
            }
        }, 1250)


        binding.imageViewMic.animate().translationY(-dp * 150).rotation(180f).scaleXBy(0.6f).scaleYBy(0.6f).setDuration(500)
            .setInterpolator(DecelerateInterpolator())
            .setListener(object : Animator.AnimatorListener{
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
                    binding.imageViewMic!!.animate().translationY(0f).scaleX(1f).scaleY(1f).setDuration(350)
                        .setInterpolator(LinearInterpolator()).setListener(object : Animator.AnimatorListener{
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
                                binding.dustinCover!!.animate().rotation(0f).setDuration(150).setStartDelay(50)
                                    .start()
                                binding.dustin!!.animate().translationX(displacement).setDuration(200)
                                    .setStartDelay(250).setInterpolator(
                                        DecelerateInterpolator()
                                    ).start()
                                binding.dustinCover!!.animate().translationX(displacement).setDuration(200)
                                    .setStartDelay(250).setInterpolator(
                                        DecelerateInterpolator()
                                    ).setListener(object : Animator.AnimatorListener {

                                        override fun onAnimationStart(p0: Animator) {

                                        }

                                        override fun onAnimationEnd(p0: Animator) {
                                            Log.d("bur", "onAnimationEnd: ")
                                            binding.editTextMessage!!.visibility = View.VISIBLE
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


    fun isShowCameraIcon(): Boolean {
        return showCameraIcon
    }

    fun showCameraIcon(showCameraIcon: Boolean) {
        this.showCameraIcon = showCameraIcon
        if (showCameraIcon) {
            binding.imageViewCamera.setVisibility(View.VISIBLE)
        } else {
            binding.imageViewCamera.setVisibility(View.GONE)
        }
    }

    fun isShowAttachmentIcon(): Boolean {
        return showAttachmentIcon
    }

    fun showAttachmentIcon(showAttachmentIcon: Boolean) {
        this.showAttachmentIcon = showAttachmentIcon
        if (showAttachmentIcon) {
            binding.imageViewAttachment.setVisibility(View.VISIBLE)
        } else {
            binding.imageViewAttachment.setVisibility(View.INVISIBLE)
        }
    }

    fun isShowEmojiIcon(): Boolean {
        return showEmojiIcon
    }

    fun showEmojiIcon(showEmojiIcon: Boolean) {
        this.showEmojiIcon = showEmojiIcon
        if (showEmojiIcon) {
            binding.imageViewEmoji.setVisibility(View.VISIBLE)
        } else {
            binding.imageViewEmoji.setVisibility(View.INVISIBLE)
        }
    }
}