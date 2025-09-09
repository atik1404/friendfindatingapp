package com.friendfinapp.dating.ui.uploadphoto.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.friendfinapp.dating.ui.mainrepo.MainRepo
import com.friendfinapp.dating.ui.uploadphoto.responsemodel.UploadPhotoResponseModel
import java.io.File

class UploadPhotoViewModel :ViewModel() {
    fun photoUpload(
        id: String,
        username: String?,
        photoalbum: String,
        imageUri: Uri,
        file: File,
        name: String?,
        s: String,
        approved: String,
        approvedDate: String,
        primary: String,
        explicit: String,
        private: String,
        faceCrop: String,
        manualApproval: String,
        salute: String
    ):  LiveData<UploadPhotoResponseModel> = repo.photoUpload(id,username,photoalbum,imageUri,file,name,s,approved,approvedDate,primary,explicit,private,faceCrop,manualApproval,salute)

    private val repo = MainRepo()

}