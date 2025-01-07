package com.tb.nextstop.data

import com.tb.nextstop.network.WPTApiV2Service
import com.tb.nextstop.network.WPTApiV3Service
import com.tb.nextstop.utils.createRoute
import com.tb.nextstop.utils.toRoute
import com.tb.nextstop.utils.toStopEntity
import com.tb.nextstop.utils.toStopFeatureList
import com.tb.nextstop.utils.toStopFeaturesEntity
import com.tb.nextstop.utils.toStopRouteEntity
import kotlinx.coroutines.flow.firstOrNull

interface WPTRepository {
    suspend fun getNearbyStops(): List<Stop>
    suspend fun getStopFeatures(stopId: Int): List<StopFeature>
    suspend fun getStopRoutes(stopId: Int): List<Route>
    suspend fun getStopSchedule(stopId: Int): StopSchedule
    suspend fun getLiveTrip(tripId: Int): LiveTripResponse
}

class NetworkWPTRepository(
    private val wptApiV3Service: WPTApiV3Service,
    private val wptApiV2Service: WPTApiV2Service,
    private val stopDao: StopDao
) : WPTRepository {
    override suspend fun getNearbyStops(): List<Stop> {
        val stops = wptApiV3Service.getNearbyStops().stops

        stops.forEach { stop ->
            stopDao.insertStop(stop.toStopEntity())
        }

        return stops
    }

    override suspend fun getStopFeatures(stopId: Int): List<StopFeature> {
        val localData = stopDao.getStopFeatures(stopId).firstOrNull()
        val stopFeatures: List<StopFeature>

        if (localData != null) {
            stopFeatures = localData.toStopFeatureList()
        } else {
            stopFeatures = wptApiV3Service.getStopFeatures(stopId).stopFeatures
            stopDao.insertStopFeatures(stopFeatures.toStopFeaturesEntity(stopId))
        }

        return stopFeatures
    }

    override suspend fun getStopRoutes(stopId: Int): List<Route> {
        val localStopRoutes = stopDao.getStopRoutes(stopId).firstOrNull()
        val stopRoutes: List<Route>

        if (!localStopRoutes.isNullOrEmpty()) {
            stopRoutes = localStopRoutes.map { stopRoute ->
                stopDao.getRoute(stopRoute.routeKey).firstOrNull()?.toRoute()
                    ?: createRoute(stopRoute.routeKey)
            }
        } else {
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
}