package com.grasstools.tangential.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.grasstools.tangential.domain.model.LocationTriggers
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Upsert
    suspend fun upsertDataModel(dataModel: LocationTriggers)

    @Insert
    suspend fun insertLocation(location: LocationTriggers)

    @Query("SELECT * FROM locations")
    fun getAllLocations(): Flow<List<LocationTriggers>>

    @Query("SELECT * FROM locations WHERE id = :locationId")
    fun getLocationById(locationId: Int): Flow<LocationTriggers?>

    @Query("SELECT * FROM locations WHERE name LIKE :searchQuery")
    fun searchLocations(searchQuery: String): Flow<List<LocationTriggers>>

    @Query("DELETE FROM locations WHERE id = :locationId")
    suspend fun deleteLocationById(locationId: Int)




}