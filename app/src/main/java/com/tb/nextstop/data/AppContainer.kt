package com.tb.nextstop.data

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.tb.nextstop.BuildConfig
import com.tb.nextstop.network.WPTApiV2Service
import com.tb.nextstop.network.WPTApiV3Service
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

interface AppContainer {
    val wptRepository: WPTRepository
    val locationRepository: LocationRepository
}

class DefaultAppContainer(private val context: Context): AppContainer {
    private val apiV3BaseUrl = "https://api.winnipegtransit.com/v3/"
    private val apiV2BaseUrl = "https://winnipegtransit.com/api/v2/"

    private val apiKeyInterceptor = Interceptor { chain ->
        val originalRequest: Request = chain.request()
        val newUrl = originalRequest.url.newBuilder()
            .addQueryParameter("api-key", BuildConfig.API_KEY)
            .build()
        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()
        chain.proceed(newRequest)
    }

    private val apiV3loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val apiV2loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val apiV3oKHttpClient = OkHttpClient.Builder()
        .addInterceptor(apiKeyInterceptor)
        .addInterceptor(apiV3loggingInterceptor)
        .build()

    private val apiV2okHttpClient = OkHttpClient.Builder()
        .addInterceptor(apiV2loggingInterceptor)
        .build()

    private val apiV3json = Json { ignoreUnknownKeys = true }

    private val apiV2json = Json { ignoreUnknownKeys = true }

    private val apiV3Retrofit = Retrofit.Builder()
        .baseUrl(apiV3BaseUrl)
        .client(apiV3oKHttpClient)
        .addConverterFactory(apiV3json.asConverterFactory("application/json".toMediaType()))
        .build()

    private val apiV2Retrofit = Retrofit.Builder()
        .baseUrl(apiV2BaseUrl)
        .client(apiV2okHttpClient)
        .addConverterFactory(apiV2json.asConverterFactory("application/json".toMediaType()))
        .build()

    private val apiV3RetrofitService: WPTApiV3Service by lazy {
        apiV3Retrofit.create(WPTApiV3Service::class.java)
    }

    private val apiV2RetrofitService: WPTApiV2Service by lazy {
        apiV2Retrofit.create(WPTApiV2Service::class.java)
    }

    override val wptRepository: WPTRepository by lazy {
        NetworkWPTRepository(
            wptApiV3Service = apiV3RetrofitService,
            wptApiV2Service = apiV2RetrofitService,
            stopDao = StopsDatabase.getDatabase(context).stopDao()
        )
    }

    override val locationRepository: LocationRepository by lazy {
        LocalLocationRepository(context)
    }
}
