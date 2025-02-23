package com.grasstools.tangential.presentation.ui.mapscreen.components

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.grasstools.tangential.presentation.ui.mapscreen.MapsViewModel

@SuppressLint("MissingPermission")
@Composable
fun GoogleMapComposable(
    modifier: Modifier = Modifier,
    sliderPosition: Float,
    onLatLongChange: (LatLng) -> Unit,
    latLng: LatLng,
    vm: MapsViewModel
) {
    val context = LocalContext.current
    var map by remember { mutableStateOf<GoogleMap?>(null) }
    var marker by remember { mutableStateOf<Marker?>(null) }
    var circle by remember { mutableStateOf<Circle?>(null) }
    var markerPosition by remember { mutableStateOf(latLng) }
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val showAllMarkers by vm.showAllMarkersFlag.collectAsState()
    val allLocations by vm.allLocations.collectAsState()

    val latitude by vm.latitude.collectAsState()
    val longitude by vm.longitude.collectAsState()


    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = {
                MapView(it).apply {
                    onCreate(Bundle())
                    getMapAsync { googleMap ->
                        map = googleMap
                        googleMap.uiSettings.isZoomControlsEnabled = true

                        marker = googleMap.addMarker(
                            MarkerOptions().position(markerPosition).title("Current Location").draggable(true)
                        )

                        circle = googleMap.addCircle(
                            CircleOptions()
                                .center(markerPosition)
                                .radius(10.0 + (sliderPosition * 190))
                                .strokeColor(0x550000FF)
                                .fillColor(0x220000FF)
                        )

                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerPosition, 12f))

                        googleMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
                            override fun onMarkerDragStart(marker: Marker) {}
                            override fun onMarkerDrag(marker: Marker) {
                                circle?.center = marker.position
                                onLatLongChange(marker.position)
                            }
                            override fun onMarkerDragEnd(marker: Marker) {}
                        })

                        googleMap.setOnMapClickListener { newLatLng ->
                            marker?.position = newLatLng
                            circle?.center = newLatLng
                            onLatLongChange(newLatLng)
                        }
                    }
                }
            },
            modifier = modifier.fillMaxWidth()
        )
    }

    LaunchedEffect(Unit) {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                location?.let {
                    val newLatLng = LatLng(it.latitude, it.longitude)
                    markerPosition = newLatLng
                    onLatLongChange(newLatLng)

                    map?.let { googleMap ->
                        marker?.position = newLatLng
                        circle?.center = newLatLng
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLatLng, 12f))
                    }
                }
            }
    }

    LaunchedEffect(latitude, longitude) {
        val newLatLng = LatLng(latitude, longitude)
        marker?.position = newLatLng
        circle?.center = newLatLng
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(newLatLng, 12f))
    }

    LaunchedEffect(showAllMarkers) {
        map?.let { googleMap ->
            if (showAllMarkers) {
                if (allLocations.isNotEmpty()) {
                    val boundsBuilder = LatLngBounds.Builder()

                    allLocations.forEach { location ->
                        val position = LatLng(location.latitude, location.longitude)
                        val markerColor = if (location.isDndEnabled) {
                            BitmapDescriptorFactory.HUE_GREEN
                        } else {
                            BitmapDescriptorFactory.HUE_BLUE
                        }

                        googleMap.addMarker(
                            MarkerOptions()
                                .position(position)
                                .title(location.name)
                                .icon(BitmapDescriptorFactory.defaultMarker(markerColor))
                        )
                        boundsBuilder.include(position)
                    }

                    val bounds = boundsBuilder.build()
                    val padding = 200
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding))
                }
            } else {
                val currentMarkerPosition = marker?.position
                googleMap.clear()
                marker = googleMap.addMarker(
                    MarkerOptions().position(currentMarkerPosition!!).title("Current Location").draggable(true)
                )
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentMarkerPosition, 12f))
            }
        }
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


