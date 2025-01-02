package com.tb.nextstop.data

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.tb.nextstop.BuildConfig
import com.tb.nextstop.network.WPTApiService
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

interface AppContainer {
    val wptRepository: WPTRepository
}

class DefaultAppContainer(private val context: Context): AppContainer {
    private val baseUrl = "https://api.winnipegtransit.com/v3/"

    val apiKeyInterceptor = Interceptor { chain ->
        val originalRequest: Request = chain.request()
        val newUrl = originalRequest.url.newBuilder()
            .addQueryParameter("api-key", BuildConfig.API_KEY)
            .build()
        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()
        chain.proceed(newRequest)
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(apiKeyInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    private val json = Json { ignoreUnknownKeys = true }

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    private val retrofitService: WPTApiService by lazy {
        retrofit.create(WPTApiService::class.java)
    }

    override val wptRepository: WPTRepository by lazy {
        NetworkWPTRepository(
            wptApiService = retrofitService,
            stopDao = StopsDatabase.getDatabase(context).stopDao()
        )
    }
}
