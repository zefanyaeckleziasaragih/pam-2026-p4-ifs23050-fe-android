package org.delcom.pam_p4_ifs23051.network.flower.service

import org.delcom.pam_p4_ifs23051.BuildConfig
import org.delcom.pam_p4_ifs23051.helper.UnsafeOkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FlowerLanguageAppContainer : IFlowerLanguageAppContainer {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL_FLOWER_API)
        .addConverterFactory(GsonConverterFactory.create())
        .client(UnsafeOkHttpClient.build())
        .build()

    private val retrofitService: FlowerLanguageApiService by lazy {
        retrofit.create(FlowerLanguageApiService::class.java)
    }

    override val flowerLanguageRepository: IFlowerLanguageRepository by lazy {
        FlowerLanguageRepository(retrofitService)
    }
}