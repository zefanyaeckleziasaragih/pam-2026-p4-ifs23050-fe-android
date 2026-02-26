package org.delcom.pam_p4_ifs23051.network.flower.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.delcom.pam_p4_ifs23051.network.data.ResponseMessage
import org.delcom.pam_p4_ifs23051.network.flower.data.ResponseFlowerLanguage
import org.delcom.pam_p4_ifs23051.network.flower.data.ResponseFlowerLanguageAdd
import org.delcom.pam_p4_ifs23051.network.flower.data.ResponseFlowerLanguages

interface IFlowerLanguageRepository {

    /** Ambil semua data bahasa bunga */
    suspend fun getAllFlowers(
        search: String? = null,
    ): ResponseMessage<ResponseFlowerLanguages?>

    /** Tambah data bahasa bunga baru */
    suspend fun postFlower(
        namaUmum   : RequestBody,
        namaLatin  : RequestBody,
        makna      : RequestBody,
        asalBudaya : RequestBody,
        deskripsi  : RequestBody,
        file       : MultipartBody.Part,
    ): ResponseMessage<ResponseFlowerLanguageAdd?>

    /** Ambil satu data bahasa bunga berdasarkan ID */
    suspend fun getFlowerById(
        flowerId: String,
    ): ResponseMessage<ResponseFlowerLanguage?>

    /** Ubah data bahasa bunga */
    suspend fun putFlower(
        flowerId   : String,
        namaUmum   : RequestBody,
        namaLatin  : RequestBody,
        makna      : RequestBody,
        asalBudaya : RequestBody,
        deskripsi  : RequestBody,
        file       : MultipartBody.Part? = null,
    ): ResponseMessage<String?>

    /** Hapus data bahasa bunga */
    suspend fun deleteFlower(
        flowerId: String,
    ): ResponseMessage<String?>
}