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

    private var fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

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
    }

    @SuppressLint("MissingPermission")
    fun recenterMap() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    val newLatLng = LatLng(location.latitude, location.longitude)
                    updateLatLong(newLatLng)
                    Log.i("MapsViewModel", "recenterMap: Centered on $newLatLng")
                } else {
                    Log.e("MapsViewModel", "recenterMap: Failed to get location")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("MapsViewModel", "recenterMap: Error getting location", exception)
            }
    }
}
