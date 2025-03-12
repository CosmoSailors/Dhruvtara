package com.grasstools.tangential.presentation.ui

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.grasstools.tangential.presentation.ui.alarmscreen.AlarmScreen
import com.grasstools.tangential.presentation.ui.locationlistscreen.LocationListScreen
import com.grasstools.tangential.presentation.ui.mapscreen.MapsScreen
import com.grasstools.tangential.presentation.ui.permissionscreen.PermissionsScreen
import com.grasstools.tangential.services.GeofenceManager
import com.grasstools.tangential.utils.NavControllerHolder
import kotlinx.serialization.Serializable

@Serializable
object MapsScreen

@Serializable
object LocationListScreen

@Serializable
object AlertScreen

@Serializable
object PermissionScreen

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun DhruvtaraScreen(context: Context, viewModel: DhruvtaraViewmodel = hiltViewModel()) {

    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val notificationPermissionState = rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
    val permissionsGranted = locationPermissionState.status.isGranted && notificationPermissionState.status.isGranted

    HandleGeofenceService(context, viewModel, permissionsGranted)

    val navController = rememberNavController()
    NavControllerHolder.navController = navController

    NavHost(
        navController = navController,
        startDestination = if (permissionsGranted && checkDndAccess(context)) MapsScreen else PermissionScreen
    ) {
        composable<PermissionScreen> { PermissionsScreen(onNavigateToMaps = { navController.navigate(route = MapsScreen) }) }
        composable<MapsScreen> { MapsScreen(onNavigateToLocationList = { navController.navigate(route = LocationListScreen) }) }
        composable<LocationListScreen> { LocationListScreen(onNavigationToMaps = { navController.navigate(route = MapsScreen) }) }
        composable<AlertScreen> { AlarmScreen(onNavigateToMaps = { navController.navigate(route = MapsScreen) }) }
    }
}

@Composable
private fun HandleGeofenceService(context: Context, viewModel: DhruvtaraViewmodel, permissionsGranted: Boolean) {
    if (permissionsGranted) {
        LaunchedEffect(Unit) {
            val intent = Intent(context, GeofenceManager::class.java)
            context.bindService(intent, viewModel.connection, Context.BIND_AUTO_CREATE)
        }

        DisposableEffect(Unit) {
            onDispose {
                if (viewModel.geofenceManagerBound.value) {
                    context.unbindService(viewModel.connection)
                }
            }
        }
    }
}

private fun checkDndAccess(context: Context): Boolean {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    return notificationManager.isNotificationPolicyAccessGranted
}