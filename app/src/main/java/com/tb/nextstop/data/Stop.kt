package com.tb.nextstop.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Stop(
    @SerialName(value = "key")
    val stopId: Int = 0,
    val name: String,
    @SerialName(value = "number")
    val stopNumber: Int,
)

@Serializable
data class StopsResponse(
    val stops: List<Stop>,
)

@Serializable
data class StopFeature(
    val name: String,
    val count: Int
)

@Serializable
data class StopFeaturesResponse(
    @SerialName(value = "stop-features")
    val stopFeatures: List<StopFeature>
)