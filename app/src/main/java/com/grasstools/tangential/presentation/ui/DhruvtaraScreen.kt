package com.grasstools.tangential.presentation.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.grasstools.tangential.presentation.ui.alarmscreen.AlarmScreen
import com.grasstools.tangential.presentation.ui.locationlist.LocationListScreen
import com.grasstools.tangential.presentation.ui.mapscreen.MapsScreen
import com.grasstools.tangential.services.GeofenceManager
import com.grasstools.tangential.utils.NavControllerHolder
import kotlinx.serialization.Serializable

@Serializable
object MapsScreen

@Serializable
object LocationListScreen

@Serializable
object AlertScreen

@Composable
fun DhruvtaraScreen(geofenceManager: GeofenceManager) {

    val navController = rememberNavController()
    NavControllerHolder.navController = navController

    NavHost(navController = navController, startDestination = MapsScreen){
        composable<MapsScreen> {
            MapsScreen(
                geofenceManager = geofenceManager,
                onNavigateToLocationList = { navController.navigate(route = LocationListScreen) }
            )
        }

        composable<LocationListScreen> {
            LocationListScreen(
                geofenceManager = geofenceManager,
                onNavigationToMaps = { navController.navigate(route = MapsScreen) },
                onNavigateToAlert = { navController.navigate(route = AlertScreen) }
            )

        }
        
        composable<AlertScreen> {
            AlarmScreen(
                onNavigateToMaps = { navController.navigate(route = MapsScreen) },

            )
        }
    }

}