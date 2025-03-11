package com.grasstools.tangential.presentation.ui.locationlistscreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grasstools.tangential.App
import com.grasstools.tangential.domain.model.Geofence
import com.grasstools.tangential.repositories.GeofenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val repository: GeofenceRepository
) : ViewModel() {

    private val _allGeofence = MutableStateFlow<List<Geofence>>(emptyList())
    val allGeofence: StateFlow<List<Geofence>> = _allGeofence.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllGeofenceFromDB().collect { geofences ->
                _allGeofence.value = geofences
            }
        }
    }

    fun toggleEnabled(geofence: Geofence) {
        viewModelScope.launch {
            repository.updateGeofenceEnabled(geofence)
        }
    }

    fun deleteGeofence(geofence: Geofence) {
        viewModelScope.launch {
            repository.deleteGeofence(geofence)
        }
    }

    suspend fun getAllGeofence(): Flow<List<Geofence>> {
        viewModelScope.launch {
            repository.getAllGeofenceFromDB().collect { geofences ->
                _allGeofence.value = geofences
            }
        }
        return allGeofence
    }
}