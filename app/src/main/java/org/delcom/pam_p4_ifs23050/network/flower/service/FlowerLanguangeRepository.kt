package org.delcom.pam_p4_ifs23051.network.flower.service

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.delcom.pam_p4_ifs23051.helper.SuspendHelper
import org.delcom.pam_p4_ifs23051.network.data.ResponseMessage
import org.delcom.pam_p4_ifs23051.network.flower.data.ResponseFlowerLanguage
import org.delcom.pam_p4_ifs23051.network.flower.data.ResponseFlowerLanguageAdd
import org.delcom.pam_p4_ifs23051.network.flower.data.ResponseFlowerLanguages

class FlowerLanguageRepository(
    private val api: FlowerLanguageApiService,
) : IFlowerLanguageRepository {

    private val gson = Gson()

    /**
     * Fix: Gson kadang gagal deserialize generic type T secara langsung.
     * Solusinya: re-serialize data ke JSON string, lalu parse ulang
     * ke tipe yang kita inginkan secara eksplisit.
     */
    private inline fun <reified T> reparse(data: Any?): T? {
        if (data == null) return null
        return try {
            val json = gson.toJson(data)
            gson.fromJson(json, object : TypeToken<T>() {}.type)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getAllFlowers(search: String?): ResponseMessage<ResponseFlowerLanguages?> =
        SuspendHelper.safeApiCall {
            val raw = api.getAllFlowers(search)
            val flowers = reparse<ResponseFlowerLanguages>(raw.data)
            ResponseMessage(
                status  = raw.status,
                message = raw.message,
                data    = flowers
            )
        }

    override suspend fun postFlower(
        namaUmum   : RequestBody,
        namaLatin  : RequestBody,
        makna      : RequestBody,
        asalBudaya : RequestBody,
        deskripsi  : RequestBody,
        file       : MultipartBody.Part,
    ): ResponseMessage<ResponseFlowerLanguageAdd?> =
        SuspendHelper.safeApiCall {
            val raw = api.postFlower(namaUmum, namaLatin, makna, asalBudaya, deskripsi, file)
            val added = reparse<ResponseFlowerLanguageAdd>(raw.data)
            ResponseMessage(
                status  = raw.status,
                message = raw.message,
                data    = added
            )
        }

    override suspend fun getFlowerById(flowerId: String): ResponseMessage<ResponseFlowerLanguage?> =
        SuspendHelper.safeApiCall {
            val raw = api.getFlowerById(flowerId)
            val flower = reparse<ResponseFlowerLanguage>(raw.data)
            ResponseMessage(
                status  = raw.status,
                message = raw.message,
                data    = flower
            )
        }

    override suspend fun putFlower(
        flowerId   : String,
        namaUmum   : RequestBody,
        namaLatin  : RequestBody,
        makna      : RequestBody,
        asalBudaya : RequestBody,
        deskripsi  : RequestBody,
        file       : MultipartBody.Part?,
    ): ResponseMessage<String?> =
        SuspendHelper.safeApiCall {
            api.putFlower(flowerId, namaUmum, namaLatin, makna, asalBudaya, deskripsi, file)
        }

    override suspend fun deleteFlower(flowerId: String): ResponseMessage<String?> =
        SuspendHelper.safeApiCall { api.deleteFlower(flowerId) }
}