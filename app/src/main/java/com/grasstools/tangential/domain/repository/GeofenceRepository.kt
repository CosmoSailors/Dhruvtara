package com.grasstools.tangential.domain.repository

import com.grasstools.tangential.data.db.GeofenceDao
import com.grasstools.tangential.domain.model.Geofence
import kotlinx.coroutines.flow.Flow

class GeofenceRepository(private val dao: GeofenceDao) {

    fun getAllGeofences(): Flow<List<Geofence>> = dao.getAllGeofences();

    suspend fun insertGeofence(geofence: Geofence) = dao.insertGeofence(geofence);

    suspend fun updateGeofence(geofence: Geofence) = dao.upsertDataModel(geofence);

    suspend fun deleteGeofence(geofence: Geofence) = dao.deleteGeofenceById(geofence.id);


}