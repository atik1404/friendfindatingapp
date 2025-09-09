package com.friendfinapp.dating.notification

import android.annotation.SuppressLint
import android.app.*
import android.content.ComponentName
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.friendfinapp.dating.R
import com.friendfinapp.dating.ui.chatroom.ChatRoomActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.graphics.PorterDuff

import android.graphics.PorterDuffXfermode

import android.graphics.RectF

import android.graphics.Rect

import android.graphics.Paint

import android.graphics.Color

import android.graphics.Canvas

import android.graphics.Bitmap





class ReceivedNotifications : FirebaseMessagingService() {
    private lateinit var actionActivityIntent: Intent
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if (remoteMessage.data["type"] != null && remoteMessage.data["type"] == "SMS") {
            getMessage(remoteMessage)
        }

    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)


    }

    private fun getMessage(remoteMessage: RemoteMessage) {
        val data = remoteMessage.data


        val sender = data["fromUserName"]
        val receiver = data["toUserName"]
        val icon = data["icon"]
        val title = data["title"]
        val body = data["body"]
        val type = data["type"]
        val messageType = data["messageType"]
        val senderProfile = data["toUserImage"]


        val userSender = data["senderUser"]
        val userReceiver = data["receiverUser"]



        Config.content = body!!
        Config.title = data["senderName"].toString()

        //val click_action = remoteMessage.notification!!.clickAction

        Log.d("TAG", "getMessage: "+userSender)
        Log.d("TAG", "getMessage: "+userReceiver)

        actionActivityIntent = Intent(applicationContext, ChatRoomActivity::class.java)
        actionActivityIntent.putExtra("notification", "yes")
        actionActivityIntent.putExtra("fromUserName", userSender.toString().trim())
        actionActivityIntent.putExtra("toUserName", userReceiver.toString().trim())
        actionActivityIntent.putExtra("toUserImage", senderProfile)


        var cn: ComponentName
        val am = applicationContext.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        cn = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            am.appTasks.get(0).taskInfo.topActivity!!
        } else {
            //noinspection deprecation
            am.getRunningTasks(1)[0].topActivity!!
        }

        Log.d("TAG", "getMessage: " + cn.className)
        Log.d("TAG", "getMessage: " + cn.className)

        if (cn.className != "com.friendfinapp.dating.ui.chatroom.ChatRoomActivity") {
            sendNotification(senderProfile)
        } else {
            ChatRoomActivity.instance?.getChatList2()
        }
    }

    private fun sendNotification(senderProfile: String?) {


        val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        actionActivityIntent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        val pendingIntent =
            PendingIntent.getActivity(
                applicationContext,
                0,
                actionActivityIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val NOTIFICATION_CHANNEL_ID = "com.friendfinapp.dating"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Notification",
                NotificationManager.IMPORTANCE_HIGH
            )

            //Configure Notification Channel
            notificationChannel.description = "FriendFin Notifications"
            notificationChannel.enableLights(true)
            notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }


        if (!senderProfile.isNullOrEmpty()){
            Log.d("TAG", "sendNotification: "+senderProfile)
            val futureTarget = Glide.with(this)
                .asBitmap()
                .load("https://friendfin.com/friendfinapi/"+senderProfile)
                .submit()

            val bitmap = futureTarget.get()
            val notificationBuilder: NotificationCompat.Builder =
                NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.friendfin_n)
                    .setLargeIcon(bitmap)
                    .setContentTitle(Config.title)
                    .setAutoCancel(true)
                    .setSound(defaultSound)
                    .setContentText(Config.content)
                    .setContentIntent(pendingIntent)
                    .setWhen(System.currentTimeMillis())
                    .setPriority(Notification.PRIORITY_HIGH)
            notificationManager.notify(1, notificationBuilder.build())
        }else{
            Log.d("TAG", "sendNotification: "+senderProfile)
            val futureTarget = Glide.with(this)
                .asBitmap()
                .load(R.drawable.logo)
                .submit()

            val bitmap = futureTarget.get()
            val notificationBuilder: NotificationCompat.Builder =
                NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.friendfin)
                    .setLargeIcon(bitmap)
                    .setContentTitle(Config.title)
                    .setAutoCancel(true)
                    .setSound(defaultSound)
                    .setContentText(Config.content)
                    .setContentIntent(pendingIntent)
                    .setWhen(System.currentTimeMillis())
                    .setPriority(Notification.PRIORITY_HIGH)
            notificationManager.notify(1, notificationBuilder.build())
        }

    }

    private fun getCircleBitmap(bitmap: Bitmap): Bitmap? {
        val output = Bitmap.createBitmap(
            bitmap.width,
            bitmap.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(output)
        val color = Color.RED
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)
        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawOval(rectF, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)
        bitmap.recycle()
        return output
    }

}