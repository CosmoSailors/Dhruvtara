package com.grasstools.tangential.presentation.ui.mapscreen

import AddNickNameDialog
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.LatLng
import com.grasstools.tangential.App
import com.grasstools.tangential.domain.model.Geofence
import com.grasstools.tangential.domain.model.GeofenceType
import com.grasstools.tangential.presentation.ui.alarmscreen.ui.theme.TangentialTheme
import com.grasstools.tangential.presentation.ui.locationlist.LocationListActivity
import com.grasstools.tangential.presentation.ui.mapscreen.components.AddLocationCard
import com.grasstools.tangential.presentation.ui.mapscreen.components.GoogleMapComposable
import com.grasstools.tangential.services.GeofenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MapsScreen(onNavigateToLocationList: () -> Unit) {
    lateinit var geofenceManager: GeofenceManager
    var geofenceManagerBound: Boolean = false
    val viewModel: MapsViewModel = viewModel()

    var showDialog by remember { mutableStateOf(false) }

    val latitude by viewModel.latitude.collectAsState()
    val longitude by viewModel.longitude.collectAsState()

    val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as GeofenceManager.LocalBinder
            geofenceManager = binder.getService()
            geofenceManagerBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            geofenceManagerBound = false
        }
    }

    fun resync() {
        geofenceManager.clear()
        geofenceManager.register(viewModel.dao.getAllGeofencesSnapshot())
    }


    TangentialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Box(modifier = Modifier.fillMaxSize()) {
                GoogleMapComposable(
                    modifier = Modifier.fillMaxSize(),
                    sliderPosition = viewModel.sliderPosition,
                    onLatLongChange = { viewModel.updateLatLong(it) },
                    latLng = LatLng(latitude, longitude),
                    vm = viewModel
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            viewModel.getCurrentLocation()
                        }, modifier = Modifier
                            .padding(4.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = "Current Location",
                            textAlign = TextAlign.Center
                        )
                    }

                    AddLocationCard(
                        modifier = Modifier
                            .fillMaxWidth().height(200.dp),
                        onSavedLocationsClick =  onNavigateToLocationList ,
                        onAddLocationClick = { },
                        onSettingsClick = { },
                        sliderPosition = viewModel.sliderPosition,
                        onSliderChange = {
                            viewModel.updateSliderPosition(it)
                        },
                        vm = viewModel
                    )
                }



                if (showDialog) {
                    AddNickNameDialog(
                        onDismissRequest = { showDialog = false },
                        onLocationAdded = { nickname ->
//                            insertTrigger(
//                                nickname,
//                                type = viewModel.type.value
//                            )
//                            CoroutineScope(Dispatchers.IO).launch {
//                                resync()
//                            }
//                            showDialog = false
//                            navigateToLocationListActivity()
                        },
                        latitude = latitude,
                        longitude = longitude,
                        radius = viewModel.radius,
                        vm = viewModel
                    )
                }
            }
        }
    }



}



