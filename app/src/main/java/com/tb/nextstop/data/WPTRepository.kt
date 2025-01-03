package com.tb.nextstop.data

import com.tb.nextstop.network.WPTApiService
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
        val localData = stopDao.getStopFeatures(stopId).firstOrNull()
        val stopFeatures: List<StopFeature>

        if (localData != null) {
            stopFeatures = localData.toStopFeatureList()
        } else {
            stopFeatures = wptApiService.getStopFeatures(stopId).stopFeatures
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
            stopRoutes = wptApiService.getStopSchedules(stopId).stopSchedule.routeSchedules
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
}