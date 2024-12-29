package com.tb.nextstop.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.tb.nextstop.BuildConfig
import com.tb.nextstop.data.StopsResponse
import com.tb.nextstop.ui.WPG_LAT
import com.tb.nextstop.ui.WPG_LON
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.winnipegtransit.com/v3/"

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
    .baseUrl(BASE_URL)
    .client(okHttpClient)
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .build()


interface WPTApiService {
    @GET("stops.json")
    suspend fun getNearbyStops(
//        @Query("route") dist: Int = 16,
        @Query("distance") dist: Int = 500,
        @Query("lat") latitude: Double = WPG_LAT,
        @Query("lon") longitude: Double = WPG_LON
    ): StopsResponse
}

object WPTApi {
    val retrofitService: WPTApiService by lazy {
        retrofit.create(WPTApiService::class.java)
    }
}