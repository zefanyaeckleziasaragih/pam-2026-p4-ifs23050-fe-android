package org.delcom.pam_p4_ifs23050.network.zodiac.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.delcom.pam_p4_ifs23050.network.data.ResponseMessage
import org.delcom.pam_p4_ifs23050.network.zodiac.data.ResponseZodiac
import org.delcom.pam_p4_ifs23050.network.zodiac.data.ResponseZodiacAdd
import org.delcom.pam_p4_ifs23050.network.zodiac.data.ResponseZodiacs

interface IZodiacRepository {

    suspend fun getAllZodiacs(
        search: String? = null,
    ): ResponseMessage<ResponseZodiacs?>

    suspend fun postZodiac(
        namaUmum   : RequestBody,
        namaLatin  : RequestBody,
        makna      : RequestBody,
        asalBudaya : RequestBody,
        deskripsi  : RequestBody,
        file       : MultipartBody.Part,
    ): ResponseMessage<ResponseZodiacAdd?>

    suspend fun getZodiacById(
        zodiacId: String,
    ): ResponseMessage<ResponseZodiac?>

    suspend fun putZodiac(
        zodiacId   : String,
        namaUmum   : RequestBody,
        namaLatin  : RequestBody,
        makna      : RequestBody,
        asalBudaya : RequestBody,
        deskripsi  : RequestBody,
        file       : MultipartBody.Part? = null,
    ): ResponseMessage<String?>

    suspend fun deleteZodiac(
        zodiacId: String,
    ): ResponseMessage<String?>
}