package com.tb.nextstop.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tb.nextstop.data.entity.RouteEntity
import com.tb.nextstop.data.entity.StopEntity
import com.tb.nextstop.data.entity.StopFeaturesEntity
import com.tb.nextstop.data.entity.StopRouteEntity

@Database(
    entities = [
        StopEntity::class,
        StopFeaturesEntity::class,
        RouteEntity::class,
        StopRouteEntity::class
    ],
    version = 6,
    exportSchema = false
)
abstract class StopsDatabase : RoomDatabase() {
    abstract fun stopDao(): StopDao

    companion object {
        @Volatile
        private var Instance: StopsDatabase? = null

        fun getDatabase(context: Context): StopsDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, StopsDatabase::class.java, "stop_database")
                    .fallbackToDestructiveMigration(true)
                    .build()
                    .also { Instance = it }
            }
        }
    }
}