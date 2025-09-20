import android.content.Context
import android.graphics.*
import androidx.exifinterface.media.ExifInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.math.min
import kotlin.math.roundToInt

suspend fun File.reduceImageSize(
    context: Context,
    maxWidth: Int = 1280,
    maxHeight: Int = 1280,
    targetBytes: Long = 500_000,     // ~500 KB target
    minQuality: Int = 60,            // won't go below this
    preferJpeg: Boolean = true       // set false to keep alpha via WEBP on API 30+
): File = withContext(Dispatchers.IO) {
    // 1) Bounds pass
    val bounds = android.graphics.BitmapFactory.Options().apply { inJustDecodeBounds = true }
    BitmapFactory.decodeFile(absolutePath, bounds)

    // 2) Compute inSampleSize (power of 2) to roughly fit max bounds
    val inSample = calculateInSampleSize(bounds.outWidth, bounds.outHeight, maxWidth, maxHeight)

    // 3) Decode with sampling
    val opts = BitmapFactory.Options().apply {
        inJustDecodeBounds = false
        inSampleSize = inSample
        inPreferredConfig = Bitmap.Config.ARGB_8888
    }
    var bmp = BitmapFactory.decodeFile(absolutePath, opts)
        ?: error("Failed to decode bitmap: $this")

    // 4) Exact fit inside maxWidth x maxHeight
    val ratio = min(maxWidth.toFloat() / bmp.width, maxHeight.toFloat() / bmp.height)
    if (ratio < 1f) {
        bmp = Bitmap.createScaledBitmap(
            bmp,
            (bmp.width * ratio).roundToInt(),
            (bmp.height * ratio).roundToInt(),
            true
        )
    }

    // 5) Fix rotation from EXIF
    bmp = rotateIfRequired(bmp, this@reduceImageSize)

    // 6) Pick format. If source has alpha and you *must* preserve it, avoid JPEG.
    val hasAlpha = bmp.hasAlpha()
    val format = when {
        preferJpeg && !hasAlpha -> Bitmap.CompressFormat.JPEG
        // WEBP works on all modern devices. Lossy preserves alpha from API 30+.
        else -> Bitmap.CompressFormat.WEBP
    }

    // If saving as JPEG but image has alpha, flatten onto white to avoid black bg
    val toSave = if (format == Bitmap.CompressFormat.JPEG && hasAlpha) {
        val flat = Bitmap.createBitmap(bmp.width, bmp.height, Bitmap.Config.ARGB_8888)
        Canvas(flat).apply {
            drawColor(Color.WHITE)
            drawBitmap(bmp, 0f, 0f, null)
        }
        flat
    } else bmp

    // 7) Iteratively compress to hit targetBytes
    var quality = 92
    var bytes: ByteArray
    do {
        val bos = ByteArrayOutputStream()
        toSave.compress(format, quality, bos)
        bytes = bos.toByteArray()
        quality -= 6
    } while (bytes.size > targetBytes && quality >= minQuality)

    // 8) Write out
    val out = File(context.cacheDir, "${nameWithoutExtension}_compressed.${if (format == Bitmap.CompressFormat.JPEG) "jpg" else "webp"}")
    out.outputStream().use { it.write(bytes) }
    out
}

private fun calculateInSampleSize(srcW: Int, srcH: Int, reqW: Int, reqH: Int): Int {
    var inSample = 1
    if (srcH > reqH || srcW > reqW) {
        var halfH = srcH / 2
        var halfW = srcW / 2
        while ((halfH / inSample) >= reqH && (halfW / inSample) >= reqW) {
            inSample *= 2
        }
    }
    return inSample
}

private fun rotateIfRequired(bitmap: Bitmap, file: File): Bitmap {
    return try {
        val exif = ExifInterface(file.absolutePath)
        val rotation = when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
        if (rotation == 0) bitmap
        else {
            val m = Matrix().apply { postRotate(rotation.toFloat()) }
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, m, true)
        }
    } catch (_: Exception) {
        bitmap
    }
}
