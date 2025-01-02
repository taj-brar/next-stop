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
    suspend fun insert(stop: StopEntity)

    @Update
    suspend fun update(stop: StopEntity)

    @Delete
    suspend fun delete(stop: StopEntity)

    @Query("SELECT * FROM stops WHERE stopID = :stopId")
    fun getStop(stopId: Int): Flow<StopEntity>

    @Query("SELECT * FROM stops ORDER BY stopId ASC")
    fun getAllStops(): Flow<List<StopEntity>>
}