package com.grasstools.tangential.presentation.ui.mapscreen

import AddNickNameDialog
import android.os.Bundle
import android.util.Log
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
                    var showDialog by remember { mutableStateOf(false) } // State for dialog visibility

                    Column {
                        GoogleMapComposable(modifier = Modifier.weight(0.70f))
                        AddLocationCard(
                            modifier = Modifier.weight(0.30f),
                            onSavedLocationsClick = { onSavedLocationsClick() },
                            onAddLocationClick = { showDialog = true }, // Show dialog on click
                            onSettingsClick = { onSettingsClick() }
                        )

                        if (showDialog) {
                            AddNickNameDialog(
                                onDismissRequest = { showDialog = false },
                                onLocationAdded = { nickname ->
                                    // Handle the nickname (save it, etc.)
                                    showDialog = false // Dismiss the dialog
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun onSavedLocationsClick() {
        Log.i("i", "fsdf")
    }

    private fun onSettingsClick() {

    }
}



