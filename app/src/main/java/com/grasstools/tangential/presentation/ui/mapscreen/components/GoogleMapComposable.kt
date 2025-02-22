package com.grasstools.tangential.presentation.ui.mapscreen.components

import android.os.Bundle
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*

@Composable
fun GoogleMapComposable(modifier: Modifier = Modifier, sliderPosition: Float) {
    val context = LocalContext.current
    var map: GoogleMap? by remember { mutableStateOf(null) }
    var marker: Marker? by remember { mutableStateOf(null) } // Store marker reference
    var markerPosition by remember { mutableStateOf(LatLng(-34.0, 151.0)) } // Store marker position
    var circle: Circle? by remember { mutableStateOf(null) } // Store circle reference

    AndroidView(
        factory = {
            MapView(it).apply {
                onCreate(Bundle())
                getMapAsync { googleMap ->
                    map = googleMap

                    // Add initial marker
                    marker = googleMap.addMarker(
                        MarkerOptions().position(markerPosition).title("Drag me").draggable(true)
                    )

                    // Add initial circle
                    circle = googleMap.addCircle(
                        CircleOptions()
                            .center(markerPosition)
                            .radius(10.0 + (sliderPosition * 190)) // Initial Radius
                            .strokeColor(0x550000FF) // Semi-transparent blue border
                            .fillColor(0x220000FF) // Semi-transparent blue fill
                    )

                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerPosition, 10f))

                    // Update marker & circle when user taps on the map
                    googleMap.setOnMapClickListener { latLng ->
                        markerPosition = latLng // Update marker position state
                        marker?.position = latLng // Move marker
                        circle?.center = latLng // Move circle
                    }

                    // Set drag listener to update the circle's position when the marker moves
                    googleMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
                        override fun onMarkerDragStart(marker: Marker) {}

                        override fun onMarkerDrag(marker: Marker) {
                            markerPosition = marker.position // Update position state
                            circle?.center = markerPosition // Move circle to match marker
                        }

                        override fun onMarkerDragEnd(marker: Marker) {}
                    })
                }
            }
        },
        modifier = modifier.fillMaxWidth()
    )

    // **Update the radius when sliderPosition changes**
    LaunchedEffect(sliderPosition) {
        circle?.radius = 10.0 + (sliderPosition * 190) // ✅ Updates dynamically
    }

    DisposableEffect(Unit) {
        onDispose {
            map?.clear()
        }
    }
}
