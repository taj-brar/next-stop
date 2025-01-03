package com.tb.nextstop.utils

import com.tb.nextstop.data.Stop
import com.tb.nextstop.data.StopEntity
import com.tb.nextstop.data.StopFeature
import com.tb.nextstop.data.StopFeaturesEntity

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

const val HEATED_SHELTER = "Heated Shelter"
const val UNHEATED_SHELTER = "Unheated Shelter"
const val BENCH = "Bench"
const val E_SIGN = "BUSwatch Electronic Sign"