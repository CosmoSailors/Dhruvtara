package com.grasstools.tangential.data.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.grasstools.tangential.domain.model.Geofence
import com.grasstools.tangential.domain.repository.GeofenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GeofenceViewModel @Inject constructor(private val repository: GeofenceRepository) : ViewModel() {

    private val allGeofences = repository.getAllGeofences()

    val geofences: LiveData<List<Geofence>> = allGeofences.asLiveData()

    fun addGeofence(geofence: Geofence)  = viewModelScope.launch {
         repository.insertGeofence(geofence)
    }

    fun updateGeofence(geofence: Geofence) = viewModelScope.launch {
        repository.updateGeofence(geofence)

    }

    fun deleteGeofence(geofence: Geofence) = viewModelScope.launch {
        repository.deleteGeofence(geofence)
    }

}