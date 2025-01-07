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
data class ScheduleTime(
    val scheduled: String,
    val estimated: String
)

@Serializable
data class Times(
    val arrival: ScheduleTime? = null,
    val departure: ScheduleTime? = null
)

@Serializable
data class Variant(
    val key: String,
    val name: String
)

@Serializable
data class Bus(
    val key: Int,
    @SerialName(value = "bike-rack")
    val bikeRack: String,
    val wifi: String
)

@Serializable
data class ScheduledStop(
    val key: String,
    val cancelled: String,
    val times: Times,
    val variant: Variant,
    val bus: Bus? = null
)

@Serializable
data class RouteSchedules(
    val route: Route,
    @SerialName(value = "scheduled-stops")
    val scheduledStops: List<ScheduledStop>
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

@Serializable
data class LiveTripStopPosition(
    val lat: Double,
    val lng: Double
)

@Serializable
data class LiveTripScheduledStop(
    val id: String,
    val tripId: String,
    val stopId: String,
    val name: String,
    val scheduledTime: String,
    val estimatedTime: String,
    val delayed: Boolean,
    val cancelled: Boolean,
    val position: LiveTripStopPosition
)

@Serializable
data class LiveRoute(
    val id: String,
    val name: String,
    val label: String,
    val badgeLabel: String
)

@Serializable
data class BusFeatures(
    @SerialName(value = "easy-access")
    val easyAccess: Boolean? = null,
    val counter: Boolean? = null,
    @SerialName(value = "bike-rack")
    val bikeRack: Boolean? = null,
    val wifi: Boolean? = null,
    @SerialName(value = "wheelchair-securement")
    val wheelchairSecurement: Boolean? = null,
)

@Serializable
data class LiveTripResponse(
    val id: String,
    val busId: String,
    val route: LiveRoute,
    val busFeatures: BusFeatures,
    val scheduledStops: List<LiveTripScheduledStop>
)