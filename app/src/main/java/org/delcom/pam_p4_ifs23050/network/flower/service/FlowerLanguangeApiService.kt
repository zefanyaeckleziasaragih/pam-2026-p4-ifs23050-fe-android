package org.delcom.pam_p4_ifs23051.network.flower.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.delcom.pam_p4_ifs23051.network.data.ResponseMessage
import org.delcom.pam_p4_ifs23051.network.flower.data.ResponseFlowerLanguage
import org.delcom.pam_p4_ifs23051.network.flower.data.ResponseFlowerLanguageAdd
import org.delcom.pam_p4_ifs23051.network.flower.data.ResponseFlowerLanguages
import retrofit2.http.*

interface FlowerLanguageApiService {

    /** GET /flowers?search=... */
    @GET("flowers")
    suspend fun getAllFlowers(
        @Query("search") search: String? = null,
    ): ResponseMessage<ResponseFlowerLanguages?>

    /** POST /flowers  (multipart) */
    @Multipart
    @POST("flowers")
    suspend fun postFlower(
        @Part("namaUmum")   namaUmum   : RequestBody,
        @Part("namaLatin")  namaLatin  : RequestBody,
        @Part("makna")      makna      : RequestBody,
        @Part("asalBudaya") asalBudaya : RequestBody,
        @Part("deskripsi")  deskripsi  : RequestBody,
        @Part            file       : MultipartBody.Part,
    ): ResponseMessage<ResponseFlowerLanguageAdd?>

    /** GET /flowers/{flowerId} */
    @GET("flowers/{flowerId}")
    suspend fun getFlowerById(
        @Path("flowerId") flowerId: String,
    ): ResponseMessage<ResponseFlowerLanguage?>

    /** PUT /flowers/{flowerId}  (multipart) */
    @Multipart
    @PUT("flowers/{flowerId}")
    suspend fun putFlower(
        @Path("flowerId")   flowerId   : String,
        @Part("namaUmum")   namaUmum   : RequestBody,
        @Part("namaLatin")  namaLatin  : RequestBody,
        @Part("makna")      makna      : RequestBody,
        @Part("asalBudaya") asalBudaya : RequestBody,
        @Part("deskripsi")  deskripsi  : RequestBody,
        @Part            file       : MultipartBody.Part? = null,
    ): ResponseMessage<String?>

    /** DELETE /flowers/{flowerId} */
    @DELETE("flowers/{flowerId}")
    suspend fun deleteFlower(
        @Path("flowerId") flowerId: String,
    ): ResponseMessage<String?>
}