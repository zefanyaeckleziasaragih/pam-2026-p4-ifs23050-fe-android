package org.delcom.pam_p4_ifs23051.network.flower.data

import kotlinx.serialization.Serializable

@Serializable
data class ResponseFlowerLanguages(
    val flowers: List<ResponseFlowerLanguageData>
)

@Serializable
data class ResponseFlowerLanguage(
    val flower: ResponseFlowerLanguageData
)

@Serializable
data class ResponseFlowerLanguageAdd(
    val flowerId: String
)

@Serializable
data class ResponseFlowerLanguageData(
    val id: String,
    val namaUmum: String,
    val namaLatin: String,
    val makna: String,
    val asalBudaya: String,
    val deskripsi: String,
    val createdAt: String,
    val updatedAt: String,
)