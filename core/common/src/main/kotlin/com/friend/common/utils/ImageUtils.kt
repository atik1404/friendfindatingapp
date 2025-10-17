package com.friend.common.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import kotlin.math.roundToInt
import androidx.core.graphics.scale

object ImageUtils {
    suspend fun resizeBitmapImage(imageBitMap: Bitmap?, maxImageSize: Float, filter: Boolean): Bitmap? {
        return withContext(Dispatchers.IO){
            imageBitMap?.let {
                val ratio: Float = (maxImageSize / imageBitMap.width).coerceAtMost(maxImageSize / imageBitMap.height)
                val width = (ratio * imageBitMap.width).roundToInt()
                val height = (ratio * imageBitMap.height).roundToInt()
                imageBitMap.scale(width, height, filter)
            }
        }
    }

    fun resizeImage(imageBitMap: Bitmap) : Bitmap?{
        val baos = ByteArrayOutputStream()
        imageBitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        var options = 100
        while (baos.toByteArray().size > 1024 * 50) {
            baos.reset()
            imageBitMap.compress(Bitmap.CompressFormat.JPEG, options, baos)
            options -= 10
        }
        val isBm = ByteArrayInputStream(baos.toByteArray())
        return BitmapFactory.decodeStream(isBm, null, null)
    }

    fun getImageSize(bitmap : Bitmap?) : Int{
        val baos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        return  1024
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun uriToBitmap(imageUri: Uri?, context:Context):Bitmap?{
        return withContext(Dispatchers.IO){
            imageUri?.let {
                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, imageUri))
                } else {
                    MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
                }
                bitmap
            }
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun bitmapToFile(bitmap: Bitmap?, context: Context, fileName: String): File?{
        return withContext(Dispatchers.IO){
            bitmap?.let {
                val filesDir: File = context.filesDir
                val imageFile = File(filesDir, "$fileName.png")
                val os: OutputStream
                try {
                    os = FileOutputStream(imageFile)
                    bitmap.compress(Bitmap.CompressFormat.PNG, 80, os)
                    os.flush()
                    os.close()
                } catch (e: Exception) {
                    //Timber.e(e.localizedMessage)
                }
                //Timber.e(imageFile.absolutePath)
                imageFile
            }
        }
    }


    fun fileSize(file: File?){
        file?.let {
            val size: Int = java.lang.String.valueOf(file.length() / 1024).toInt()
            //Timber.e(size.toString())
        }
    }

    fun bitmapToBase64Converter(bitmap : Bitmap?) : String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap?.let {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            return "data:image/jpeg;base64,${Base64.encodeToString(byteArray, Base64.DEFAULT)}"
        }
        return ""
    }
}