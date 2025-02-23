package com.grasstools.tangential.presentation.ui.locationlist

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grasstools.tangential.DruvTaraApplication
import com.grasstools.tangential.data.db.GeofenceDao
import com.grasstools.tangential.domain.model.Geofence
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class LocationViewModel(
    private val dao: GeofenceDao
): ViewModel() {

    fun toggleEnabled(geofence: Geofence) {
        viewModelScope.launch {
            dao.updateGeofenceEnabled(geofence.id, !geofence.enabled)
        }
        DruvTaraApplication.getContext()?.sendBroadcast(Intent("GEOFENCE_CHANGE"))
    }

    fun deleteGeofence(geofence: Geofence) {
        viewModelScope.launch {
            dao.deleteGeofenceById(geofence.id)
        }
        DruvTaraApplication.getContext()?.sendBroadcast(Intent("GEOFENCE_CHANGE"))
    }

    fun getAllRecords(): Flow<List<Geofence>> {
        return dao.getAllGeofences()
    }
}