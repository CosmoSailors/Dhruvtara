package com.grasstools.tangential.repositories

import com.grasstools.tangential.domain.model.Geofence
import kotlinx.coroutines.flow.Flow

interface GeofenceRepository {

    suspend fun getAllGeofenceFromDB(): Flow<List<Geofence>>;
    suspend fun insertGeofence(newGeofence: Geofence);
    suspend fun updateGeofenceEnabled(geofence: Geofence);
    suspend fun deleteGeofence(geofence: Geofence);

}