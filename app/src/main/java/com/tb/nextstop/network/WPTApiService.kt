package com.tb.nextstop.network

import com.tb.nextstop.data.LiveTripResponse
import com.tb.nextstop.data.StopFeaturesResponse
import com.tb.nextstop.data.StopSchedulesResponse
import com.tb.nextstop.data.StopsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WPTApiV3Service {
    @GET("stops.json")
    suspend fun getNearbyStops(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("distance") dist: Int = 500,
    ): StopsResponse

    @GET("stops/{stopId}/features.json")
    suspend fun getStopFeatures(
        @Path("stopId") stopId: Int
    ): StopFeaturesResponse

    @GET("stops/{stopId}/schedule.json")
    suspend fun getStopSchedules(
        @Path("stopId") stopId: Int
    ): StopSchedulesResponse
}

interface WPTApiV2Service {
    @GET("trips/schedule")
    suspend fun getLiveTrip(
        @Query("trip_id") tripId: Int,
        @Query("live") live: Boolean = true
    ): LiveTripResponse
}