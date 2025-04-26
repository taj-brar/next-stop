package com.tb.nextstop.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "savedStops")
data class SavedStopEntity(
    @PrimaryKey
    val stopId: Int,
    val name: String,
    val stopNumber: Int,
    val lat: Double,
    val lon: Double,
)