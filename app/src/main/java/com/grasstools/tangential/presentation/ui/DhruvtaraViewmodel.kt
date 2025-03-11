package com.grasstools.tangential.presentation.ui

import androidx.lifecycle.ViewModel
import com.grasstools.tangential.domain.model.Geofence
import com.grasstools.tangential.repositories.GeofenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DhruvtaraViewmodel @Inject constructor(
    private val repository: GeofenceRepository
): ViewModel() {

    private val _allGeofence = MutableStateFlow<List<Geofence>>(emptyList())
    val allGeofence: StateFlow<List<Geofence>> = _allGeofence.asStateFlow()

    suspend fun getAllGeofence() = repository.getAllGeofenceFromDB()
}