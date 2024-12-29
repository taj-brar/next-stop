package com.tb.nextstop.data

import com.tb.nextstop.network.WPTApiService

interface WPTRepository {
    suspend fun getNearbyStops(): StopsResponse
    suspend fun getStopFeatures(stopId: Int): StopFeaturesResponse
}

class NetworkWPTRepository(
    private val wptApiService: WPTApiService
) : WPTRepository {
    override suspend fun getNearbyStops(): StopsResponse = wptApiService.getNearbyStops()

    override suspend fun getStopFeatures(stopId: Int): StopFeaturesResponse =
        wptApiService.getStopFeatures(stopId)
}