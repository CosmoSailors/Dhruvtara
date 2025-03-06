package com.grasstools.tangential.presentation.ui.permissionscreen

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.grasstools.tangential.presentation.ui.permissionscreen.components.PermissionCard


@Composable
fun PermissionsScreen(onNavigateToMaps: () -> Unit) {
    val context = LocalContext.current
    var locationGranted by remember { mutableStateOf(checkLocationPermission(context)) }
    var notificationGranted by remember { mutableStateOf(checkNotificationPermission(context)) }

    var hasDndPermission by remember { mutableStateOf(checkDndAccess(context)) }


    val locationLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> locationGranted = granted }

    val notificationLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> notificationGranted = granted }

    LaunchedEffect(locationGranted, notificationGranted) {
        while (true) {
            hasDndPermission = checkDndAccess(context)
            kotlinx.coroutines.delay(1000)
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Permissions Required",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            PermissionCard(
                title = "DND Access",
                description = "Allows automatic Do Not Disturb mode",
                granted = hasDndPermission,
                onRequest = {
                    val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                    context.startActivity(intent)
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            PermissionCard(
                title = "Location Access",
                description = "Required for geofencing & map features",
                granted = locationGranted,
                onRequest = { locationLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            PermissionCard(
                title = "Notification Access",
                description = "Required to notify you about geofence events",
                granted = notificationGranted,
                onRequest = { notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS) }
            )

            Spacer(modifier = Modifier.height(16.dp))



            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    onNavigateToMaps()
                },
                enabled = locationGranted && notificationGranted && hasDndPermission,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("🚀 Let's Go", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
            }
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
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    return notificationManager.isNotificationPolicyAccessGranted
}
