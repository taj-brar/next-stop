package com.tb.nextstop.utils

import com.tb.nextstop.data.Route
import com.tb.nextstop.data.RouteEntity
import com.tb.nextstop.data.Stop
import com.tb.nextstop.data.StopEntity
import com.tb.nextstop.data.StopFeature
import com.tb.nextstop.data.StopFeaturesEntity
import com.tb.nextstop.data.StopRouteEntity
import kotlinx.serialization.json.JsonPrimitive

fun Stop.toStopEntity(): StopEntity {
    return StopEntity(
        stopId = stopId,
        name = name,
        stopNumber = stopNumber,
        lat = centre.geographic.latitude.toDoubleOrNull() ?: 0.0,
        lon = centre.geographic.longitude.toDoubleOrNull() ?: 0.0,
        expiryTime = getCacheExpiryTime(),
    )
}

fun List<StopFeature>.toStopFeaturesEntity(stopId: Int): StopFeaturesEntity {
    return StopFeaturesEntity(
        stopId = stopId,
        hasHeatedShelter = any { feature -> feature.name == HEATED_SHELTER },
        hasUnHeatedShelter = any { feature -> feature.name == UNHEATED_SHELTER },
        hasBench = any { feature -> feature.name == BENCH },
        hasESign = any { feature -> feature.name == E_SIGN },
        expiryTime = getCacheExpiryTime(),
    )
}

fun StopFeaturesEntity.toStopFeatureList(): List<StopFeature> {
    val featureList = mutableListOf<StopFeature>()
    if (hasHeatedShelter)
        featureList.add(StopFeature(name = HEATED_SHELTER))
    if (hasUnHeatedShelter)
        featureList.add(StopFeature(name = UNHEATED_SHELTER))
    if (hasBench)
        featureList.add(StopFeature(name = BENCH))
    if (hasESign)
        featureList.add(StopFeature(name = E_SIGN))

    return featureList
}

fun RouteEntity.toRoute(): Route {
    return Route(
        key = JsonPrimitive(routeKey),
        number = JsonPrimitive(number),
        badgeLabel = JsonPrimitive(badgeLabel)
    )
}

fun Route.toStopRouteEntity(stopId: Int): StopRouteEntity {
    return StopRouteEntity(
        stopId = stopId,
        routeKey = tryGetValueFromJsonElement(key).toString(),
        expiryTime = getCacheExpiryTime(),
    )
}