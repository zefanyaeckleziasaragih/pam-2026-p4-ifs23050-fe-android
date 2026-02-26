package org.delcom.pam_p4_ifs23050.network.zodiac.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.delcom.pam_p4_ifs23050.network.data.ResponseMessage
import org.delcom.pam_p4_ifs23050.network.zodiac.data.ResponseZodiac
import org.delcom.pam_p4_ifs23050.network.zodiac.data.ResponseZodiacAdd
import org.delcom.pam_p4_ifs23050.network.zodiac.data.ResponseZodiacs
import retrofit2.http.*

interface ZodiacApiService {

    @GET("flowers")
    suspend fun getAllZodiacs(
        @Query("search") search: String? = null,
    ): ResponseMessage<ResponseZodiacs?>

    @Multipart
    @POST("flowers")
    suspend fun postZodiac(
        @Part("namaUmum")   namaUmum   : RequestBody,
        @Part("namaLatin")  namaLatin  : RequestBody,
        @Part("makna")      makna      : RequestBody,
        @Part("asalBudaya") asalBudaya : RequestBody,
        @Part("deskripsi")  deskripsi  : RequestBody,
        @Part            file       : MultipartBody.Part,
    ): ResponseMessage<ResponseZodiacAdd?>

    @GET("flowers/{zodiacId}")
    suspend fun getZodiacById(
        @Path("zodiacId") zodiacId: String,
    ): ResponseMessage<ResponseZodiac?>

    @Multipart
    @PUT("flowers/{zodiacId}")
    suspend fun putZodiac(
        @Path("zodiacId")   zodiacId   : String,
        @Part("namaUmum")   namaUmum   : RequestBody,
        @Part("namaLatin")  namaLatin  : RequestBody,
        @Part("makna")      makna      : RequestBody,
        @Part("asalBudaya") asalBudaya : RequestBody,
        @Part("deskripsi")  deskripsi  : RequestBody,
        @Part            file       : MultipartBody.Part? = null,
    ): ResponseMessage<String?>

    @DELETE("flowers/{zodiacId}")
    suspend fun deleteZodiac(
        @Path("zodiacId") zodiacId: String,
    ): ResponseMessage<String?>
}