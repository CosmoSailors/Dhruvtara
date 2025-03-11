package com.grasstools.tangential.presentation.ui.mapscreen

import android.annotation.SuppressLint
import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.grasstools.tangential.App
import com.grasstools.tangential.domain.model.Geofence
import com.grasstools.tangential.domain.model.GeofenceType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val application: Application
) : ViewModel() {

    private val database by lazy { (application as App).database }
    val dao = database.dao()

    private val _showDialogFlag = MutableStateFlow(false)
    val showDialogFlag: StateFlow<Boolean> = _showDialogFlag.asStateFlow()

    private val _sliderPosition = MutableStateFlow(0.0)
    val sliderPosition: StateFlow<Double> = _sliderPosition.asStateFlow()

    private val _latitude = MutableStateFlow(0.0)
    val latitude: StateFlow<Double> = _latitude.asStateFlow()

    private val _longitude = MutableStateFlow(0.0)
    val longitude: StateFlow<Double> = _longitude.asStateFlow()

    private val _radius = MutableStateFlow(50.0)
    val radius: StateFlow<Double> = _radius.asStateFlow()

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

    fun updateRadius(value: Double) {
        _radius.value = value
    }

    fun updateLatLong(value: LatLng) {
        _latitude.value = value.latitude
        _longitude.value = value.longitude
    }

    fun updateSliderPosition(value: Double) {
        _sliderPosition.value = value
        updateRadius(50 + (950 * value))
    }

    fun updateType(value: GeofenceType) {
        _type.value = value
    }

    private suspend fun insertLocationTrigger(geofence: Geofence) {
        dao.insertGeofence(geofence)
        loadAllGeofences()
    }

    private suspend fun loadAllGeofences() {
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
                application,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val fusedLocationClient: FusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(application)

            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    _latitude.value = location.latitude
                    _longitude.value = location.longitude
                    setShouldRecenterMap(true)
                }
            }
        }
    }

    fun onDialogSaveButtonClick(nickname: String) {

        val newGeofence = Geofence(
            name = nickname,
            latitude = latitude.value,
            longitude = longitude.value,
            radius = radius.value.toFloat(),
            type = type.value,
            config = "",
            enabled = true
        )
        viewModelScope.launch {
            insertLocationTrigger(newGeofence)
        }.invokeOnCompletion {
            onDialogDismiss()
        }

    }

    fun onAddLocationClick(){
        _showDialogFlag.value = true
    }

    fun onDialogDismiss(){
        _showDialogFlag.value = false
    }
}
