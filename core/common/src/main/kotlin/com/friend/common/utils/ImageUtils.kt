package com.friend.common.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

object ImageUtils {
    fun createImageUri(context: Context): Uri {
        val dir = File(context.cacheDir, "images").apply { mkdirs() }
        val file = File(dir, "app_name_${System.currentTimeMillis()}.jpg")
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }

    fun uriToBitmap(context: Context, uri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                decoder.isMutableRequired = true
            }
        } else {
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }
    }

    fun Uri.convertToFile(context: Context): File? {
        return try {
            val fileName = queryFileName(context) ?: "friendfin_${System.currentTimeMillis()}"
            val ext = guessExtension(context) ?: ".png"
            val outFile = File(context.cacheDir, "$fileName$ext")

            context.contentResolver.openInputStream(this)?.use { input ->
                FileOutputStream(outFile).use { output ->
                    input.copyTo(output)
                }
            } ?: return null

            outFile
        } catch (e: Exception) {
            null
        }
    }

    private fun Uri.queryFileName(context: Context): String? {
        val projection = arrayOf(android.provider.OpenableColumns.DISPLAY_NAME)
        context.contentResolver.query(this, projection, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1 && cursor.moveToFirst()) return cursor.getString(nameIndex)
        }
        return null
    }

    private fun Uri.guessExtension(context: Context): String? {
        val type = context.contentResolver.getType(this) ?: return null
        val ext = android.webkit.MimeTypeMap.getSingleton().getExtensionFromMimeType(type)
        return ext?.let { ".$it" }
    }
}