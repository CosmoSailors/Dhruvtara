package com.grasstools.tangential.presentation.ui.mapscreen.components

import android.os.Bundle
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

@Composable
fun GoogleMapComposable(modifier: Modifier = Modifier) {  // Add modifier parameter
    val context = LocalContext.current
    var map: GoogleMap? by remember { mutableStateOf(null) }
    val sydney = remember { LatLng(-34.0, 151.0) }

    AndroidView(
        factory = {
            MapView(it).apply {
                onCreate(Bundle())
                getMapAsync { googleMap ->
                    map = googleMap
                    googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
                }
            }
        },
        modifier = modifier.fillMaxWidth(),
        update = { mapView ->
            map?.let { googleMap ->
                // Update map if needed
            }
        }
    )

    DisposableEffect(Unit) {
        onDispose {
            map?.clear()
        }
    }
}