package com.tb.nextstop.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tb.nextstop.data.entity.NearbyStopEntity
import com.tb.nextstop.data.entity.RouteEntity
import com.tb.nextstop.data.entity.SavedStopEntity
import com.tb.nextstop.data.entity.StopFeaturesEntity
import com.tb.nextstop.data.entity.StopRouteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StopDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStop(stop: NearbyStopEntity)

    @Update
    suspend fun updateStop(stop: NearbyStopEntity)

    @Delete
    suspend fun deleteStop(stop: NearbyStopEntity)

    @Query("SELECT * FROM nearbyStops WHERE stopID = :stopId")
    fun getStop(stopId: Int): Flow<NearbyStopEntity>

    @Query("SELECT * FROM nearbyStops ORDER BY stopId ASC")
    fun getAllStops(): Flow<List<NearbyStopEntity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStopFeatures(stopFeatures: StopFeaturesEntity)

    @Update
    fun updateStopFeatures(stopFeatures: StopFeaturesEntity)

    @Delete
    suspend fun deleteStopFeatures(stopFeatures: StopFeaturesEntity)

    @Query("SELECT * FROM stopFeatures WHERE stopId = :stopId")
    fun getStopFeatures(stopId: Int) : Flow<StopFeaturesEntity>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRoute(routeEntity: RouteEntity)

    @Update
    suspend fun updateRoute(stopRouteEntity: RouteEntity)

    @Delete
    suspend fun deleteRoute(routeEntity: RouteEntity)

    @Query("SELECT * FROM routes WHERE routeKey = :routeKey")
    fun getRoute(routeKey: String) : Flow<RouteEntity>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStopRoute(stopRouteEntity: StopRouteEntity)

    @Update
    suspend fun updateStopRoute(stopRouteEntity: StopRouteEntity)

    @Delete
    suspend fun deleteStopRoute(stopRouteEntity: StopRouteEntity)

    @Query("SELECT * FROM stopRoutes WHERE stopId = :stopId")
    fun getStopRoutes(stopId: Int) : Flow<List<StopRouteEntity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedStop(savedStop: SavedStopEntity)

    @Update
    suspend fun updateSavedStop(savedStop: SavedStopEntity)

    @Delete
    suspend fun deleteSavedStop(savedStop: SavedStopEntity)

    @Query("SELECT * FROM savedStops ORDER BY stopId ASC")
    fun getAllSavedStops(): Flow<List<SavedStopEntity>>
}