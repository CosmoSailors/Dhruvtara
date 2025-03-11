package com.grasstools.tangential.data

import com.grasstools.tangential.data.db.GeofenceDao
import com.grasstools.tangential.domain.model.Geofence
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomLocalDataSource @Inject constructor(
    private val geofenceDao: GeofenceDao
): LocalDataSource {
    override suspend fun getAllGeofence(): Flow<List<Geofence>> {
        return geofenceDao.getAllGeofences()
    }

    override suspend fun insertGeofence(newGeofence: Geofence) {
        geofenceDao.insertGeofence(newGeofence)
    }

    override suspend fun updateGeofenceEnabled(geofence: Geofence) {
        geofenceDao.updateGeofenceEnabled(geofence.id, !geofence.enabled)
    }

    override suspend fun deleteGeofence(geofence: Geofence) {
        geofenceDao.deleteGeofenceById(geofence.id)
    }
}