package com.friendfinapp.dating.helper

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class NetworkCheckWorker( appContext: Context, params: WorkerParameters) : Worker(appContext, params) {
    override fun doWork(): Result {
        val cm = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting

        if (!isConnected) {
            // Show "No Internet" message or take appropriate action
            // You can use NotificationManager to display a notification
            // or communicate with your activity.
            // For simplicity, we'll use Log.d for demonstration purposes.
            Log.d("TAG", "doWork: no internet")
        }else{
            Log.d("TAG", "doWork:  internet cunnected")
        }

        // Indicate that the work is complete
        return Result.success()
    }
}