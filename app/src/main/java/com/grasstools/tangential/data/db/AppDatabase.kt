package com.grasstools.tangential.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.grasstools.tangential.domain.model.LocationTriggers

@Database(entities = [LocationTriggers::class], version = 1)
abstract class TangentialDatabase: RoomDatabase() {
    abstract fun dao(): LocationDao

    companion object {
        @Volatile
        private var INSTANCE: TangentialDatabase? = null

        fun getDatabase(context: Context): TangentialDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TangentialDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}