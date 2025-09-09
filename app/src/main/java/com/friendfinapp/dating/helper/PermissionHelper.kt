package com.friendfinapp.dating.helper

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import com.friendfinapp.dating.ui.chatroom.ChatRoomActivity
import com.friendfinapp.dating.ui.uploadphoto.UploadPhotoActivity

import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class PermissionHelper     // constructor of this class
(var context: Context) {
    // Using this method to check necessary permission id granted or not
    @RequiresApi(Build.VERSION_CODES.R)
    fun checkPermission(): Boolean {
        val result1 = ContextCompat.checkSelfPermission(context, Manifest.permission.MANAGE_EXTERNAL_STORAGE)
        val result2 = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES)
        val result3 = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        return result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun checkPermissionForUploadScreenshot(): Boolean {

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            //val result1 = ContextCompat.checkSelfPermission(context, Manifest.permission.MANAGE_EXTERNAL_STORAGE)
            val result2 = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES)
            //result1 == PackageManager.PERMISSION_GRANTED ||
                    result2 == PackageManager.PERMISSION_GRANTED
        }else{
            val result1 = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val result2 = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
            result1 == PackageManager.PERMISSION_GRANTED ||
                    result2 == PackageManager.PERMISSION_GRANTED
        }

    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun checkPermissionForUploadVideo(): Boolean {

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            val result1 = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_VIDEO)
            val result2 = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES)
            result1 == PackageManager.PERMISSION_GRANTED ||
            result2 == PackageManager.PERMISSION_GRANTED
        }else{
            val result1 = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val result2 = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
            result1 == PackageManager.PERMISSION_GRANTED ||
                    result2 == PackageManager.PERMISSION_GRANTED
        }

    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun checkPermissionForNotification(): Boolean {
        val result1 = ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
        return result1 == PackageManager.PERMISSION_GRANTED
    }


    fun checkPermissionForCamera(): Boolean {
        val result1 = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        return result1 == PackageManager.PERMISSION_GRANTED
    }
  fun checkPermissionForRecordAudio(): Boolean {
        val result1 = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
        return result1 == PackageManager.PERMISSION_GRANTED
    }

    // If not granted then request the permission using this method
    fun requestPermission() {
        Dexter.withContext(context)
                .withPermissions(
                        Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_MEDIA_IMAGES,
                        Manifest.permission.CAMERA)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // do you work now
//                            if (EditProfileActivity.instance != null) EditProfileActivity.instance!!.showImageImportDialog()
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied) {
                            // permission is denied permenantly, navigate user to app settings
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                })
                .onSameThread()
                .check()
    }


    fun requestPermissionNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Dexter.withContext(context)
                .withPermissions(
                    Manifest.permission.POST_NOTIFICATIONS)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // do you work now
                            try {
                           //     UploadPhotoActivity.instance!!.pickImage()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied) {

                            ChatRoomActivity.instance?.showSettingsDialog()
                            // permission is denied permenantly, navigate user to app settings
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                })
                .onSameThread()
                .check()
        }
    }

    fun requestPermissionForCamera() {

            Dexter.withContext(context)
                .withPermissions(
                    Manifest.permission.CAMERA)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // do you work now
                            try {

                                ChatRoomActivity.instance!!.pickImageCamera()
                               // UploadPhotoActivity.instance!!.pickImage()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied) {
                            // permission is denied permenantly, navigate user to app settings
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                })
                .onSameThread()
                .check()

    }


    fun requestPermissionForAudio() {

        Dexter.withContext(context)
            .withPermissions(
                Manifest.permission.RECORD_AUDIO)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    // check if all permissions are granted
                    if (report.areAllPermissionsGranted()) {
                        // do you work now
                        try {

                          //  ChatRoomActivity.instance!!.pickImageCamera()
                            // UploadPhotoActivity.instance!!.pickImage()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    // check for permanent denial of any permission
                    if (report.isAnyPermissionPermanentlyDenied) {
                        // permission is denied permenantly, navigate user to app settings
                        ChatRoomActivity.instance?.showSettingsDialog()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                    token.continuePermissionRequest()
                }
            })
            .onSameThread()
            .check()

    }
    @RequiresApi(Build.VERSION_CODES.R)
    fun requestPermissionUploadScreenshot() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            Dexter.withContext(context)
                .withPermissions(
//                    Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_MEDIA_IMAGES
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // do you work now
                            try {
                                ChatRoomActivity.instance!!.pickImageGallery()
                              //  UploadPhotoActivity.instance!!.pickImage()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied) {
                            // permission is denied permenantly, navigate user to app settings
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest>,
                        token: PermissionToken
                    ) {
                        token.continuePermissionRequest()
                    }
                })
                .onSameThread()
                .check()
        }else{
            Dexter.withContext(context)
                .withPermissions(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // do you work now
                            try {
                                ChatRoomActivity.instance!!.pickImageGallery()
                             //   UploadPhotoActivity.instance!!.pickImage()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied) {
                            // permission is denied permenantly, navigate user to app settings
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest>,
                        token: PermissionToken
                    ) {
                        token.continuePermissionRequest()
                    }
                })
                .onSameThread()
                .check()
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun requestPermissionUploadVideo() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            Dexter.withContext(context)
                .withPermissions(
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_IMAGES
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // do you work now
                            try {
                                ChatRoomActivity.instance!!.pickVideosGallery()
                                //  UploadPhotoActivity.instance!!.pickImage()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied) {
                            // permission is denied permenantly, navigate user to app settings
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest>,
                        token: PermissionToken
                    ) {
                        token.continuePermissionRequest()
                    }
                })
                .onSameThread()
                .check()
        }else{
            Dexter.withContext(context)
                .withPermissions(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // do you work now
                            try {
                                ChatRoomActivity.instance!!.pickVideosGallery()
                                //   UploadPhotoActivity.instance!!.pickImage()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied) {
                            // permission is denied permenantly, navigate user to app settings
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest>,
                        token: PermissionToken
                    ) {
                        token.continuePermissionRequest()
                    }
                })
                .onSameThread()
                .check()
        }
    }


}