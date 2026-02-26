package org.delcom.pam_p4_ifs23051.network.plants.service

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.delcom.pam_p4_ifs23051.helper.SuspendHelper
import org.delcom.pam_p4_ifs23051.network.data.ResponseMessage
import org.delcom.pam_p4_ifs23051.network.plants.data.ResponsePlant
import org.delcom.pam_p4_ifs23051.network.plants.data.ResponsePlantAdd
import org.delcom.pam_p4_ifs23051.network.plants.data.ResponsePlants
import org.delcom.pam_p4_ifs23051.network.plants.data.ResponseProfile

class PlantRepository(private val plantApiService: PlantApiService) : IPlantRepository {

    private val gson = Gson()
    private val TAG = "PlantRepository"

    private inline fun <reified T> reparse(data: Any?): T? {
        if (data == null) return null
        return try {
            val json = gson.toJson(data)
            gson.fromJson(json, object : TypeToken<T>() {}.type)
        } catch (e: Exception) {
            Log.e(TAG, "reparse error: ${e.message}")
            null
        }
    }

    override suspend fun getProfile(): ResponseMessage<ResponseProfile?> {
        return SuspendHelper.safeApiCall {
            // ── DEBUG: cetak raw response dulu ──────────────────────────────
            try {
                val rawBody = plantApiService.getProfileRaw()
                val rawString = rawBody.string()
                Log.d(TAG, "=== RAW PROFILE RESPONSE ===")
                Log.d(TAG, rawString)
                Log.d(TAG, "============================")
            } catch (e: Exception) {
                Log.e(TAG, "Raw fetch error: ${e.message}")
            }
            // ────────────────────────────────────────────────────────────────

            val raw = plantApiService.getProfile()

            Log.d(TAG, "status  = ${raw.status}")
            Log.d(TAG, "message = ${raw.message}")
            Log.d(TAG, "data    = ${raw.data}")
            Log.d(TAG, "data class = ${raw.data?.javaClass?.name}")

            val profile = reparse<ResponseProfile>(raw.data)

            Log.d(TAG, "profile after reparse = $profile")

            ResponseMessage(
                status  = raw.status,
                message = raw.message,
                data    = profile
            )
        }
    }

    override suspend fun getAllPlants(search: String?): ResponseMessage<ResponsePlants?> {
        return SuspendHelper.safeApiCall {
            val raw = plantApiService.getAllPlants(search)
            val plants = reparse<ResponsePlants>(raw.data)
            ResponseMessage(
                status  = raw.status,
                message = raw.message,
                data    = plants
            )
        }
    }

    override suspend fun postPlant(
        nama: RequestBody,
        deskripsi: RequestBody,
        manfaat: RequestBody,
        efekSamping: RequestBody,
        file: MultipartBody.Part
    ): ResponseMessage<ResponsePlantAdd?> {
        return SuspendHelper.safeApiCall {
            val raw = plantApiService.postPlant(
                nama        = nama,
                deskripsi   = deskripsi,
                manfaat     = manfaat,
                efekSamping = efekSamping,
                file        = file
            )
            val plantAdd = reparse<ResponsePlantAdd>(raw.data)
            ResponseMessage(
                status  = raw.status,
                message = raw.message,
                data    = plantAdd
            )
        }
    }

    override suspend fun getPlantById(plantId: String): ResponseMessage<ResponsePlant?> {
        return SuspendHelper.safeApiCall {
            val raw = plantApiService.getPlantById(plantId)
            val plant = reparse<ResponsePlant>(raw.data)
            ResponseMessage(
                status  = raw.status,
                message = raw.message,
                data    = plant
            )
        }
    }

    override suspend fun putPlant(
        plantId: String,
        nama: RequestBody,
        deskripsi: RequestBody,
        manfaat: RequestBody,
        efekSamping: RequestBody,
        file: MultipartBody.Part?
    ): ResponseMessage<String?> {
        return SuspendHelper.safeApiCall {
            plantApiService.putPlant(
                plantId     = plantId,
                nama        = nama,
                deskripsi   = deskripsi,
                manfaat     = manfaat,
                efekSamping = efekSamping,
                file        = file
            )
        }
    }

    override suspend fun deletePlant(plantId: String): ResponseMessage<String?> {
        return SuspendHelper.safeApiCall {
            plantApiService.deletePlant(plantId)
        }
    }
}