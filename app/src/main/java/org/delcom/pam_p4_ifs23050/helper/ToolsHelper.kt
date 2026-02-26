package org.delcom.pam_p4_ifs23051.helper

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.delcom.pam_p4_ifs23051.BuildConfig
import java.io.File

object ToolsHelper {

    fun getPlantImageUrl(plantId: String): String =
        "${BuildConfig.BASE_URL_PANTS_API}plants/$plantId/image"

    fun getProfilePhotoUrl(): String =
        "${BuildConfig.BASE_URL_PANTS_API}profile/photo"

    /** URL gambar bahasa bunga — dari backend ifs23051 */
    fun getFlowerImageUrl(flowerId: String): String =
        "${BuildConfig.BASE_URL_FLOWER_API}flowers/$flowerId/image"

    fun String.toRequestBodyText(): RequestBody =
        this.toRequestBody("text/plain".toMediaTypeOrNull())

    fun uriToMultipart(context: Context, uri: Uri, partName: String): MultipartBody.Part {
        val file = uriToFile(context, uri)
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }

    fun uriToFile(context: Context, uri: Uri): File {
        val file = File.createTempFile("upload", ".tmp", context.cacheDir)
        context.contentResolver.openInputStream(uri)?.use { input ->
            file.outputStream().use { output -> input.copyTo(output) }
        }
        return file
    }
}