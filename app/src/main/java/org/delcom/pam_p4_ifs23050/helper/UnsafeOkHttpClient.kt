package org.delcom.pam_p4_ifs23050.helper

import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.delcom.pam_p4_ifs23050.BuildConfig
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * OkHttpClient yang menonaktifkan validasi SSL.
 * HANYA untuk development / self-signed certificate.
 * JANGAN digunakan di production!
 */
object UnsafeOkHttpClient {

    // Expose trustAllCerts dan sslContext agar bisa dipakai Coil juga
    val trustAllCerts: Array<TrustManager> = arrayOf(
        object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
        }
    )

    val sslContext: SSLContext = SSLContext.getInstance("SSL").apply {
        init(null, trustAllCerts, SecureRandom())
    }

    /**
     * Connection pool: reuse koneksi TCP ke server yang sama.
     * Tanpa ini setiap request buka koneksi baru → overhead TLS handshake tiap kali.
     */
    private val sharedConnectionPool = ConnectionPool(
        maxIdleConnections = 5,
        keepAliveDuration  = 30L,
        timeUnit           = TimeUnit.SECONDS,
    )

    fun build(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BASIC
            else HttpLoggingInterceptor.Level.NONE
        }

        return OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier { _, _ -> true }
            // Reuse koneksi → hemat TLS handshake
            .connectionPool(sharedConnectionPool)
            // Timeout lebih ketat agar error cepat ketahuan
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)  // upload butuh lebih lama, tapi tidak perlu 60s jika gambar sudah dikompres
            .retryOnConnectionFailure(true)
            .apply {
                if (BuildConfig.DEBUG) addInterceptor(loggingInterceptor)
            }
            .build()
    }
}