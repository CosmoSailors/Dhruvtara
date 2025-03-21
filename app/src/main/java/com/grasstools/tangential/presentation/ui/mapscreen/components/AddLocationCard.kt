package com.grasstools.tangential.presentation.ui.mapscreen.components

import SettingsIconButton
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.StateFlow

@Composable
fun AddLocationCard(
    modifier: Modifier = Modifier,
    onSavedLocationsClick: () -> Unit,
    onAddLocationClick: () -> Unit,
    sliderPosition: Double,
    onSliderChange: (Float) -> Unit,
    onToggleShowAllMarkers: (toggle: Boolean) -> Unit,
    showAllMarkersFlag: StateFlow<Boolean>,
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Adjust radius: ${(50 + (950 * sliderPosition)).toInt()}m",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Slider(
                value = sliderPosition.toFloat(),
                onValueChange = { onSliderChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary,
                    inactiveTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onSavedLocationsClick) {
                    Icon(
                        imageVector = Icons.Filled.List,
                        contentDescription = "Saved Locations",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Button(
                    onClick = onAddLocationClick,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add Location",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Add Location",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                SettingsIconButton(
                    showAllMarkersFlow = showAllMarkersFlag,
                    onToggleShowAllMarkers = { onToggleShowAllMarkers(it) }
                )
            }
        }
    }
}
