package com.tb.nextstop.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nearbyStops")
data class NearbyStopEntity(
    @PrimaryKey
    val stopId: Int,
    val name: String,
    val stopNumber: Int,
    val lat: Double,
    val lon: Double,
    val expiryTime: String
)