package com.tb.nextstop.utils

import com.tb.nextstop.data.Route
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.intOrNull

const val HEATED_SHELTER = "Heated Shelter"
const val UNHEATED_SHELTER = "Unheated Shelter"
const val BENCH = "Bench"
const val E_SIGN = "BUSwatch Electronic Sign"

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

fun createRoute(routeKey: String): Route {
    return Route(
        key = JsonPrimitive(routeKey),
        number = JsonPrimitive(routeKey),
        badgeLabel = JsonPrimitive(routeKey)
    )
}

fun simplifyStopName(stopName: String): String {
    return stopName
        .replace("Northbound", "NB", ignoreCase = true)
        .replace("Southbound", "SB", ignoreCase = true)
        .replace("Eastbound", "EB", ignoreCase = true)
        .replace("Westbound", "WB", ignoreCase = true)
        .replace(" at ", " @ ", ignoreCase = true)
}