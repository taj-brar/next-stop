package com.tb.nextstop.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stopFeatures")
data class StopFeaturesEntity(
    @PrimaryKey
    val stopId: Int,
    val hasHeatedShelter: Boolean,
    val hasUnHeatedShelter: Boolean,
    val hasBench: Boolean,
    val hasESign: Boolean
)