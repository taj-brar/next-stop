package com.tb.nextstop.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class Stop(
    @SerialName(value = "key")
    val stopId: Int = 0,
    val name: String = "",
    @SerialName(value = "number")
    val stopNumber: Int = 0,
    val centre: Centre = Centre()
)

@Serializable
data class Centre(
    val geographic: GeographicPoint = GeographicPoint()
)

@Serializable
data class GeographicPoint(
    val latitude: String = "0",
    val longitude: String = "0"
)

@Serializable
data class StopsResponse(
    val stops: List<Stop> = listOf(),
)

@Serializable
data class StopFeature(
    val name: String = "",
    val count: Int = 0
)

@Serializable
data class StopFeaturesResponse(
    @SerialName(value = "stop-features")
    val stopFeatures: List<StopFeature> = listOf()
)

@Serializable
data class Route(
    val key: JsonElement,
    val number: JsonElement,
    @SerialName(value = "badge-label")
    val badgeLabel: JsonElement
)

@Serializable
data class RouteSchedules(
    val route: Route
)

@Serializable
data class StopSchedule(
    val stop: Stop = Stop(),
    @SerialName(value = "route-schedules")
    val routeSchedules: List<RouteSchedules> = listOf()
)

@Serializable
data class StopSchedulesResponse(
    @SerialName(value = "stop-schedule")
    val stopSchedule: StopSchedule = StopSchedule()
)