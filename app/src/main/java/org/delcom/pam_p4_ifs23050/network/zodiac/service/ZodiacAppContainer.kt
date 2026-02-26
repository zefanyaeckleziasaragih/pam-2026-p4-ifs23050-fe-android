package org.delcom.pam_p4_ifs23050.network.zodiac.service

import org.delcom.pam_p4_ifs23050.BuildConfig
import org.delcom.pam_p4_ifs23050.helper.UnsafeOkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ZodiacAppContainer : IZodiacAppContainer {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL_ZODIAC_API)
        .addConverterFactory(GsonConverterFactory.create())
        .client(UnsafeOkHttpClient.build())
        .build()

    private val retrofitService: ZodiacApiService by lazy {
        retrofit.create(ZodiacApiService::class.java)
    }

    override val zodiacRepository: IZodiacRepository by lazy {
        ZodiacRepository(retrofitService)
    }
}