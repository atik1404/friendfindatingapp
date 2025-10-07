package com.friendfinapp.dating.ui.chatroom.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.friendfinapp.dating.R
import com.friendfinapp.dating.databinding.ItemCustomerChatLeftBinding
import com.friendfinapp.dating.databinding.ItemCustomerChatRightBinding
import com.friendfinapp.dating.helper.Constants
import com.friendfinapp.dating.helper.Constants.TESTEXO
import com.friendfinapp.dating.helper.Constants.length
import com.friendfinapp.dating.helper.ProgressCustomDialog
import com.friendfinapp.dating.helper.changeActivity
import com.friendfinapp.dating.ui.chatroom.responsemodel.LiveChatResponseModel
import com.friendfinapp.dating.ui.videoplay.VideoViewPlayActivity
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.friendfinapp.dating.helper.dateparser.DateTimeFormat
import com.iamkamrul.dateced.DateCed
import com.friendfinapp.dating.helper.dateparser.DateTimeParser.convertReadableDateTime
import com.friendfinapp.dating.helper.dateparser.parseUtcToLocalCompat
import java.text.SimpleDateFormat
import java.util.Locale


class LiveChatAdapter(
    var context: Context, var customDialog: ProgressCustomDialog?, var fromUserName: String

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var lastDateGrouped = ""

    private var currentPlayingPosition: Int = -1
    private var exoPlayer: SimpleExoPlayer? = null
    private var seekBarUpdater: Runnable? = null

    var animationCheck = false
    var typingAnimationCheck = false

    var chatList: MutableList<LiveChatResponseModel.Data> = ArrayList()
    var invokeList: MutableList<LiveChatResponseModel.Data> = ArrayList()


    var myUserName = fromUserName


    //  var onItemClick: ((LiveChatAdapter) -> Unit)? = null
    var onItemLongClick: ((list: MutableList<LiveChatResponseModel.Data>) -> Unit)? = null


    private var isSelectionModeEnabled = false
    private val selectedItems = HashSet<Int>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            val binding = ItemCustomerChatLeftBinding.inflate(
                LayoutInflater.from(
                    context
                ), parent, false
            )
            ViewHolder(binding)
        } else {
            val binding = ItemCustomerChatRightBinding.inflate(
                LayoutInflater.from(
                    context
                ), parent, false
            )
            ViewHolder2(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val sendTime = chatList[position].sendTime ?: ""
        if (holder.itemViewType == 0) {
            if (position == chatList.size - 1) {

                if (animationCheck) {
                    holder.itemView.animation =
                        AnimationUtils.loadAnimation(context, R.anim.anim_slide_in_right)
                    animationCheck = false
                }

            } else if (position > 100) {
                if (animationCheck) {
                    holder.itemView.animation =
                        AnimationUtils.loadAnimation(context, R.anim.anim_slide_in_right)
                    animationCheck = false
                }
            }

            if (selectedItems.contains(position)) {
                holder.itemView.setBackgroundColor(
                    ContextCompat.getColor(holder.itemView.context, R.color.signInColor)
                )
            } else {
                holder.itemView.setBackgroundColor(Color.TRANSPARENT) // or default background
            }

            val viewHolder2: ViewHolder2 = holder as ViewHolder2
            viewHolder2.binding.dateTv.text = chatList[position].effectiveDate
            viewHolder2.binding.dateTv.isVisible = chatList[position].effectiveDate.isNotEmpty()
            if (chatList[position].imageURL.isNullOrEmpty() && chatList[position].audioURL.isNullOrEmpty() && chatList[position].videoURL.isNullOrEmpty()) {

                viewHolder2.binding.imageCard.visibility = View.GONE
                viewHolder2.binding.videoCard.visibility = View.GONE
                viewHolder2.binding.audioView.visibility = View.GONE
                viewHolder2.binding.chatText.visibility = View.VISIBLE


                viewHolder2.binding.chatText.text = chatList[position].body.toString()
                viewHolder2.binding.nameUser.text = chatList[position].fromUsername.toString()


            }
            else if (!chatList[position].imageURL.isNullOrEmpty() && chatList[position].audioURL.isNullOrEmpty() && chatList[position].videoURL.isNullOrEmpty()) {
                viewHolder2.binding.audioView.visibility = View.GONE
                viewHolder2.binding.videoCard.visibility = View.GONE
                viewHolder2.binding.imageCard.visibility = View.VISIBLE
                Glide.with(context)
                    .load(Constants.BaseUrl + chatList[position].imageURL.toString())
                    .diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.logo)
                    .into(viewHolder2.binding.messageImage)



                viewHolder2.binding.chatText.text = chatList[position].body.toString()
                viewHolder2.binding.nameUser.text = chatList[position].fromUsername.toString()
            }
            else if (!chatList[position].videoURL.isNullOrEmpty() && chatList[position].audioURL.isNullOrEmpty() && chatList[position].imageURL.isNullOrEmpty()) {
                viewHolder2.binding.audioView.visibility = View.GONE
                viewHolder2.binding.imageCard.visibility = View.GONE
                viewHolder2.binding.videoCard.visibility = View.VISIBLE


                Glide.with(context).asBitmap()
                    .load("https://friendfin.com/friendfinapi/" + chatList[viewHolder2.adapterPosition].videoURL.toString())
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache the image
                    .placeholder(R.drawable.allvip).into(viewHolder2.binding.messageVideoThumbnil)

                viewHolder2.binding.videoPlayClick.setOnClickListener {
//                        https://friendfin.com/friendfinapi/
                    var bundel = Bundle()
                    bundel.putString(
                        "videoUrl",
                        "https://friendfin.com/friendfinapi/" + chatList[viewHolder2.adapterPosition].videoURL.toString()
                    )

                    context.changeActivity(
                        VideoViewPlayActivity::class.java, bundel
                    )

                }

                viewHolder2.binding.messageVideoThumbnil.setOnClickListener {

                    var bundel = Bundle()
                    bundel.putString(
                        "videoUrl",
                        "https://friendfin.com/friendfinapi/" + chatList[viewHolder2.adapterPosition].videoURL.toString()
                    )

                    context.changeActivity(
                        VideoViewPlayActivity::class.java, bundel
                    )

                }


                viewHolder2.binding.chatText.text = chatList[position].body.toString()
                viewHolder2.binding.nameUser.text = chatList[position].fromUsername.toString()
            }
            else {
                viewHolder2.binding.imageCard.visibility = View.GONE
                viewHolder2.binding.videoCard.visibility = View.GONE
                viewHolder2.binding.chatText.visibility = View.GONE

                viewHolder2.binding.audioView.visibility = View.VISIBLE

                viewHolder2.binding.nameUser.text = chatList[position].fromUsername.toString()
                viewHolder2.binding.textViewDuration.setTextColor(
                    ContextCompat.getColor(
                        context, R.color.white
                    )
                )

                viewHolder2.binding.progressBarParent.isEnabled = false
                viewHolder2.binding.textViewDuration.text =
                    formatTime(chatList[position].audioDuration!!.toString().trim().toLong())
//                viewHolder2.binding.textViewDuration.text=formatTime("7241".toLong())
//                Log.d("bur", "onBindViewHolder: "+chatList[position].audioDuration!!.toString().trim().toLong())

                if (currentPlayingPosition == position) {

                    viewHolder2.binding.progressBarParent.isEnabled = true
                    viewHolder2.binding.imageViewPlayPause.setImageResource(R.drawable.ic_playerplaybb)
                    activeSeekBar = viewHolder2.binding.progressBarParent
                    startSeekBarUpdater(
                        viewHolder2.binding.progressBarParent

                    )
                    if (exoPlayer?.playWhenReady == false) {
                        //viewHolder2.binding.textViewDuration.text=formatTime(Constants.duration)

                        viewHolder2.binding.imageViewPlayPause.setImageResource(R.drawable.ic_playerplayb)
                        viewHolder2.binding.progressBarParent.progress = calculateSeekBarProgress()

                    } else {

                        viewHolder2.binding.imageViewPlayPause.setImageResource(R.drawable.ic_playerplaybb)
                        viewHolder2.binding.progressBarParent.progress = calculateSeekBarProgress()

                    }

                } else {

                    viewHolder2.binding.progressBarParent.isEnabled = false
                    viewHolder2.binding.imageViewPlayPause.setImageResource(R.drawable.ic_playerplayb)
                    viewHolder2.binding.progressBarParent.progress = 0

                    if (activeSeekBar == viewHolder2.binding.progressBarParent) {
                        activeSeekBar = null
                        stopSeekBarUpdater()
                    }

                }

                viewHolder2.binding.imageViewPlayPause.setOnClickListener {

                    if (currentPlayingPosition == position) {
                        // Clicked on the currently playing item, so pause the playback
                        if (exoPlayer?.playWhenReady == true) {

                            viewHolder2.binding.imageViewPlayPause.setImageResource(R.drawable.ic_playerplayb)
                            exoPlayer!!.playWhenReady = false
                            exoPlayer!!.playbackState
                            length = exoPlayer!!.currentPosition.toInt()
                            TESTEXO = false
                        } else {
                            //                            // Start playing the media
                            exoPlayer?.playWhenReady = true
                            exoPlayer!!.playbackState
                            TESTEXO = true
                            viewHolder2.binding.imageViewPlayPause.setImageResource(R.drawable.ic_playerplaybb)
                        }

                    } else {
                        TESTEXO = true

                        // Clicked on a different item, so stop the previous playback and start playback for the new item
                        stopPlayback()
                        startPlayback(
                            "https://friendfin.com/friendfinapi/" + chatList[viewHolder2.adapterPosition].audioURL.toString()
                                .trim(), viewHolder2.binding.progressBarParent, position
                        )
                    }

                }

                viewHolder2.binding.progressBarParent.setOnSeekBarChangeListener(object :
                    SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?, progress: Int, fromUser: Boolean
                    ) {
                        if (fromUser) {
                            // Seek to the corresponding position in the audio player
                            exoPlayer?.seekTo(
                                (((progress / 100f * exoPlayer?.duration!!) ?: 0)).toLong()
                            )
                            notifyDataSetChanged()
                        }
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {
                        // Not needed for this implementation

                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {
                        // Not needed for this implementation

                    }
                })

            }

            try {
                viewHolder2.binding.sendTimeTv.text = chatList[position].sendTime?.parseUtcToLocalCompat(DateTimeFormat.sqlhma)
            } catch (ex: Exception) {
                viewHolder2.binding.sendTimeTv.text = DateCed.Factory.now().hMa
            }
        }
        else {
            val viewHolder: ViewHolder = holder as ViewHolder
            viewHolder.binding.dateTv.text = chatList[position].effectiveDate
            viewHolder.binding.dateTv.isVisible = chatList[position].effectiveDate.isNotEmpty()

            if (position == chatList.size - 1) {
                Log.d("TAG", "onBindViewHolder: problem" + position)
                viewHolder.binding.chatTextLayout.visibility = View.GONE
                if (typingAnimationCheck) {
                    Log.d("TAG", "onBindViewHolder: problem2" + position)
                    viewHolder.binding.animation.visibility = View.VISIBLE
                    holder.itemView.animation =
                        AnimationUtils.loadAnimation(context, R.anim.anim_slide_in_right)

                    typingAnimationCheck = false

                    var handler = Handler()
                    handler.postDelayed(Runnable {

                        viewHolder.binding.animation.visibility = View.GONE
                        viewHolder.binding.chatTextLayout.visibility = View.VISIBLE


                        //                       viewHolder.binding.chatText.text=chatList[position].body.toString()
//                       viewHolder.binding.nameUser.text=chatList[position].fromUsername.toString()


                        if (chatList[position].imageURL.isNullOrEmpty() && chatList[position].audioURL.isNullOrEmpty() && chatList[position].videoURL.isNullOrEmpty()) {
                            viewHolder.binding.imageCard.visibility = View.GONE
                            viewHolder.binding.videoCard.visibility = View.GONE
                            viewHolder.binding.audioView.visibility = View.GONE
                            viewHolder.binding.chatText.visibility = View.VISIBLE


                            viewHolder.binding.chatText.text = chatList[position].body.toString()
                            viewHolder.binding.nameUser.text =
                                chatList[position].fromUsername.toString()
                        } else if (!chatList[position].imageURL.isNullOrEmpty() && chatList[position].audioURL.isNullOrEmpty() && chatList[position].videoURL.isNullOrEmpty()) {
                            viewHolder.binding.audioView.visibility = View.GONE
                            viewHolder.binding.videoCard.visibility = View.GONE
                            viewHolder.binding.imageCard.visibility = View.VISIBLE
                            Glide.with(context)
                                // .load(Base64.getDecoder().decode(imageBytes))
                                .load(Constants.BaseUrl + chatList[position].imageURL.toString())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(R.drawable.logo).into(viewHolder.binding.messageImage)

                            viewHolder.binding.chatText.text = chatList[position].body.toString()
                            viewHolder.binding.nameUser.text =
                                chatList[position].fromUsername.toString()
                        } else if (!chatList[position].videoURL.isNullOrEmpty() && chatList[position].imageURL.isNullOrEmpty() && chatList[position].audioURL.isNullOrEmpty()) {
                            viewHolder.binding.audioView.visibility = View.GONE
                            viewHolder.binding.videoCard.visibility = View.VISIBLE
                            viewHolder.binding.imageCard.visibility = View.GONE


                            Glide.with(context).asBitmap()
                                .load("https://friendfin.com/friendfinapi/" + chatList[viewHolder.adapterPosition].videoURL.toString())
                                .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache the image
                                .placeholder(R.drawable.allvip)
                                .into(viewHolder.binding.messageVideoThumbnil)


                            viewHolder.binding.videoPlayClick.setOnClickListener {
//                        https://friendfin.com/friendfinapi/
                                var bundel = Bundle()
                                bundel.putString(
                                    "videoUrl",
                                    "https://friendfin.com/friendfinapi/" + chatList[viewHolder.adapterPosition].videoURL.toString()
                                )

                                context.changeActivity(
                                    VideoViewPlayActivity::class.java, bundel
                                )

                            }
                            viewHolder.binding.messageVideoThumbnil.setOnClickListener {

                                var bundel = Bundle()
                                bundel.putString(
                                    "videoUrl",
                                    "https://friendfin.com/friendfinapi/" + chatList[viewHolder.adapterPosition].videoURL.toString()
                                )

                                context.changeActivity(
                                    VideoViewPlayActivity::class.java, bundel
                                )

                            }
                            viewHolder.binding.chatText.text = chatList[position].body.toString()
                            viewHolder.binding.nameUser.text =
                                chatList[position].fromUsername.toString()
                        } else {
                            viewHolder.binding.imageCard.visibility = View.GONE
                            viewHolder.binding.videoCard.visibility = View.GONE
                            viewHolder.binding.chatText.visibility = View.GONE

                            viewHolder.binding.audioView.visibility = View.VISIBLE

                            viewHolder.binding.nameUser.text =
                                chatList[position].fromUsername.toString()
                            viewHolder.binding.textViewDuration.setTextColor(
                                ContextCompat.getColor(
                                    context, R.color.black
                                )
                            )

                            viewHolder.binding.progressBarParent.isEnabled = false
                            viewHolder.binding.textViewDuration.text = formatTime(
                                chatList[position].audioDuration!!.toString().trim().toLong()
                            )
                            if (currentPlayingPosition == position) {

                                viewHolder.binding.progressBarParent.isEnabled = true
                                viewHolder.binding.imageViewPlayPause.setImageResource(R.drawable.ic_playerplaybb)
                                activeSeekBar = viewHolder.binding.progressBarParent
                                startSeekBarUpdater(
                                    viewHolder.binding.progressBarParent
                                )
                                if (exoPlayer?.playWhenReady == false) {

                                    viewHolder.binding.imageViewPlayPause.setImageResource(R.drawable.ic_playerplayb)
                                    viewHolder.binding.progressBarParent.progress =
                                        calculateSeekBarProgress()

                                } else {

                                    viewHolder.binding.imageViewPlayPause.setImageResource(R.drawable.ic_playerplaybb)
                                    viewHolder.binding.progressBarParent.progress =
                                        calculateSeekBarProgress()

                                }

                            } else {

                                viewHolder.binding.progressBarParent.isEnabled = false
                                viewHolder.binding.imageViewPlayPause.setImageResource(R.drawable.ic_playerplayb)
                                viewHolder.binding.progressBarParent.progress = 0
                                if (activeSeekBar == viewHolder.binding.progressBarParent) {
                                    activeSeekBar = null
                                    stopSeekBarUpdater()
                                }

                            }

                            viewHolder.binding.imageViewPlayPause.setOnClickListener {

                                if (currentPlayingPosition == position) {
                                    // Clicked on the currently playing item, so pause the playback
                                    if (exoPlayer?.playWhenReady == true) {
                                        viewHolder.binding.imageViewPlayPause.setImageResource(R.drawable.ic_playerplayb)
                                        exoPlayer!!.playWhenReady = false
                                        exoPlayer!!.playbackState
                                        length = exoPlayer!!.currentPosition.toInt()
                                        TESTEXO = false
                                    } else {
                                        //                            // Start playing the media
                                        exoPlayer?.playWhenReady = true
                                        exoPlayer!!.playbackState
                                        TESTEXO = true
                                        viewHolder.binding.imageViewPlayPause.setImageResource(R.drawable.ic_playerplaybb)
                                    }

                                } else {
                                    TESTEXO = true

                                    stopPlayback()
                                    startPlayback(
                                        "https://friendfin.com/friendfinapi/" + chatList[viewHolder.adapterPosition].audioURL.toString()
                                            .trim(), viewHolder.binding.progressBarParent, position
                                    )
                                }
                            }

                            viewHolder.binding.progressBarParent.setOnSeekBarChangeListener(object :
                                SeekBar.OnSeekBarChangeListener {
                                override fun onProgressChanged(
                                    seekBar: SeekBar?, progress: Int, fromUser: Boolean
                                ) {
                                    if (fromUser) {
                                        // Seek to the corresponding position in the audio player
                                        exoPlayer?.seekTo(
                                            (((progress / 100f * exoPlayer?.duration!!)
                                                ?: 0)).toLong()
                                        )
                                        notifyDataSetChanged()
                                    }
                                }

                                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                                    // Not needed for this implementation
                                }

                                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                                }
                            })


                        }


                    }, 2000)

                } else {
                    viewHolder.binding.animation.visibility = View.GONE
                    viewHolder.binding.chatTextLayout.visibility = View.VISIBLE

                    if (chatList[position].imageURL.isNullOrEmpty() && chatList[position].audioURL.isNullOrEmpty() && chatList[position].videoURL.isNullOrEmpty()) {
                        viewHolder.binding.imageCard.visibility = View.GONE
                        viewHolder.binding.videoCard.visibility = View.GONE
                        viewHolder.binding.audioView.visibility = View.GONE
                        viewHolder.binding.chatText.visibility = View.VISIBLE


                        viewHolder.binding.chatText.text = chatList[position].body.toString()
                        viewHolder.binding.nameUser.text =
                            chatList[position].fromUsername.toString()
                    } else if (!chatList[position].imageURL.isNullOrEmpty() && chatList[position].audioURL.isNullOrEmpty() && chatList[position].videoURL.isNullOrEmpty()) {
                        viewHolder.binding.audioView.visibility = View.GONE
                        viewHolder.binding.videoCard.visibility = View.GONE
                        viewHolder.binding.imageCard.visibility = View.VISIBLE
                        Glide.with(context)
                            // .load(Base64.getDecoder().decode(imageBytes))
                            .load(Constants.BaseUrl + chatList[position].imageURL.toString())
                            .diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.logo)
                            .into(viewHolder.binding.messageImage)

                        viewHolder.binding.chatText.text = chatList[position].body.toString()
                        viewHolder.binding.nameUser.text =
                            chatList[position].fromUsername.toString()
                    } else if (!chatList[position].videoURL.isNullOrEmpty() && chatList[position].audioURL.isNullOrEmpty() && chatList[position].audioURL.isNullOrEmpty()) {

                        viewHolder.binding.audioView.visibility = View.GONE
                        viewHolder.binding.videoCard.visibility = View.VISIBLE
                        viewHolder.binding.imageCard.visibility = View.GONE


                        Glide.with(context).asBitmap()
                            .load("https://friendfin.com/friendfinapi/" + chatList[viewHolder.adapterPosition].videoURL.toString())
                            .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache the image
                            .placeholder(R.drawable.allvip)
                            .into(viewHolder.binding.messageVideoThumbnil)

                        viewHolder.binding.videoPlayClick.setOnClickListener {
//                        https://friendfin.com/friendfinapi/
                            var bundel = Bundle()
                            bundel.putString(
                                "videoUrl",
                                "https://friendfin.com/friendfinapi/" + chatList[viewHolder.adapterPosition].videoURL.toString()
                            )

                            context.changeActivity(
                                VideoViewPlayActivity::class.java, bundel
                            )

                        }

                        viewHolder.binding.messageVideoThumbnil.setOnClickListener {

                            var bundel = Bundle()
                            bundel.putString(
                                "videoUrl",
                                "https://friendfin.com/friendfinapi/" + chatList[viewHolder.adapterPosition].videoURL.toString()
                            )

                            context.changeActivity(
                                VideoViewPlayActivity::class.java, bundel
                            )

                        }

                        viewHolder.binding.chatText.text = chatList[position].body.toString()
                        viewHolder.binding.nameUser.text =
                            chatList[position].fromUsername.toString()
                    } else {
                        viewHolder.binding.imageCard.visibility = View.GONE
                        viewHolder.binding.videoCard.visibility = View.GONE
                        viewHolder.binding.chatText.visibility = View.GONE

                        viewHolder.binding.audioView.visibility = View.VISIBLE

                        viewHolder.binding.nameUser.text =
                            chatList[position].fromUsername.toString()
                        viewHolder.binding.textViewDuration.setTextColor(
                            ContextCompat.getColor(
                                context, R.color.black
                            )
                        )

                        viewHolder.binding.progressBarParent.isEnabled = false
                        viewHolder.binding.textViewDuration.text = formatTime(
                            chatList[position].audioDuration!!.toString().trim().toLong()
                        )
                        if (currentPlayingPosition == position) {

                            viewHolder.binding.progressBarParent.isEnabled = true
                            viewHolder.binding.imageViewPlayPause.setImageResource(R.drawable.ic_playerplaybb)
                            activeSeekBar = viewHolder.binding.progressBarParent
                            startSeekBarUpdater(
                                viewHolder.binding.progressBarParent
                            )
                            if (exoPlayer?.playWhenReady == false) {

                                viewHolder.binding.imageViewPlayPause.setImageResource(R.drawable.ic_playerplayb)
                                viewHolder.binding.progressBarParent.progress =
                                    calculateSeekBarProgress()

                            } else {
                                viewHolder.binding.textViewDuration.visibility = View.VISIBLE
                                viewHolder.binding.imageViewPlayPause.setImageResource(R.drawable.ic_playerplaybb)
                                viewHolder.binding.progressBarParent.progress =
                                    calculateSeekBarProgress()

                            }

                        } else {

                            viewHolder.binding.progressBarParent.isEnabled = false
                            viewHolder.binding.imageViewPlayPause.setImageResource(R.drawable.ic_playerplayb)
                            viewHolder.binding.progressBarParent.progress = 0
                            if (activeSeekBar == viewHolder.binding.progressBarParent) {
                                activeSeekBar = null
                                stopSeekBarUpdater()
                            }

                        }

                        viewHolder.binding.imageViewPlayPause.setOnClickListener {

                            if (currentPlayingPosition == position) {
                                if (exoPlayer?.playWhenReady == true) {
                                    viewHolder.binding.imageViewPlayPause.setImageResource(R.drawable.ic_playerplayb)
                                    exoPlayer!!.playWhenReady = false
                                    exoPlayer!!.playbackState
                                    length = exoPlayer!!.currentPosition.toInt()
                                    TESTEXO = false
                                } else {
                                    //                            // Start playing the media
                                    exoPlayer?.playWhenReady = true
                                    exoPlayer!!.playbackState
                                    TESTEXO = true
                                    viewHolder.binding.imageViewPlayPause.setImageResource(R.drawable.ic_playerplaybb)
                                }

                            } else {
                                TESTEXO = true

                                stopPlayback()
                                startPlayback(
                                    "https://friendfin.com/friendfinapi/" + chatList[viewHolder.adapterPosition].audioURL.toString()
                                        .trim(), viewHolder.binding.progressBarParent, position
                                )
                            }
                        }

                        viewHolder.binding.progressBarParent.setOnSeekBarChangeListener(object :
                            SeekBar.OnSeekBarChangeListener {
                            override fun onProgressChanged(
                                seekBar: SeekBar?, progress: Int, fromUser: Boolean
                            ) {
                                if (fromUser) {
                                    // Seek to the corresponding position in the audio player
                                    exoPlayer?.seekTo(
                                        (((progress / 100f * exoPlayer?.duration!!) ?: 0)).toLong()
                                    )
                                    notifyDataSetChanged()
                                }
                            }

                            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                                // Not needed for this implementation

                            }

                            override fun onStopTrackingTouch(seekBar: SeekBar?) {

                            }
                        })

                    }
                }

            }
            else {
                viewHolder.binding.animation.visibility = View.GONE


                if (chatList[position].imageURL.isNullOrEmpty() && chatList[position].audioURL.isNullOrEmpty() && chatList[position].videoURL.isNullOrEmpty()) {
                    viewHolder.binding.imageCard.visibility = View.GONE
                    viewHolder.binding.videoCard.visibility = View.GONE
                    viewHolder.binding.audioView.visibility = View.GONE
                    viewHolder.binding.chatText.visibility = View.VISIBLE


                    viewHolder.binding.chatText.text = chatList[position].body.toString()
                    viewHolder.binding.nameUser.text = chatList[position].fromUsername.toString()
                } else if (!chatList[position].imageURL.isNullOrEmpty() && chatList[position].audioURL.isNullOrEmpty() && chatList[position].videoURL.isNullOrEmpty()) {
                    viewHolder.binding.audioView.visibility = View.GONE
                    viewHolder.binding.videoCard.visibility = View.GONE
                    viewHolder.binding.imageCard.visibility = View.VISIBLE
                    Glide.with(context)
                        .load(Constants.BaseUrl + chatList[position].imageURL.toString())
                        .diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.logo)
                        .into(viewHolder.binding.messageImage)

                    viewHolder.binding.chatText.text = chatList[position].body.toString()
                    viewHolder.binding.nameUser.text = chatList[position].fromUsername.toString()
                } else if (!chatList[position].videoURL.isNullOrEmpty() && chatList[position].audioURL.isNullOrEmpty() && chatList[position].imageURL.isNullOrEmpty()) {

                    viewHolder.binding.audioView.visibility = View.GONE
                    viewHolder.binding.videoCard.visibility = View.VISIBLE
                    viewHolder.binding.imageCard.visibility = View.GONE



                    Glide.with(context).asBitmap()
                        .load("https://friendfin.com/friendfinapi/" + chatList[viewHolder.adapterPosition].videoURL.toString())
                        .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache the image
                        .placeholder(R.drawable.allvip)
                        .into(viewHolder.binding.messageVideoThumbnil)

                    viewHolder.binding.videoPlayClick.setOnClickListener {

                        var bundel = Bundle()
                        bundel.putString(
                            "videoUrl",
                            "https://friendfin.com/friendfinapi/" + chatList[viewHolder.adapterPosition].videoURL.toString()
                        )

                        context.changeActivity(
                            VideoViewPlayActivity::class.java, bundel
                        )

                    }
                    viewHolder.binding.messageVideoThumbnil.setOnClickListener {

                        var bundel = Bundle()
                        bundel.putString(
                            "videoUrl",
                            "https://friendfin.com/friendfinapi/" + chatList[viewHolder.adapterPosition].videoURL.toString()
                        )

                        context.changeActivity(
                            VideoViewPlayActivity::class.java, bundel
                        )

                    }

                    viewHolder.binding.chatText.text = chatList[position].body.toString()
                    viewHolder.binding.nameUser.text = chatList[position].fromUsername.toString()
                } else {
                    viewHolder.binding.imageCard.visibility = View.GONE
                    viewHolder.binding.videoCard.visibility = View.GONE
                    viewHolder.binding.chatText.visibility = View.GONE

                    viewHolder.binding.audioView.visibility = View.VISIBLE

                    viewHolder.binding.nameUser.text = chatList[position].fromUsername.toString()
                    viewHolder.binding.textViewDuration.setTextColor(
                        ContextCompat.getColor(
                            context, R.color.black
                        )
                    )
                    viewHolder.binding.textViewDuration.text =
                        formatTime(chatList[position].audioDuration!!.toString().trim().toLong())

                    viewHolder.binding.progressBarParent.isEnabled = false
                    if (currentPlayingPosition == position) {
                        viewHolder.binding.progressBarParent.isEnabled = true
                        viewHolder.binding.imageViewPlayPause.setImageResource(R.drawable.ic_playerplaybb)
                        activeSeekBar = viewHolder.binding.progressBarParent
                        startSeekBarUpdater(
                            viewHolder.binding.progressBarParent
                        )
                        if (exoPlayer?.playWhenReady == false) {

                            viewHolder.binding.imageViewPlayPause.setImageResource(R.drawable.ic_playerplayb)
                            viewHolder.binding.progressBarParent.progress =
                                calculateSeekBarProgress()

                        } else {

                            viewHolder.binding.imageViewPlayPause.setImageResource(R.drawable.ic_playerplaybb)
                            viewHolder.binding.progressBarParent.progress =
                                calculateSeekBarProgress()

                        }

                    } else {
                        viewHolder.binding.progressBarParent.isEnabled = false
                        viewHolder.binding.imageViewPlayPause.setImageResource(R.drawable.ic_playerplayb)
                        viewHolder.binding.progressBarParent.progress = 0
                        if (activeSeekBar == viewHolder.binding.progressBarParent) {
                            activeSeekBar = null
                            stopSeekBarUpdater()
                        }

                    }

                    viewHolder.binding.imageViewPlayPause.setOnClickListener {

                        if (currentPlayingPosition == position) {
                            // Clicked on the currently playing item, so pause the playback
                            if (exoPlayer?.playWhenReady == true) {
                                viewHolder.binding.imageViewPlayPause.setImageResource(R.drawable.ic_playerplayb)
                                exoPlayer!!.playWhenReady = false
                                exoPlayer!!.playbackState
                                length = exoPlayer!!.currentPosition.toInt()
                                TESTEXO = false
                            } else {
                                //                            // Start playing the media
                                exoPlayer?.playWhenReady = true
                                exoPlayer!!.playbackState
                                TESTEXO = true
                                viewHolder.binding.imageViewPlayPause.setImageResource(R.drawable.ic_playerplaybb)
                            }

                        } else {
                            TESTEXO = true

                            stopPlayback()
                            startPlayback(
                                "https://friendfin.com/friendfinapi/" + chatList[viewHolder.adapterPosition].audioURL.toString()
                                    .trim(), viewHolder.binding.progressBarParent, position
                            )
                        }

                    }

                    viewHolder.binding.progressBarParent.setOnSeekBarChangeListener(object :
                        SeekBar.OnSeekBarChangeListener {
                        override fun onProgressChanged(
                            seekBar: SeekBar?, progress: Int, fromUser: Boolean
                        ) {
                            if (fromUser) {
                                // Seek to the corresponding position in the audio player
                                exoPlayer?.seekTo(
                                    (((progress / 100f * exoPlayer?.duration!!) ?: 0)).toLong()
                                )
                                notifyDataSetChanged()
                            }
                        }

                        override fun onStartTrackingTouch(seekBar: SeekBar?) {
                            // Not needed for this implementation
                        }

                        override fun onStopTrackingTouch(seekBar: SeekBar?) {
                            // Not needed for this implementation
                        }
                    })
                }
            }

            try {
                viewHolder.binding.sendTimeTv.text =  chatList[position].sendTime?.parseUtcToLocalCompat(DateTimeFormat.sqlhma)
            } catch (ex: Exception) {
                viewHolder.binding.sendTimeTv.text = DateCed.Factory.now().hMa
            }
        }

        holder.itemView.setOnClickListener {
            toggleSelectionMode(position, holder)
        }

        val item = chatList[position]
        holder.itemView.setOnLongClickListener {
            toggleSelectionMode(position, holder)
            return@setOnLongClickListener true
        }

        if (selectedItems.contains(position)) {
            holder.itemView.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context, R.color.signInColor
                )
            )
        } else {
            holder.itemView.background = null
        }

        lastDateGrouped = sendTime.convertReadableDateTime(DateTimeFormat.ddmmyyyy24H, DateTimeFormat.outputDMy)
    }

    override fun getItemViewType(position: Int): Int {
        return if (chatList[position].fromUsername.equals(myUserName)) {
            0
        } else {
            1
        }
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    inner class ViewHolder(var binding: ItemCustomerChatLeftBinding) : RecyclerView.ViewHolder(
        binding.root
    )

    inner class ViewHolder2(var binding: ItemCustomerChatRightBinding) : RecyclerView.ViewHolder(
        binding.root
    )

    fun addData(chatItem: List<LiveChatResponseModel.Data>) {

        chatList.clear()
        chatList.addAll(chatItem)

        notifyDataSetChanged()
    }

    fun addDataNewItem(chatItem: List<LiveChatResponseModel.Data>) {
        if (chatList.isNotEmpty()) {
            if (chatList[chatList.size - 1].body.toString() != chatItem[chatItem.size - 1].body.toString() ||
                chatList[chatList.size - 1].imageURL.toString() != chatItem[chatItem.size - 1].imageURL.toString() ||
                chatList[chatList.size - 1].audioURL.toString() != chatItem[chatItem.size - 1].audioURL.toString() ||
                chatList[chatList.size - 1].videoURL.toString() != chatItem[chatItem.size - 1].videoURL.toString()) {

                chatList.addAll(listOf(chatItem[chatItem.size - 1]))

                //notifyItemInserted(chatList.size - chatItem.size)
                Log.d("TAG", "addDataNewItem before notify : " + chatList.size)
                notifyItemInserted(chatList.size - 1)
                Log.d("TAG", "addDataNewItemafter notify : " + chatList.size)
                animationCheck = true
                typingAnimationCheck = true
            }
        } else {
            if (chatItem.isNotEmpty()) {
                chatList.addAll(listOf(chatItem[chatList.size]))
                animationCheck = true
                typingAnimationCheck = true
            }
        }
    }

    private fun startPlayback(
        audioUrl: String,
        seekBar: SeekBar,
        position: Int,

        ) {
        currentPlayingPosition = position
        stopPlayback()
        val mediaUri = Uri.parse(audioUrl)
        val mediaItem = MediaItem.fromUri(mediaUri)

        exoPlayer = SimpleExoPlayer.Builder(seekBar.context).build()
        exoPlayer?.apply {
            exoPlayer!!.setMediaItem(mediaItem)
            exoPlayer!!.prepare()
            exoPlayer!!.play()
            playWhenReady = true
            addListener(object : Player.Listener {
                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    when (playbackState) {

                        Player.STATE_IDLE -> {
                            Log.d("bur", "ideal: ")
                        }

                        Player.STATE_ENDED -> {
                            stopSeekBarUpdater()
                            currentPlayingPosition = -1
                            notifyDataSetChanged()
                        }

                        Player.STATE_READY -> {
                            TESTEXO = true
                            activeSeekBar = seekBar

                            startSeekBarUpdater(seekBar)
                            notifyDataSetChanged()
                        }

                        Player.STATE_BUFFERING -> {
                            Log.d("bur", "onPlayerStateChanged: " + exoPlayer?.duration)
                            // customDialog?.show()
                        }
                    }
                }
            })
        }

        notifyDataSetChanged()
    }

    private fun pausePlayback() {
        exoPlayer?.playWhenReady = false
        stopSeekBarUpdater()
        notifyDataSetChanged()
    }

    fun stopPlayback() {
        exoPlayer?.stop()
        exoPlayer?.release()
        exoPlayer = null
        activeSeekBar?.progress = 0
        activeSeekBar = null
        stopSeekBarUpdater()
    }

    private fun startSeekBarUpdater(seekBar: SeekBar) {
        stopSeekBarUpdater()
        seekBarUpdater = Runnable {
            exoPlayer?.let { player ->


                var current = length

                val currentPosition = player.currentPosition.toInt()
                val totalDuration = player.duration.toInt()
                val progress = (currentPosition.toFloat() / totalDuration * 100).toInt()


                //seekBar.isIndeterminate = false
                seekBar.progress = progress

                Constants.duration = totalDuration.toLong()

                if (player.playWhenReady) {
                    startSeekBarUpdater(seekBar)
                }

            }
        }

        seekBarUpdater?.let { handler.postDelayed(it, 1000) }
    }

    private fun formatTime(timeInMillis: Long): String {
        val formatter = SimpleDateFormat("mm:ss", Locale.getDefault())
        return formatter.format(timeInMillis)
    }

    private val handler: Handler = Handler()
    private fun stopSeekBarUpdater() {
        seekBarUpdater?.let { handler.removeCallbacks(it) }
        seekBarUpdater = null
    }

    private var activeSeekBar: SeekBar? = null

    private fun calculateSeekBarProgress(): Int {
        val currentPosition = exoPlayer?.currentPosition ?: 0
        val totalDuration = exoPlayer?.duration ?: 0
        return if (totalDuration > 0) {
            (currentPosition.toFloat() / totalDuration * 100).toInt()
        } else {
            0
        }
    }


    class ThumbnailDownloader(private val imageView: ImageView) :
        AsyncTask<String, Void, Bitmap?>() {

        override fun doInBackground(vararg params: String?): Bitmap? {
            val videoUrl = params[0]

            try {
                val retriever = MediaMetadataRetriever()
                retriever.setDataSource(videoUrl, HashMap())

                // Retrieve the frame at a specific time (e.g., 1 second into the video)
                val timeUs = 1000000L // 1 second in microseconds
                val bitmap =
                    retriever.getFrameAtTime(timeUs, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)

                retriever.release()
                return bitmap
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }

        override fun onPostExecute(result: Bitmap?) {
            if (result != null) {
                imageView.setImageBitmap(result)
            } else {
                // Handle the case where thumbnail retrieval fails, you can set a placeholder image here
            }
        }
    }


    private fun toggleSelectionMode(position: Int, holder: RecyclerView.ViewHolder) {


        if (isSelectionModeEnabled) {


            toggleSelection(position, holder)
            Log.d("bu", "selection mode call : ")

        } else {
            // Enter selection mode
            isSelectionModeEnabled = true
            toggleSelection(position, holder)


        }
    }

    private fun toggleSelection(position: Int, holder: RecyclerView.ViewHolder) {
        //  Log.d("bur", "toggleSelection call: " + position)


        // ChatRoomActivity.instance?.selectedMessageList?.clear()
        if (selectedItems.contains(position)) {
            selectedItems.remove(position)
            invokeList.remove(chatList[position])

            onItemLongClick?.invoke(invokeList)
            draw(holder, position)

        } else {

            if (selectedItems.size == 5) {

                Toast.makeText(context, "You can only share upto 5 messages", Toast.LENGTH_SHORT)
                    .show()

            } else {


                //Log.d("TAG", "toggleSelection: " + position)
                selectedItems.add(position)
                invokeList.add(chatList[position])

                onItemLongClick?.invoke(invokeList)
                draw(holder, position)
                notifyItemChanged(position)  // Important!
            }

        }

    }

    private fun draw(holder: RecyclerView.ViewHolder, position: Int) {

        Log.d("bur", "toggleSelection call: " + position)
        if (selectedItems.contains(position)) {
            Log.d(
                "TAG",
                "onBindViewHolder: " + position + selectedItems.size + selectedItems.toString()
            )
            holder.itemView.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context, R.color.signInColor
                )
            )


        } else {
            Log.d("TAG", "onBindViewHolder2: " + selectedItems.size + selectedItems.toString())
            holder.itemView.background = null
        }

    }

    fun exitSelectionMode() {
        // Clear selection and exit selection mode
        isSelectionModeEnabled = false
        selectedItems.clear()

    }

    fun removeData(selectedMessageList: MutableList<LiveChatResponseModel.Data>) {

        for (message in selectedMessageList) {

            val position = chatList.indexOf(message)
            if (position != -1) {
                // Remove message from dataset
                chatList.removeAt(position)
                // Notify adapter of dataset change
                notifyItemRemoved(position)
            }


        }
        selectedMessageList.clear()

    }
}