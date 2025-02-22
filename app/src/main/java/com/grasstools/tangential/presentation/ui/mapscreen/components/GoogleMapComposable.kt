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
fun GoogleMapComposable(modifier: Modifier = Modifier, sliderPosition: Float, onLatLongChange: (LatLng) -> Unit) {
    val context = LocalContext.current
    var map: GoogleMap? by remember { mutableStateOf(null) }
    var marker: Marker? by remember { mutableStateOf(null) }
    var markerPosition by remember { mutableStateOf(LatLng(-34.0, 151.0)) }
    var circle: Circle? by remember { mutableStateOf(null) }

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
                    onLatLongChange(markerPosition)


                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerPosition, 10f))

                    googleMap.setOnMapClickListener { latLng ->
                        markerPosition = latLng
                        marker?.position = latLng
                        circle?.center = latLng
                        onLatLongChange(latLng)
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

    LaunchedEffect(sliderPosition) {
        circle?.radius = 10.0 + (sliderPosition * 190)
    }

    DisposableEffect(Unit) {
        onDispose {
            map?.clear()
        }
    }
}