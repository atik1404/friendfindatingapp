package com.friend.data.util

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

object MultiPartConverter {
    fun mConverter(data: String): RequestBody =
        data.toRequestBody(MultipartBody.FORM)

    fun mConverterNullable(data: String?): RequestBody? =
        data?.toRequestBody(MultipartBody.FORM)

    fun mConvertImage(file: File?, fileName: String): MultipartBody.Part? {
        val body = file?.asRequestBody("image/*".toMediaTypeOrNull())
        return body?.let { MultipartBody.Part.createFormData(fileName, file.name, it) }
    }

    fun convertVideo(file: File?, formKey: String): MultipartBody.Part? {
        if (file == null) return null
        val body = file.asRequestBody("video/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(formKey, file.name, body)
    }

    fun convertAudio(file: File?, formKey: String): MultipartBody.Part? {
        if (file == null) return null
        val body = file.asRequestBody("audio/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(formKey, file.name, body)
    }
}