package com.grasstools.tangential.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.grasstools.tangential.domain.model.Geofence
import kotlinx.coroutines.flow.Flow

@Dao
interface GeofenceDao {
//    @Upsert
//    suspend fun upsertDataModel(dataModel: LocationTriggers)
//
//    @Insert
//    suspend fun insertLocation(location: LocationTriggers)
//
//    @Query("SELECT * FROM locations")
//    fun getAllLocations(): Flow<List<LocationTriggers>>
//
//    @Query("SELECT * FROM locations WHERE id = :locationId")
//    fun getLocationById(locationId: Int): Flow<LocationTriggers?>
//
//    @Query("SELECT * FROM locations WHERE name LIKE :searchQuery")
//    fun searchLocations(searchQuery: String): Flow<List<LocationTriggers>>
//
//    @Query("DELETE FROM locations WHERE id = :locationId")
//    suspend fun deleteLocationById(locationId: Int)
//
//    @Query("UPDATE locations SET isDndEnabled = :isDndEnabled WHERE id = :locationId")
//    suspend fun updateDndStatus(locationId: Int, isDndEnabled: Boolean)
//
//    @Query("DELETE FROM locations")
//    suspend fun deleteAllLocations()

    @Upsert
    suspend fun upsertDataModel(dataModel: Geofence)

    @Insert
    suspend fun insertGeofence(geofence: Geofence)

    @Query("SELECT * FROM geofences")
    fun getAllGeofences(): Flow<List<Geofence>>

    @Query("SELECT * FROM geofences")
    fun getAllGeofencesSnapshot(): List<Geofence>

    @Query("SELECT * FROM geofences WHERE id = :geofenceId")
    fun getGeofenceById(geofenceId: Int): Flow<Geofence?>

    @Query("SELECT * FROM geofences WHERE name LIKE :searchQuery")
    fun searchGeofences(searchQuery: String): Flow<List<Geofence>>

    @Query("DELETE FROM geofences WHERE id = :geofenceId")
    suspend fun deleteGeofenceById(geofenceId: Int)

    @Query("UPDATE geofences SET enabled = :enabled WHERE id = :geofenceId")
    suspend fun updateGeofenceEnabled(geofenceId: Int, enabled: Boolean)

    @Query("DELETE FROM geofences")
    suspend fun deleteAllGeofences()
}