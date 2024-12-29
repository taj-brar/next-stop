package com.tb.nextstop.data

import com.tb.nextstop.network.WPTApiService

interface WPTRepository {
    suspend fun getNearbyStops(): StopsResponse
}

class NetworkWPTRepository(
    private val wptApiService: WPTApiService
) : WPTRepository {
    override suspend fun getNearbyStops(): StopsResponse = wptApiService.getNearbyStops()
}