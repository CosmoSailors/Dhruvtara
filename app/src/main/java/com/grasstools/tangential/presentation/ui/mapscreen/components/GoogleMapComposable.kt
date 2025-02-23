package com.grasstools.tangential.presentation.ui.mapscreen.components

import android.os.Bundle
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.grasstools.tangential.presentation.ui.mapscreen.MapsViewModel

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
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val showAllMarkers by vm.showAllMarkersFlag.collectAsState()
    val allLocations by vm.allGeofences.collectAsState()
    val latitude by vm.latitude.collectAsState()
    val longitude by vm.longitude.collectAsState()
    val shouldRecenterMap by vm.shouldRecenterMap.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = {
                MapView(it).apply {
                    onCreate(Bundle())
                    getMapAsync { googleMap ->
                        map = googleMap
                        googleMap.uiSettings.isZoomControlsEnabled = true

                        marker = googleMap.addMarker(
                            MarkerOptions()
                                .position(latLng)
                                .title("Set Location")
                                .draggable(true)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                        )

                        circle = googleMap.addCircle(
                            CircleOptions()
                                .center(latLng)
                                .radius(50.0 + (sliderPosition * 950))
                                .strokeColor(0xFF000000.toInt()) // Black stroke
                                .fillColor(0x5500A8E0.toInt()) // Semi-transparent blue
                        )

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

    LaunchedEffect(latitude, longitude) {
        val newLatLng = LatLng(latitude, longitude)
        marker?.position = newLatLng
        circle?.center = newLatLng

        if (shouldRecenterMap) {
            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(newLatLng, 15.0f))
            vm.setShouldRecenterMap(false)
        }
    }

    LaunchedEffect(showAllMarkers) {
        map?.let { googleMap ->
            googleMap.clear()

            // ✅ Re-add the main draggable marker after clearing other markers
            marker = googleMap.addMarker(
                MarkerOptions()
                    .position(marker?.position ?: latLng)
                    .title("Set Location")
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            )

            circle = googleMap.addCircle(
                CircleOptions()
                    .center(marker?.position ?: latLng)
                    .radius(50.0 + (sliderPosition * 950))
                    .strokeColor(0xFF000000.toInt())
                    .fillColor(0x5500A8E0.toInt())
            )

            if (showAllMarkers && allLocations.isNotEmpty()) {
                val boundsBuilder = LatLngBounds.Builder()
                allLocations.forEach { location ->
                    val position = LatLng(location.latitude, location.longitude)
                    val markerColor = if (location.enabled) {
                        BitmapDescriptorFactory.HUE_GREEN
                    } else {
                        BitmapDescriptorFactory.HUE_RED
                    }

                    val circleColor = if (location.enabled) {
                        0x4400FF00.toInt() // Light Green
                    } else {
                        0x44FF0000.toInt() // Light Red
                    }

                    googleMap.addCircle(
                        CircleOptions()
                            .center(position)
                            .radius(location.radius.toDouble())
                            .strokeColor(0xFF000000.toInt())
                            .fillColor(circleColor)
                    )

                    googleMap.addMarker(
                        MarkerOptions()
                            .position(position)
                            .title(location.name)
                            .icon(BitmapDescriptorFactory.defaultMarker(markerColor))
                    )
                    boundsBuilder.include(position)
                }

                val bounds = boundsBuilder.build()
                googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150))
            }
        }
    }

    LaunchedEffect(sliderPosition) {
        circle?.radius = 50.0 + (sliderPosition * 950)
    }

    DisposableEffect(Unit) {
        onDispose {
            map?.clear()
        }
    }
}
