package com.tb.nextstop.data

import com.tb.nextstop.network.WPTApiService
import com.tb.nextstop.utils.toStopEntity
import com.tb.nextstop.utils.toStopFeaturesEntity

interface WPTRepository {
    suspend fun getNearbyStops(): List<Stop>
    suspend fun getStopFeatures(stopId: Int): List<StopFeature>
    suspend fun getStopSchedules(stopId: Int): StopSchedule
}

class NetworkWPTRepository(
    private val wptApiService: WPTApiService,
    private val stopDao: StopDao
) : WPTRepository {
    override suspend fun getNearbyStops(): List<Stop> {
        val stops = wptApiService.getNearbyStops().stops

        stops.forEach { stop ->
            stopDao.insertStop(stop.toStopEntity())
        }

        return stops
    }

    override suspend fun getStopFeatures(stopId: Int): List<StopFeature> {
        val stopFeatures = wptApiService.getStopFeatures(stopId).stopFeatures
        // TODO: check DB before calling API
        stopDao.insertStopFeatures(stopFeatures.toStopFeaturesEntity(stopId))

        return stopFeatures
    }

    override suspend fun getStopSchedules(stopId: Int): StopSchedule =
        wptApiService.getStopSchedules(stopId).stopSchedule
}