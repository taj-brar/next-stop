package com.tb.nextstop.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stops")
data class StopEntity(
    @PrimaryKey
    val stopId: Int,
    val name: String,
    val stopNumber: Int,
    val lat: Double,
    val lon: Double,
    val expiryTime: String
)