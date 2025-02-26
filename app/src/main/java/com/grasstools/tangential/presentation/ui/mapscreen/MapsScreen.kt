package com.grasstools.tangential.presentation.ui.mapscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable

@Serializable
object MapsScreen

@Serializable
object LocationListScreen

@Serializable
object AlertScreen


@Composable
fun MapsScreen(onNavigateToLocationList: () -> Unit) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = MapsScreen){
        composable<MapsScreen> {
            MapsScreen(
                onNavigateToLocationList = { navController.navigate(LocationListScreen) }
            )
        }

        composable<LocationListScreen> {
            com.grasstools.tangential.presentation.ui.mapscreen.LocationListScreen(
                onNavigateToAlert = { navController.navigate(AlertScreen) }
            )

        }
    }


    Column {
        Text("Hello World")
    }
}
