package com.grasstools.tangential.presentation.ui.mapscreen

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.grasstools.tangential.data.db.GeofenceDao
import com.grasstools.tangential.domain.model.Geofence
import com.grasstools.tangential.domain.model.GeofenceType
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MapsViewModel(
    private val dao: GeofenceDao,
    private val context: Context
) : ViewModel() {

    var sliderPosition by mutableFloatStateOf(0f)
        private set

    private val _latitude = MutableStateFlow(0.0)
    val latitude: StateFlow<Double> = _latitude.asStateFlow()

    private val _longitude = MutableStateFlow(0.0)
    val longitude: StateFlow<Double> = _longitude.asStateFlow()

    var radius by mutableFloatStateOf(10.0f)
        private set

    private val _type = MutableStateFlow(GeofenceType.DND)
    val type: StateFlow<GeofenceType> = _type.asStateFlow()

    private val _showAllMarkersFlag = MutableStateFlow(false)
    val showAllMarkersFlag: StateFlow<Boolean> = _showAllMarkersFlag.asStateFlow()

    private val _allGeofences = MutableStateFlow<List<Geofence>>(emptyList())
    val allGeofences: StateFlow<List<Geofence>> = _allGeofences.asStateFlow()

    private val _shouldRecenterMap = MutableStateFlow(false)
    val shouldRecenterMap: StateFlow<Boolean> = _shouldRecenterMap.asStateFlow()

    init {
        viewModelScope.launch {
            dao.getAllGeofences().collect { geofences ->
                _allGeofences.value = geofences
            }
        }
    }

    fun toggleShowAllMarkers(value: Boolean) {
        _showAllMarkersFlag.value = value
    }

    fun updateRadius(value: Float) {
        radius = value
    }

    fun updateLatLong(value: LatLng) {
        _latitude.value = value.latitude
        _longitude.value = value.longitude
        Log.i("MapsViewModel", "updateLatLong: ${_latitude.value}, ${_longitude.value}")
    }

    fun updateSliderPosition(value: Float) {
        sliderPosition = value
        updateRadius(10 + (190 * value))
    }

    fun updateType(value: GeofenceType) {
        _type.value = value
    }

    suspend fun insertLocationTrigger(geofence: Geofence) {
        dao.insertGeofence(geofence)
        loadAllGeofences()
    }

    suspend fun loadAllGeofences() {
        viewModelScope.launch {
            dao.getAllGeofences().collect { geofences ->
                _allGeofences.value = geofences
            }
        }
    }

    fun setShouldRecenterMap(value: Boolean) {
        _shouldRecenterMap.value = value
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val fusedLocationClient: FusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(context)

            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    _latitude.value = location.latitude
                    _longitude.value = location.longitude
                    setShouldRecenterMap(true)
                }
            }
        }
    }
}
