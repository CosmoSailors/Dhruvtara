package com.grasstools.tangential.presentation.ui.mapscreen

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.grasstools.tangential.data.db.LocationDao
import com.grasstools.tangential.domain.model.LocationTriggers
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class MapsViewModel(
    private val dao: LocationDao,
    private val context: Context
) : ViewModel() {

    var sliderPosition by mutableFloatStateOf(0f)
        private set

    var latitude by mutableStateOf(0.0)
        private set

    var longitude by mutableStateOf(0.0)
        private set

    var radius by mutableStateOf(10.0f)
        private set

    private val _showAllMarkersFlag = MutableStateFlow(false)
    val showAllMarkersFlag: StateFlow<Boolean> = _showAllMarkersFlag.asStateFlow()

    private val _allLocations = MutableStateFlow<List<LocationTriggers>>(emptyList())
    val allLocations: StateFlow<List<LocationTriggers>> = _allLocations.asStateFlow()

    init {
        viewModelScope.launch {
            dao.getAllLocations().collect { locations ->
                _allLocations.value = locations
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
        latitude = value.latitude
        longitude = value.longitude
        Log.i("MapsViewModel", "updateLatLong: $latitude, $longitude")
    }

    fun updateSliderPosition(value: Float) {
        sliderPosition = value
        updateRadius(10 + (190 * value))
    }

    suspend fun insertLocationTrigger(locationTrigger: LocationTriggers) {
        dao.insertLocation(locationTrigger)
        loadAllLocations()
    }

    suspend fun loadAllLocations() {
        dao.getAllLocations().collect { locations ->
            _allLocations.value = locations
        }
    }

}
