package com.tb.nextstop.network

import com.tb.nextstop.data.StopsResponse
import com.tb.nextstop.ui.WPG_LAT
import com.tb.nextstop.ui.WPG_LON
import retrofit2.http.GET
import retrofit2.http.Query


interface WPTApiService {
    @GET("stops.json")
    suspend fun getNearbyStops(
        @Query("distance") dist: Int = 500,
        @Query("lat") latitude: Double = WPG_LAT,
        @Query("lon") longitude: Double = WPG_LON
    ): StopsResponse
}