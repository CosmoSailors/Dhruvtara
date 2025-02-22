package com.grasstools.tangential.presentation.ui.mapscreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.* // Import Material3 components
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.grasstools.tangential.ui.theme.TangentialTheme
import com.google.android.gms.maps.GoogleMap
import com.grasstools.tangential.presentation.ui.mapscreen.components.AddLocationCard
import com.grasstools.tangential.presentation.ui.mapscreen.components.GoogleMapComposable

class MapsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TangentialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        GoogleMapComposable(modifier = Modifier.weight(0.7f))
                        AddLocationCard(modifier = Modifier
                            .weight(0.3f)
                        ) {
                        }
                    }
                }
            }
        }
    }
}



