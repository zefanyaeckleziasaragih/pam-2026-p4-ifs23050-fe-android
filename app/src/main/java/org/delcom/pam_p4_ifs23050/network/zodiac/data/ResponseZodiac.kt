package org.delcom.pam_p4_ifs23050.network.zodiac.data

import kotlinx.serialization.Serializable

@Serializable
data class ResponseZodiacs(
    val flowers: List<ResponseZodiacData>
)

@Serializable
data class ResponseZodiac(
    val flower: ResponseZodiacData
)

@Serializable
data class ResponseZodiacAdd(
    val flowerId: String
)

@Serializable
data class ResponseZodiacData(
    val id: String,
    val namaUmum: String,
    val namaLatin: String,
    val makna: String,
    val asalBudaya: String,
    val deskripsi: String,
    val createdAt: String,
    val updatedAt: String,
)