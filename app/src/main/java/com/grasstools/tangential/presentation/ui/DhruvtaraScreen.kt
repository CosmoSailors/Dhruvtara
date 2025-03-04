package com.grasstools.tangential.presentation.ui

import android.Manifest
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.grasstools.tangential.App
import com.grasstools.tangential.presentation.ui.alarmscreen.AlarmScreen
import com.grasstools.tangential.presentation.ui.locationlist.LocationListScreen
import com.grasstools.tangential.presentation.ui.mapscreen.MapsScreen
import com.grasstools.tangential.services.GeofenceManager
import com.grasstools.tangential.utils.NavControllerHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
fun DhruvtaraScreen(context: Context) {

    var geofenceManager by remember { mutableStateOf<GeofenceManager?>(null) }
    var geofenceManagerBound by remember { mutableStateOf(false) }

    val locationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    val notificationPermissionState = rememberPermissionState(
        Manifest.permission.POST_NOTIFICATIONS
    )

    if(locationPermissionState.status.isGranted && notificationPermissionState.status.isGranted ){
        val connection = object : ServiceConnection {
            override fun onServiceConnected(className: ComponentName, service: IBinder) {
                val binder = service as GeofenceManager.LocalBinder
                geofenceManager = binder.getService() // Update state
                geofenceManagerBound = true

                if (!geofenceManager!!.isInit) {
                    CoroutineScope(Dispatchers.IO).launch {
                        for (geofence in (context.applicationContext as App).database.dao().getAllGeofencesSnapshot()) {
                            geofenceManager?.register(geofence)
                        }
                        geofenceManager?.isInit = true
                    }
                } else {
                    Log.i("LOL", "Already init")
                }
            }

            override fun onServiceDisconnected(arg0: ComponentName) {
                geofenceManager = null
                geofenceManagerBound = false
            }
        }

        LaunchedEffect(Unit) {
            val intent = Intent(context, GeofenceManager::class.java)
            context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }

        DisposableEffect(Unit) {
            onDispose {
                if (geofenceManagerBound) {
                    context.unbindService(connection)
                }
            }
        }
    }



    val navController = rememberNavController()
    NavControllerHolder.navController = navController

    NavHost(navController = navController, startDestination = if(locationPermissionState.status.isGranted && notificationPermissionState.status.isGranted && checkDndAccess(context))MapsScreen else  PermissionScreen  ) {
        composable<PermissionScreen> {
            PermissionsScreen(
                onNavigateToMaps = {navController.navigate(route = MapsScreen)}
            )
        }

        composable<MapsScreen> {
            geofenceManager?.let { it1 ->
                MapsScreen(
                    geofenceManager = it1,
                    onNavigateToLocationList = { navController.navigate(route = LocationListScreen) }
                )
            }
        }

        composable<LocationListScreen> {
            geofenceManager?.let { it1 ->
                LocationListScreen(
                    geofenceManager = it1,
                    onNavigationToMaps = { navController.navigate(route = MapsScreen) },
                    onNavigateToAlert = { navController.navigate(route = AlertScreen) }
                )
            }

        }
        
        composable<AlertScreen> {
            AlarmScreen(
                onNavigateToMaps = { navController.navigate(route = MapsScreen) },

            )
        }
    }

}

private fun checkDndAccess(context: Context): Boolean {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    return notificationManager.isNotificationPolicyAccessGranted
}