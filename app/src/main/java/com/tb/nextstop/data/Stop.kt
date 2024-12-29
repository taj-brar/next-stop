package com.tb.nextstop.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Stop(
    @SerialName(value = "key")
    val stopId: Int = 0,
    val name: String
)

@Serializable
data class StopsResponse(
    val stops: List<Stop>,
)