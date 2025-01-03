package com.tb.nextstop.utils

import com.tb.nextstop.data.Route
import com.tb.nextstop.data.RouteEntity
import com.tb.nextstop.data.Stop
import com.tb.nextstop.data.StopEntity
import com.tb.nextstop.data.StopFeature
import com.tb.nextstop.data.StopFeaturesEntity
import com.tb.nextstop.data.StopRouteEntity
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.intOrNull

const val HEATED_SHELTER = "Heated Shelter"
const val UNHEATED_SHELTER = "Unheated Shelter"
const val BENCH = "Bench"
const val E_SIGN = "BUSwatch Electronic Sign"

fun Stop.toStopEntity(): StopEntity {
    return StopEntity(
        stopId = stopId,
        name = name,
        stopNumber = stopNumber,
        lat = centre.geographic.latitude.toDoubleOrNull() ?: 0.0,
        lon = centre.geographic.longitude.toDoubleOrNull() ?: 0.0,
    )
}

fun List<StopFeature>.toStopFeaturesEntity(stopId: Int): StopFeaturesEntity {
    return StopFeaturesEntity(
        stopId = stopId,
        hasHeatedShelter = any { feature -> feature.name == HEATED_SHELTER },
        hasUnHeatedShelter = any { feature -> feature.name == UNHEATED_SHELTER },
        hasBench = any { feature -> feature.name == BENCH },
        hasESign = any { feature -> feature.name == E_SIGN },
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

fun tryGetValueFromJsonElement(jsonElement: JsonElement): Any? {
    return tryGetIntValue(jsonElement) ?: tryGetStringValue(jsonElement)
}

private fun tryGetStringValue(jsonElement: JsonElement): String? {
    val primitive = jsonElement as? JsonPrimitive
    return if (primitive?.isString == true) primitive.content else null
}

private fun tryGetIntValue(jsonElement: JsonElement): Int? {
    return (jsonElement as? JsonPrimitive)?.intOrNull
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
        routeKey = tryGetValueFromJsonElement(key).toString()
    )
}

fun Route.toRouteEntity(): RouteEntity {
    return RouteEntity(
        routeKey = tryGetStringValue(key) ?: "",
        number = tryGetStringValue(number) ?: "",
        badgeLabel = tryGetStringValue(badgeLabel) ?: ""
    )
}

fun createRoute(routeKey: String): Route {
    return Route(
        key = JsonPrimitive(routeKey),
        number = JsonPrimitive(routeKey),
        badgeLabel = JsonPrimitive(routeKey)
    )
}