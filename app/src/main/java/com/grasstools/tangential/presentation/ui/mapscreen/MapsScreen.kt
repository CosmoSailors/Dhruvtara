package com.grasstools.tangential.presentation.ui.mapscreen

import DetailsDialog
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.grasstools.tangential.presentation.ui.alarmscreen.ui.theme.TangentialTheme
import com.grasstools.tangential.presentation.ui.mapscreen.components.AddLocationCard
import com.grasstools.tangential.presentation.ui.mapscreen.components.GoogleMapComposable
import com.grasstools.tangential.services.GeofenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MapsScreen(onNavigateToLocationList: () -> Unit, geofenceManager: GeofenceManager) {

    val viewModel: MapsViewModel = viewModel()

    val latitude by viewModel.latitude.collectAsState()
    val longitude by viewModel.longitude.collectAsState()
    val showDialogFlag by viewModel.showDialogFlag.collectAsState()
    val sliderPosition by viewModel.sliderPosition.collectAsState()

    fun resync() {
        geofenceManager.clear()
        geofenceManager.register(viewModel.dao.getAllGeofencesSnapshot())
    }

    LaunchedEffect(Unit) {
        viewModel.getCurrentLocation()
    }

    TangentialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Box(modifier = Modifier.fillMaxSize()) {
                GoogleMapComposable(
                    modifier = Modifier.fillMaxSize(),
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
                        onAddLocationClick = {viewModel.onAddLocationClick()},
                        sliderPosition = sliderPosition,
                        onSliderChange = {
                            viewModel.updateSliderPosition(it.toDouble())
                        },
                        showAllMarkersFlag = viewModel.showAllMarkersFlag,
                        onToggleShowAllMarkers ={ viewModel.toggleShowAllMarkers(it) }
                    )
                }



                if (showDialogFlag) {
                    DetailsDialog(
                        onDismissRequest = { viewModel.onDialogDismiss() },
                        onLocationAdded = { nickname ->
                            viewModel.onDialogSaveButtonClick(nickname)
                            onNavigateToLocationList()
                            CoroutineScope(Dispatchers.IO).launch {
                              resync()
                           }
                        },
                        vm = viewModel
                    )
                }
            }
        }
    }



}



