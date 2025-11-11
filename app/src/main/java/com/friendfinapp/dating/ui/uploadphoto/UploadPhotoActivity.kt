package com.friendfinapp.dating.ui.uploadphoto


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.friendfinapp.dating.R
import com.friendfinapp.dating.application.BaseActivity
import com.friendfinapp.dating.cropper.CropImage
import com.friendfinapp.dating.cropper.CropImageView
import com.friendfinapp.dating.databinding.ActivityUploadPhotoBinding
import com.friendfinapp.dating.helper.*
import com.friendfinapp.dating.ui.chatroom.ChatRoomActivity
import com.friendfinapp.dating.ui.uploadphoto.viewmodel.UploadPhotoViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import reduceImageSize
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class UploadPhotoActivity : BaseActivity<ActivityUploadPhotoBinding>() {

    private var permissionHelper: PermissionHelper? = null

    private lateinit var sessionManager: SessionManager

    private lateinit var viewModel: UploadPhotoViewModel

    private var uri: Uri? = null
    override fun viewBindingLayout(): ActivityUploadPhotoBinding = ActivityUploadPhotoBinding.inflate(layoutInflater)

    @RequiresApi(Build.VERSION_CODES.R)
    override fun initializeView(savedInstanceState: Bundle?) {
        instance = this

        setUpView()

        setUpClickListener()
    }

    private fun setUpView() {

        permissionHelper = PermissionHelper(this)

        sessionManager = SessionManager(this)
        viewModel = ViewModelProvider(this).get(UploadPhotoViewModel::class.java)

        if (!Constants.IS_SUBSCRIBE) {

            val adView = AdView(this)
            val adRequest = AdRequest.Builder().build()

            adView.setAdSize(AdSize.BANNER)

            adView.adUnitId = getString(R.string.BannerAdsUnitId)
            binding.adView.loadAd(adRequest)
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun setUpClickListener() {

        binding.imageBack.setOnClickListener {
            finish()
        }

        binding.addPhoto.setOnClickListener {

            if (permissionHelper!!.checkPermissionForUploadScreenshot()) {
                binding.imageUploadText.text = ""
                showChooserDialog()
            } else {
                permissionHelper!!.requestPermissionUploadScreenshot()
            }
        }

        binding.uploadImage.setOnClickListener {
            if (uri != null) {
                uploadImageToTheServer(uri)
            } else {
                Toast.makeText(this, "Image uri not found." + uri, Toast.LENGTH_SHORT).show()
            }
        }

    }

    @SuppressLint("SuspiciousIndentation")
    private fun uploadImageToTheServer(imageUri: Uri?) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                binding.progressBar.isVisible = true
                binding.uploadImage.isEnabled = false
                val username = sessionManager.username
                val ID = "1"
                val Primary = "1"
                val Explicit = "1"
                val Private = "1"
                val FaceCrop = "1"
                val ManualApproval = "1"
                val Salute = "1"
                val photoalbum = "1"
                val name = sessionManager.fullName
                val approved = "0"
                val approvedDate = "2022-03-07T10:44:19.97"
                val file = File(Objects.requireNonNull(FileUtils.getPath(this@UploadPhotoActivity, imageUri)))

                val reduceFileSize = file.reduceImageSize(this@UploadPhotoActivity)
                val image = FileUtils.getPath(this@UploadPhotoActivity, imageUri)

                viewModel.photoUpload(
                    ID,
                    username,
                    photoalbum,
                    Uri.parse(image),
                    reduceFileSize,
                    name,
                    "hello",
                    approved,
                    approvedDate,
                    Primary,
                    Explicit,
                    Private,
                    FaceCrop,
                    ManualApproval,
                    Salute
                ).observe(this@UploadPhotoActivity, {
                    if (it.statusCode == 200) {
                        binding.imageBan.setImageURI(null)
                        binding.imageUploadText.text = "Image uploaded successfully."
                    }

                    binding.progressBar.isVisible = false
                    binding.uploadImage.isEnabled = true
                })
            } catch (e: Exception) {
                 showToastMessage("Failed to upload photo, please retry")
            }
        }


    }


    //image choose dialog
    var dialog: Dialog? = null
    private fun showChooserDialog() {
        dialog = Dialog(this)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(true)
        dialog!!.setContentView(R.layout.dialog_ask_chosser)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val width: Int = (this.resources.displayMetrics.widthPixels * 0.95).toInt()
        dialog!!.window!!.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
        val close = dialog!!.findViewById(R.id.close) as ImageView
        val galley = dialog!!.findViewById(R.id.gallery) as TextView
        val camera = dialog!!.findViewById(R.id.camera) as TextView

        galley.setOnClickListener {
            dialog!!.dismiss()
            pickImageGallery()
        }

        camera.setOnClickListener {

            if (permissionHelper!!.checkPermissionForCamera()) {
                dialog!!.dismiss()
                pickImageCamera()
            } else {
                permissionHelper!!.requestPermissionForCamera()
            }

        }

        close.setOnClickListener { dialog!!.dismiss() }
        dialog!!.show()
    }

    //end


    //image choose from gallery
    fun pickImageGallery() {

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        startActivityForResult(
            Intent.createChooser(intent, "Select Picture"), ChatRoomActivity.GALLERY_REQUEST_CODE
        )
    }

    //end

    //chose image from camera
    fun pickImageCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, ChatRoomActivity.Camera_REQUEST_CODE)
    }

    //    fun pickImage() {
//        val intent = Intent(Intent.ACTION_PICK)
//        intent.type = "image/*"
//        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
//        startActivityForResult(intent, GALLERY_REQUEST_CODE)
//    }
    private var imageUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {

            when (requestCode) {
                ChatRoomActivity.GALLERY_REQUEST_CODE -> {
                    CropImage.activity(data?.data).setGuidelines(CropImageView.Guidelines.ON)
                        .start(this)
                    imageUri = data?.data
                    selectedImageUri = imageUri
                }

                ChatRoomActivity.Camera_REQUEST_CODE -> {

                    val thumbnail = data?.extras?.get("data") as Bitmap
                    selectedImageBitmap = thumbnail

                    val imageUrl = selectedImageBitmap?.let { saveBitmapToCache2(it) }
                    // binding.imageHoldSection.visibility = View.VISIBLE


                    CropImage.activity(imageUrl).setGuidelines(CropImageView.Guidelines.ON)
                        .start(this)

                }
            }
//            if (requestCode == GALLERY_REQUEST_CODE) {
//                //customDialog?.show()
//
//
//                CropImage.activity(data!!.data)
//                    .setGuidelines(CropImageView.Guidelines.ON)
//                    .start(this)
//
//
//                imageUri = data?.data
//
//            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (CropImage.getActivityResult(data) != null) {
                val result: CropImage.ActivityResult = CropImage.getActivityResult(data)

                if (resultCode == RESULT_OK) {

                    uri = result.uri
                    binding.imageBan.setImageURI(uri)


                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    val exception: Exception = result.getError()

                    Log.d("TAG", "onActivityResult: " + exception.message)

                }
            }


        }
    }

    // image capture system
   // private var currentPhotoPath: String? = null
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


//    private fun getFileName(image_uri: Uri?) {
//        try {
//            val returnCursor = contentResolver.query(image_uri!!, null, null, null, null)
//            val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
//            returnCursor.moveToFirst()
//            imageName.value = returnCursor.getString(nameIndex)
//            returnCursor.close()
//        } catch (e: Exception) {
//            showToast("Oops, something went wrong!!!")
//        }
//    }

    companion object {
        //for upload photo
        //const val GALLERY_REQUEST_CODE = 20001
        //private val REQUEST_CODE = 501
        var instance: UploadPhotoActivity? = null
    }

}