package com.grasstools.tangential.presentation.ui.mapscreen

import android.annotation.SuppressLint
import com.grasstools.tangential.data.db.GeofenceDao
import com.grasstools.tangential.domain.model.Geofence

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
import com.google.android.gms.maps.model.LatLng
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.grasstools.tangential.domain.model.GeofenceType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class MapsViewModel(
    private val dao: GeofenceDao,
    private val context: Context
) : ViewModel() {

    var sliderPosition by mutableFloatStateOf(0f)
        private set

    var latitude = MutableStateFlow(0.0)
        private set

    var longitude = MutableStateFlow(0.0)
        private set

    var radius by mutableStateOf(10.0f)
        private set

    var type = MutableStateFlow(GeofenceType.DND)
        private set

    private val _showAllMarkersFlag = MutableStateFlow(false)
    val showAllMarkersFlag: StateFlow<Boolean> = _showAllMarkersFlag.asStateFlow()

    private val _allGeofences = MutableStateFlow<List<Geofence>>(emptyList())
    val allGeofences: StateFlow<List<Geofence>> = _allGeofences.asStateFlow()

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
        latitude.value = value.latitude
        longitude.value = value.longitude
        Log.i("MapsViewModel", "updateLatLong: $latitude, $longitude")
    }

    fun updateSliderPosition(value: Float) {
        sliderPosition = value
        updateRadius(10 + (190 * value))
    }

    fun updateType(value: GeofenceType) {
        type.value = value
    }

    suspend fun insertLocationTrigger(geofence: Geofence) {
        dao.insertGeofence(geofence)
        loadAllGeofences()
    }

    suspend fun loadAllGeofences() {
        dao.getAllGeofences().collect { geofences ->
            _allGeofences.value = geofences
        }
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
                    latitude.value = location.latitude
                    longitude.value = location.longitude
                }
            }
        }
    }

}
