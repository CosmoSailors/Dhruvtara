package com.grasstools.tangential.presentation.ui.locationlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grasstools.tangential.App
import com.grasstools.tangential.data.db.GeofenceDao
import com.grasstools.tangential.domain.model.Geofence
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class LocationViewModel(application: Application
): AndroidViewModel(application) {

    private val database by lazy { (application as App).database }
    val dao = database.dao()

    fun toggleEnabled(geofence: Geofence) {
        viewModelScope.launch {
            dao.updateGeofenceEnabled(geofence.id, !geofence.enabled)
        }
    }

    fun deleteGeofence(geofence: Geofence) {
        viewModelScope.launch {
            dao.deleteGeofenceById(geofence.id)
        }
    }

    fun getAllRecords(): Flow<List<Geofence>> {
        return dao.getAllGeofences()
    }
}