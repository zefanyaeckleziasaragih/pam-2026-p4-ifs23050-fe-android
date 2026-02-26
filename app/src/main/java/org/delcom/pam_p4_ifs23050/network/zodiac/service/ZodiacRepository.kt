package org.delcom.pam_p4_ifs23050.network.zodiac.service

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.delcom.pam_p4_ifs23050.helper.SuspendHelper
import org.delcom.pam_p4_ifs23050.network.data.ResponseMessage
import org.delcom.pam_p4_ifs23050.network.zodiac.data.ResponseZodiac
import org.delcom.pam_p4_ifs23050.network.zodiac.data.ResponseZodiacAdd
import org.delcom.pam_p4_ifs23050.network.zodiac.data.ResponseZodiacs

class ZodiacRepository(
    private val api: ZodiacApiService,
) : IZodiacRepository {

    private val gson = Gson()

    private inline fun <reified T> reparse(data: Any?): T? {
        if (data == null) return null
        return try {
            val json = gson.toJson(data)
            gson.fromJson(json, object : TypeToken<T>() {}.type)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getAllZodiacs(search: String?): ResponseMessage<ResponseZodiacs?> =
        SuspendHelper.safeApiCall {
            val raw = api.getAllZodiacs(search)
            val zodiacs = reparse<ResponseZodiacs>(raw.data)
            ResponseMessage(
                status  = raw.status,
                message = raw.message,
                data    = zodiacs
            )
        }

    override suspend fun postZodiac(
        namaUmum   : RequestBody,
        namaLatin  : RequestBody,
        makna      : RequestBody,
        asalBudaya : RequestBody,
        deskripsi  : RequestBody,
        file       : MultipartBody.Part,
    ): ResponseMessage<ResponseZodiacAdd?> =
        SuspendHelper.safeApiCall {
            val raw = api.postZodiac(namaUmum, namaLatin, makna, asalBudaya, deskripsi, file)
            val added = reparse<ResponseZodiacAdd>(raw.data)
            ResponseMessage(
                status  = raw.status,
                message = raw.message,
                data    = added
            )
        }

    override suspend fun getZodiacById(zodiacId: String): ResponseMessage<ResponseZodiac?> =
        SuspendHelper.safeApiCall {
            val raw = api.getZodiacById(zodiacId)
            val zodiac = reparse<ResponseZodiac>(raw.data)
            ResponseMessage(
                status  = raw.status,
                message = raw.message,
                data    = zodiac
            )
        }

    override suspend fun putZodiac(
        zodiacId   : String,
        namaUmum   : RequestBody,
        namaLatin  : RequestBody,
        makna      : RequestBody,
        asalBudaya : RequestBody,
        deskripsi  : RequestBody,
        file       : MultipartBody.Part?,
    ): ResponseMessage<String?> =
        SuspendHelper.safeApiCall {
            api.putZodiac(zodiacId, namaUmum, namaLatin, makna, asalBudaya, deskripsi, file)
        }

    override suspend fun deleteZodiac(zodiacId: String): ResponseMessage<String?> =
        SuspendHelper.safeApiCall { api.deleteZodiac(zodiacId) }
}