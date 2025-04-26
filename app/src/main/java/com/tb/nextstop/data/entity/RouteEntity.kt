package com.tb.nextstop.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "routes")
data class RouteEntity(
    @PrimaryKey
    val routeKey: String,
    val number: String,
    val badgeLabel: String,
    val expiryTime: String
)