package org.delcom.pam_p4_ifs23050.network.plants.service

import org.delcom.pam_p4_ifs23050.BuildConfig
import org.delcom.pam_p4_ifs23050.helper.UnsafeOkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PlantAppContainer : IPlantAppContainer {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL_PANTS_API)
        .addConverterFactory(GsonConverterFactory.create())
        .client(UnsafeOkHttpClient.build())
        .build()

    private val retrofitService: PlantApiService by lazy {
        retrofit.create(PlantApiService::class.java)
    }

    override val plantRepository: IPlantRepository by lazy {
        PlantRepository(retrofitService)
    }
}