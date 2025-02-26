package com.grasstools.tangential.presentation.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.grasstools.tangential.presentation.ui.locationlist.LocationListScreen
import com.grasstools.tangential.presentation.ui.mapscreen.MapsScreen
import kotlinx.serialization.Serializable

@Serializable
object MapsScreen

@Serializable
object LocationListScreen

@Serializable
object AlertScreen

@Composable
fun DhruvtaraScreen() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = MapsScreen){
        composable<MapsScreen> {
            MapsScreen(
                onNavigateToLocationList = { navController.navigate(route = LocationListScreen) }
            )
        }

        composable<LocationListScreen> {
            LocationListScreen(
                onNavigationToMaps = { navController.navigate(route = MapsScreen) },
                onNavigateToAlert = { navController.navigate(route = AlertScreen) }
            )

        }
    }

}