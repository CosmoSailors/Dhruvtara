package com.grasstools.tangential.presentation.ui.mapscreen.components

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun AddLocationCard(
    modifier: Modifier = Modifier,
    onSavedLocationsClick: () -> Unit,
    onAddLocationClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    var sliderPosition by remember { mutableFloatStateOf(0f) }

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

            Text(text = "Adjust radius:", modifier = Modifier.padding(bottom = 4.dp)) // Text above Slider

            Slider(
                value = sliderPosition,
                onValueChange = { sliderPosition = it },
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

                IconButton(onClick = onSettingsClick) {
                    Icon(Icons.Filled.Settings, contentDescription = "Settings")
                }
            }
        }
    }
}

@Preview
@Composable
fun AddLocationCardPreview() {
    val context = LocalContext.current
    AddLocationCard(
        onSavedLocationsClick = {
            println("Saved Locations clicked")
        },
        onAddLocationClick = {
            println("Add Location clicked")
        },
        onSettingsClick = {
            println("Settings clicked")
        }
    )
}


@Composable
fun MyScreen() {
    val context = LocalContext.current
    AddLocationCard(
        onSavedLocationsClick = {
//            val intent = Intent(context, NextActivity::class.java)
//            context.startActivity(intent)
        },
        onAddLocationClick = {
            // Handle add location click
        },
        onSettingsClick = {
            // Handle settings click
        }
    )
}