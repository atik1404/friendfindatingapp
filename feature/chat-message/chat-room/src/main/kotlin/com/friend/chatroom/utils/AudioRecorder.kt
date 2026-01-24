package com.friend.chatroom.utils

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import java.io.File

class AudioRecorder {
    private var recorder: MediaRecorder? = null
    private var currentFile: File? = null

    fun start(context: Context): File {
        // App-specific storage (no extra storage permission needed)
        val dir = context.getExternalFilesDir(null) ?: context.filesDir
        val file = File(dir, "rec_${System.currentTimeMillis()}.m4a")
        currentFile = file

        val r = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            @Suppress("DEPRECATION")
            MediaRecorder()
        }

        r.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setAudioEncodingBitRate(128_000)
            setAudioSamplingRate(44_100)
            setOutputFile(file.absolutePath)

            prepare()
            start()
        }

        recorder = r
        return file
    }

    fun stop(): File? {
        val file = currentFile
        val r = recorder ?: return file

        try {
            r.stop()
        } catch (_: RuntimeException) {
            // If stop() fails (e.g., too short), file may be corrupt -> delete it
            file?.delete()
        } finally {
            r.release()
            recorder = null
            currentFile = null
        }

        return file
    }

    fun release() {
        recorder?.release()
        recorder = null
        currentFile = null
    }
}
