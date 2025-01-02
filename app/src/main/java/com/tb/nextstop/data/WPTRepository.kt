package com.tb.nextstop.data

import com.tb.nextstop.network.WPTApiService

interface WPTRepository {
    suspend fun getNearbyStops(): StopsResponse
    suspend fun getStopFeatures(stopId: Int): StopFeaturesResponse
    suspend fun getStopSchedules(stopId: Int): StopSchedulesResponse
}

class NetworkWPTRepository(
    private val wptApiService: WPTApiService,
    private val stopDao: StopDao
) : WPTRepository {
    override suspend fun getNearbyStops(): StopsResponse = wptApiService.getNearbyStops()

    override suspend fun getStopFeatures(stopId: Int): StopFeaturesResponse =
        wptApiService.getStopFeatures(stopId)

    override suspend fun getStopSchedules(stopId: Int): StopSchedulesResponse =
        wptApiService.getStopSchedules(stopId)
}