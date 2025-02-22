package com.grasstools.tangential.presentation.ui.mapscreen

import AddNickNameDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.grasstools.tangential.DruvTaraApplication
import com.grasstools.tangential.domain.model.LocationTriggers
import com.grasstools.tangential.ui.theme.TangentialTheme
import com.grasstools.tangential.presentation.ui.locationlist.LocationListActivity
import com.grasstools.tangential.presentation.ui.mapscreen.components.AddLocationCard
import com.grasstools.tangential.presentation.ui.mapscreen.components.GoogleMapComposable
import kotlinx.coroutines.launch

class MapsActivity : ComponentActivity() {

    private val database by lazy { (application as DruvTaraApplication).database }
    private val viewModel by viewModels<MapsViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MapsViewModel(database.dao()) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MapsScreen()
        }
    }

    @Composable
    private fun MapsScreen() {
        var showDialog by remember { mutableStateOf(false) }
        TangentialTheme {
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                Column {
                    GoogleMapComposable(modifier = Modifier.weight(0.70f), sliderPosition = viewModel.sliderPosition, onLatLongChange = {viewModel.updateLatLong(it)})
                    AddLocationCard(
                        modifier = Modifier.weight(0.30f),
                        onSavedLocationsClick = { onSavedLocationsClick() },
                        onAddLocationClick = { showDialog = true },
                        onSettingsClick = { onSettingsClick() },
                        sliderPosition = viewModel.sliderPosition,
                        onSliderChange = { viewModel.updateSliderPosition(it) }
                    )
                    if (showDialog) {
                        AddNickNameDialog(
                            onDismissRequest = { showDialog = false },
                            onLocationAdded = { nickname ->
                                insertTrigger(nickname)
                                showDialog = false
                                navigateToLocationListActivity()

                            },
                            latitude = viewModel.latitude,
                            longitude = viewModel.longitude,
                            radius = viewModel.radius
                        )
                    }
                }
            }
        }
    }

    private fun insertTrigger(nickname: String) {
        viewModel.viewModelScope.launch {
            viewModel.insertLocationTrigger(
                LocationTriggers(
                    name = nickname,
                    latitude = viewModel.latitude,
                    longitude = viewModel.longitude,
                    isDndEnabled = false,
                    radius = viewModel.radius.toDouble()
                )
            )
        }
    }

    private fun navigateToLocationListActivity() {
        startActivity(Intent(this, LocationListActivity::class.java))
    }

    private fun onSavedLocationsClick() {
        startActivity(Intent(this, LocationListActivity::class.java))

        Log.i("MapsActivity", "Saved Locations Clicked")
    }

    private fun onSettingsClick() {
        Log.i("MapsActivity", "Settings Clicked")
    }
}
