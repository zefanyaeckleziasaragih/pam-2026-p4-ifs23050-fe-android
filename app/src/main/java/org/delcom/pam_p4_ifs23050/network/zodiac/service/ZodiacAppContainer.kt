package org.delcom.pam_p4_ifs23050.network.zodiac.service

import org.delcom.pam_p4_ifs23050.BuildConfig
import org.delcom.pam_p4_ifs23050.helper.UnsafeOkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ZodiacAppContainer : IZodiacAppContainer {

    /**
     * Retrofit instance dibuat sekali (companion object) agar tidak
     * rebuild setiap kali ZodiacAppContainer di-inject ulang.
     * Ini juga memastikan OkHttpClient (dan connection pool-nya)
     * benar-benar di-share di seluruh lifecycle app.
     */
    private val retrofit: Retrofit
        get() = RetrofitHolder.instance

    private companion object RetrofitHolder {
        val instance: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL_ZODIAC_API)
                .addConverterFactory(GsonConverterFactory.create())
                .client(UnsafeOkHttpClient.build())
                .build()
        }
    }

    private val retrofitService: ZodiacApiService by lazy {
        retrofit.create(ZodiacApiService::class.java)
    }

    override val zodiacRepository: IZodiacRepository by lazy {
        ZodiacRepository(retrofitService)
    }
}