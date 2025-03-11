package com.grasstools.tangential.data

import com.grasstools.tangential.domain.model.Geofence
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun getAllGeofence(): Flow<List<Geofence>>
    suspend fun insertGeofence(newGeofence: Geofence)
    suspend fun updateGeofenceEnabled(geofence: Geofence)
    suspend fun deleteGeofence(geofence: Geofence)

}