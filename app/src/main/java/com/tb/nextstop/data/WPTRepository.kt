package com.tb.nextstop.data

import com.tb.nextstop.network.WPTApiV2Service
import com.tb.nextstop.network.WPTApiV3Service
import com.tb.nextstop.utils.createRoute
import com.tb.nextstop.utils.isCacheExpired
import com.tb.nextstop.utils.toRoute
import com.tb.nextstop.utils.toSavedStopEntity
import com.tb.nextstop.utils.toStop
import com.tb.nextstop.utils.toStopEntity
import com.tb.nextstop.utils.toStopFeatureList
import com.tb.nextstop.utils.toStopFeaturesEntity
import com.tb.nextstop.utils.toStopRouteEntity
import kotlinx.coroutines.flow.firstOrNull

interface WPTRepository {
    suspend fun getNearbyStops(lat: Double, lon: Double): List<Stop>
    suspend fun getStopFeatures(stopId: Int): List<StopFeature>
    suspend fun getStopRoutes(stopId: Int): List<Route>
    suspend fun getStopSchedule(stopId: Int): StopSchedule
    suspend fun getLiveTrip(tripId: Int): LiveTripResponse
    suspend fun insertSavedStop(savedStop: Stop)
    suspend fun getSavedStops(): List<Stop>
}

class NetworkWPTRepository(
    private val wptApiV3Service: WPTApiV3Service,
    private val wptApiV2Service: WPTApiV2Service,
    private val stopDao: StopDao
) : WPTRepository {
    override suspend fun getNearbyStops(lat: Double, lon: Double): List<Stop> {
        val stops = wptApiV3Service.getNearbyStops(
            latitude = lat,
            longitude = lon
        ).stops

        stops.forEach { stop ->
            stopDao.insertStop(stop.toStopEntity())
        }

        return stops
    }

    override suspend fun getStopFeatures(stopId: Int): List<StopFeature> {
        val localData = stopDao.getStopFeatures(stopId).firstOrNull()
        var stopFeatures = listOf<StopFeature>()
        var needNetworkCall = true

        if (localData != null) {
            if (isCacheExpired(localData.expiryTime)) {
                stopDao.deleteStopFeatures(localData)
            } else {
                stopFeatures = localData.toStopFeatureList()
                needNetworkCall = false
            }
        }

        if (needNetworkCall) {
            stopFeatures = wptApiV3Service.getStopFeatures(stopId).stopFeatures
            stopDao.insertStopFeatures(stopFeatures.toStopFeaturesEntity(stopId))
        }

        return stopFeatures
    }

    override suspend fun getStopRoutes(stopId: Int): List<Route> {
        val localStopRoutes = stopDao.getStopRoutes(stopId).firstOrNull()
        var stopRoutes = listOf<Route>()
        var needNetworkCall = true

        if (!localStopRoutes.isNullOrEmpty()) {
            if (localStopRoutes.any { isCacheExpired(it.expiryTime) }) {
                localStopRoutes.forEach { localStopRoute ->
                    stopDao.deleteStopRoute(localStopRoute)
                }
            } else {
                stopRoutes = localStopRoutes.map { stopRoute ->
                    stopDao.getRoute(stopRoute.routeKey).firstOrNull()?.toRoute()
                        ?: createRoute(stopRoute.routeKey)
                }
                needNetworkCall = false
            }
        }

        if (needNetworkCall) {
            stopRoutes = wptApiV3Service.getStopSchedules(stopId).stopSchedule.routeSchedules
                .map { routeSchedule ->
                    routeSchedule.route
                }
            stopRoutes.forEach { route ->
                val stopRouteEntity = route.toStopRouteEntity(stopId)
                stopDao.insertStopRoute(stopRouteEntity)
            }
        }

        return stopRoutes
    }

    override suspend fun getStopSchedule(stopId: Int): StopSchedule {
        val result = wptApiV3Service.getStopSchedules(stopId).stopSchedule
        return result
    }

    override suspend fun getLiveTrip(tripId: Int): LiveTripResponse {
        return wptApiV2Service.getLiveTrip(tripId)
    }

    override suspend fun insertSavedStop(savedStop: Stop) {
        stopDao.insertSavedStop(savedStop.toSavedStopEntity())
    }

    override suspend fun getSavedStops(): List<Stop> {
        val savedStopEntities = stopDao.getAllSavedStops().firstOrNull()
        val savedStops = mutableListOf<Stop>()

        savedStopEntities?.forEach { entity ->
            savedStops.add(entity.toStop())
        }

        return savedStops
    }
}