package com.grasstools.tangential.presentation.ui

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.grasstools.tangential.presentation.ui.mapscreen.MapsActivity

class PermissionsManager : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PermissionsScreen()
        }
    }

    @Composable
    fun PermissionsScreen() {
        val context = this
        var locationGranted by remember { mutableStateOf(checkLocationPermission(context)) }
        var notificationGranted by remember { mutableStateOf(checkNotificationPermission(context)) }
        val dndAccessGranted by remember { mutableStateOf(checkDndAccess(context)) }

        val locationLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            locationGranted = granted
        }

        val notificationLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            notificationGranted = granted
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!locationGranted) {
                Button(onClick = {
                    locationLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }) {
                    Text("Grant Location Permission")
                }
            } else {
                Text("Location Permission Granted")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (!notificationGranted) {
                Button(onClick = {
                    notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }) {
                    Text("Grant Notification Permission")
                }
            } else
                Text("Notification Permission Granted")

            Spacer(modifier = Modifier.height(16.dp))

            if (!dndAccessGranted) {
                Button(onClick = {
                    val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                    context.startActivity(intent)
                }) {
                    Text("Request DND Access (Optional)")
                }
            } else {
                Text("DND Access Granted (Optional)")
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val intent = Intent(context, MapsActivity::class.java)
                    startActivity(intent)
                    finish()
                },
                enabled = locationGranted && notificationGranted
            ) {
                Text("-> Let's Go")
            }
        }
    }

    private fun checkLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkNotificationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context, Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkDndAccess(context: Context): Boolean {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return notificationManager.isNotificationPolicyAccessGranted
    }
}