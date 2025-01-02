package com.tb.nextstop.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface StopDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStop(stop: StopEntity)

    @Update
    suspend fun updateStop(stop: StopEntity)

    @Delete
    suspend fun deleteStop(stop: StopEntity)

    @Query("SELECT * FROM stops WHERE stopID = :stopId")
    fun getStop(stopId: Int): Flow<StopEntity>

    @Query("SELECT * FROM stops ORDER BY stopId ASC")
    fun getAllStops(): Flow<List<StopEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStopFeatures(stopFeatures: StopFeaturesEntity)
}