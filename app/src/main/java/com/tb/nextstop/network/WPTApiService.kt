package com.tb.nextstop.network

import com.tb.nextstop.BuildConfig
import com.tb.nextstop.data.Stop
import com.tb.nextstop.ui.WPG_LAT
import com.tb.nextstop.ui.WPG_LON
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
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

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(okHttpClient)
    .addConverterFactory(ScalarsConverterFactory.create())
    .build()


interface WPTApiService {
    @GET("stops.json")
    suspend fun getNearbyStops(
//        @Query("route") dist: Int = 16,
        @Query("distance") dist: Int = 100,
        @Query("lat") latitude: Double = WPG_LAT,
        @Query("lon") longitude: Double = WPG_LON
    ): String
}

object WPTApi {
    val retrofitService: WPTApiService by lazy {
        retrofit.create(WPTApiService::class.java)
    }
}