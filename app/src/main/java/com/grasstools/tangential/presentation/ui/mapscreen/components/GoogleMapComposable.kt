package com.grasstools.tangential.presentation.ui.mapscreen.components

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*

@SuppressLint("MissingPermission")
@Composable
fun GoogleMapComposable(
    modifier: Modifier = Modifier,
    sliderPosition: Float,
    onLatLongChange: (LatLng) -> Unit,
    latLng: LatLng
) {
    val context = LocalContext.current
    var map: GoogleMap? by remember { mutableStateOf(null) }
    var marker: Marker? by remember { mutableStateOf(null) }
    var markerPosition by remember { mutableStateOf(latLng) }
    var circle: Circle? by remember { mutableStateOf(null) }
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    LaunchedEffect(Unit) {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                location?.let {
                    val newLatLng = LatLng(it.latitude, it.longitude)
                    markerPosition = newLatLng
                    onLatLongChange(newLatLng)
                    map?.moveCamera(CameraUpdateFactory.newLatLngZoom(newLatLng, 12f))
                    marker?.position = newLatLng
                    Log.i("GoogleMapComposable", "Centered on $newLatLng")
                } ?: Log.e("GoogleMapComposable", "Failed to get location")
            }
            .addOnFailureListener { exception ->
                Log.e("GoogleMapComposable", "Error getting location", exception)
            }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = {
                MapView(it).apply {
                    onCreate(Bundle())
                    getMapAsync { googleMap ->
                        map = googleMap

                        marker = googleMap.addMarker(
                            MarkerOptions().position(markerPosition).title("Drag me").draggable(true)
                        )

                        circle = googleMap.addCircle(
                            CircleOptions()
                                .center(markerPosition)
                                .radius(10.0 + (sliderPosition * 190))
                                .strokeColor(0x550000FF)
                                .fillColor(0x220000FF)
                        )

                        googleMap.setOnMapClickListener { newLatLng ->
                            markerPosition = newLatLng
                            marker?.position = newLatLng
                            circle?.center = newLatLng
                            onLatLongChange(newLatLng)
                        }

                        googleMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
                            override fun onMarkerDragStart(marker: Marker) {}
                            override fun onMarkerDrag(marker: Marker) {
                                markerPosition = marker.position
                                circle?.center = markerPosition
                                onLatLongChange(markerPosition)
                            }
                            override fun onMarkerDragEnd(marker: Marker) {}
                        })
                    }
                }
            },
            modifier = modifier.fillMaxWidth()
        )
    }

    LaunchedEffect(sliderPosition) {
        circle?.radius = 10.0 + (sliderPosition * 190)
    }

    DisposableEffect(Unit) {
        onDispose {
            map?.clear()
        }
    }
}