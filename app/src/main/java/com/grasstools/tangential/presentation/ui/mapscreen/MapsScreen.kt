package com.grasstools.tangential.presentation.ui.mapscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.grasstools.tangential.presentation.ui.locationlist.LocationListScreen
import kotlinx.serialization.Serializable


@Composable
fun MapsScreen(onNavigateToLocationList: () -> Unit) {

    Column {
        Text("Hello MapsScreen")
    }
}
