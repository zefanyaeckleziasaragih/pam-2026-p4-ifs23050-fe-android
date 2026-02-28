package org.delcom.pam_p4_ifs23050.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.delcom.pam_p4_ifs23050.BuildConfig
import java.io.File
import java.io.FileOutputStream

object ToolsHelper {

    fun getPlantImageUrl(plantId: String): String =
        "${BuildConfig.BASE_URL_PANTS_API}plants/$plantId/image"

    fun getProfilePhotoUrl(): String =
        "${BuildConfig.BASE_URL_PANTS_API}profile/photo"

    /** URL gambar zodiak */
    fun getZodiacImageUrl(zodiacId: String): String =
        "${BuildConfig.BASE_URL_ZODIAC_API}flowers/$zodiacId/image"

    fun String.toRequestBodyText(): RequestBody =
        this.toRequestBody("text/plain".toMediaTypeOrNull())

    fun uriToMultipart(context: Context, uri: Uri, partName: String): MultipartBody.Part {
        val file = uriToCompressedFile(context, uri)
        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }

    fun uriToFile(context: Context, uri: Uri): File {
        val file = File.createTempFile("upload", ".jpg", context.cacheDir)
        context.contentResolver.openInputStream(uri)?.use { input ->
            file.outputStream().use { output -> input.copyTo(output) }
        }
        return file
    }

    /**
     * Compress & resize gambar sebelum upload.
     * - Max dimensi: 1024px (lebar atau tinggi)
     * - Kualitas JPEG: 80%
     * - Hasil: file .jpg di cache dir
     *
     * Ini drastis mengurangi ukuran upload tanpa mengubah logika CRUD sama sekali.
     */
    private fun uriToCompressedFile(context: Context, uri: Uri): File {
        val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        context.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it, null, options)
        }

        val maxDimension = 1024
        var sampleSize = 1
        var w = options.outWidth
        var h = options.outHeight
        while (w > maxDimension * 2 || h > maxDimension * 2) {
            sampleSize *= 2
            w /= 2
            h /= 2
        }

        val decodeOptions = BitmapFactory.Options().apply { inSampleSize = sampleSize }
        val rawBitmap = context.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it, null, decodeOptions)
        } ?: run {
            // Fallback: kembalikan file mentah jika decode gagal
            val fallback = File.createTempFile("upload", ".jpg", context.cacheDir)
            context.contentResolver.openInputStream(uri)?.use { input ->
                fallback.outputStream().use { out -> input.copyTo(out) }
            }
            return fallback
        }

        // Scale ke max 1024px sambil pertahankan aspect ratio
        val scaled: Bitmap = if (rawBitmap.width > maxDimension || rawBitmap.height > maxDimension) {
            val ratio = rawBitmap.width.toFloat() / rawBitmap.height.toFloat()
            val (newW, newH) = if (rawBitmap.width >= rawBitmap.height) {
                maxDimension to (maxDimension / ratio).toInt()
            } else {
                (maxDimension * ratio).toInt() to maxDimension
            }
            val result = Bitmap.createScaledBitmap(rawBitmap, newW, newH, true)
            if (result !== rawBitmap) rawBitmap.recycle()
            result
        } else {
            rawBitmap
        }

        val outFile = File.createTempFile("upload_compressed", ".jpg", context.cacheDir)
        FileOutputStream(outFile).use { fos ->
            scaled.compress(Bitmap.CompressFormat.JPEG, 80, fos)
        }
        scaled.recycle()

        return outFile
    }
}