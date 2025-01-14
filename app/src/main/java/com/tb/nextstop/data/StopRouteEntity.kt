package com.tb.nextstop.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stopRoutes")
data class StopRouteEntity(
    @PrimaryKey(autoGenerate = true)
    val key: Int = 0,
    val stopId: Int,
    val routeKey: String,
    val expiryTime: String
)