package com.grasstools.tangential.presentation.ui.mapscreen.components

import SettingsIconButton
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.grasstools.tangential.presentation.ui.mapscreen.MapsViewModel


@Composable
fun AddLocationCard(
    modifier: Modifier = Modifier,
    onSavedLocationsClick: () -> Unit,
    onAddLocationClick: () -> Unit,
    onSettingsClick: () -> Unit,
    sliderPosition: Float,
    onSliderChange: (Float) -> Unit,
vm: MapsViewModel
) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(text = "Adjust radius: ${10 + (190*sliderPosition)}m", modifier = Modifier.padding(bottom = 4.dp)) // Text above Slider

            Slider(
                value = sliderPosition,
                onValueChange = {onSliderChange(it)},
                modifier = Modifier
                    .padding(32.dp, 0.dp, 32.dp, 32.dp)
                    .fillMaxWidth()
                    .height(16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 32.dp, end = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onSavedLocationsClick) {
                    Icon(Icons.Filled.List, contentDescription = "Saved Locations")
                }

                TextButton(
                    onClick =  onAddLocationClick ,
                    modifier = Modifier.padding(8.dp),
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                    )
                ) {
                    Icon(Icons.Filled.Add, "Add location button", tint = MaterialTheme.colorScheme.onSurface)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Add Location", color = MaterialTheme.colorScheme.onSurface)
                }

                SettingsIconButton(
                    showAllMarkersFlow = vm.showAllMarkersFlag,
                    onToggleShowAllMarkers = { vm.toggleShowAllMarkers(it) }
                )
            }
        }
    }
}




