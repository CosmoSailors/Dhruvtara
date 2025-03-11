package com.grasstools.tangential.repositories

import com.grasstools.tangential.data.LocalDataSource
import com.grasstools.tangential.domain.model.Geofence
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GeofenceRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource
): GeofenceRepository {

    override suspend fun getAllGeofenceFromDB(): Flow<List<Geofence>> {
        return localDataSource.getAllGeofence()
    }

    override suspend fun insertGeofence(newGeofence: Geofence) {
        localDataSource.insertGeofence(newGeofence)
    }

    override suspend fun updateGeofenceEnabled(geofence: Geofence) {
        localDataSource.updateGeofenceEnabled(geofence)
    }

    override suspend fun deleteGeofence(geofence: Geofence) {
        localDataSource.deleteGeofence(geofence)
    }

}