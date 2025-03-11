package com.grasstools.tangential.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.grasstools.tangential.domain.model.Geofence
import kotlinx.coroutines.flow.Flow


@Dao
interface GeofenceDao {

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