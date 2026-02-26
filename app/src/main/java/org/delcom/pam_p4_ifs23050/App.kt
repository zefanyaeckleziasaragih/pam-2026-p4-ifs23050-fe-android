package org.delcom.pam_p4_ifs23051

import android.app.Application
import coil.Coil
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient
import org.delcom.pam_p4_ifs23051.helper.UnsafeOkHttpClient
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        setupCoil()
    }

    /**
     * Konfigurasi Coil agar pakai OkHttpClient yang bypass SSL.
     * Tanpa ini, Coil pakai client default dan gagal load gambar
     * dari server dengan self-signed certificate.
     */
    private fun setupCoil() {
        // Buat OkHttpClient khusus Coil (tanpa logging body agar lebih cepat)
        val coilHttpClient = OkHttpClient.Builder()
            .sslSocketFactory(
                UnsafeOkHttpClient.sslContext.socketFactory,
                UnsafeOkHttpClient.trustAllCerts[0] as javax.net.ssl.X509TrustManager
            )
            .hostnameVerifier { _, _ -> true }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        val imageLoader = ImageLoader.Builder(this)
            .okHttpClient(coilHttpClient)
            // Memory cache: simpan gambar di RAM agar tidak fetch ulang
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.25) // 25% dari memory app
                    .build()
            }
            // Disk cache: simpan gambar di storage agar tidak download ulang
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizeBytes(50L * 1024 * 1024) // 50 MB
                    .build()
            }
            .crossfade(true) // animasi fade saat gambar muncul
            .build()

        Coil.setImageLoader(imageLoader)
    }
}