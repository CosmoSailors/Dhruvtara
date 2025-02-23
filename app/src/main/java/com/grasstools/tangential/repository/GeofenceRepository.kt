package com.grasstools.tangential.repository

import com.grasstools.tangential.App
import com.grasstools.tangential.domain.model.Geofence
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GeofenceRepository {
    private val database by lazy { App.getInstance()!!.database }
    private val dao = database.dao()

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    fun getAllGeofences(): Flow<Geofence> {
        val geofences: Flow<Geofence> = flow {
            dao.getAllGeofences().collect { list ->
                for (gf in list) {
                    emit(gf)
                }
            }
        }

        return geofences
    }
}