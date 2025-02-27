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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.grasstools.tangential.presentation.ui.alarmscreen.ui.theme.TangentialTheme

class PermissionsManager : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TangentialTheme {
                PermissionsScreen()
            }
        }
    }
}

@Composable
fun PermissionsScreen() {
    val context = LocalContext.current
    var locationGranted by remember { mutableStateOf(checkLocationPermission(context)) }
    var notificationGranted by remember { mutableStateOf(checkNotificationPermission(context)) }
    var dndAccessGranted by remember { mutableStateOf(checkDndAccess(context)) }

    val locationLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> locationGranted = granted }

    val notificationLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> notificationGranted = granted }

    LaunchedEffect(locationGranted, notificationGranted, dndAccessGranted) {
        if (locationGranted && notificationGranted && dndAccessGranted) {
//            navigateToNextActivity(context)
            (context as? ComponentActivity)?.finish()
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

            PermissionCard(
                title = "DND Access",
                description = "Allows automatic Do Not Disturb mode",
                granted = dndAccessGranted,
                onRequest = {
                    val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                    context.startActivity(intent)
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
//                    navigateToNextActivity(context)
                    (context as? ComponentActivity)?.finish()
                },
                enabled = locationGranted && notificationGranted,
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

@Composable
fun PermissionCard(
    title: String,
    description: String,
    granted: Boolean,
    onRequest: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (!granted) {
                Button(
                    onClick = onRequest,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Grant Access", color = MaterialTheme.colorScheme.onPrimary)
                }
            } else {
                Text(
                    text = "✅ Permission Granted",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}

//private fun navigateToNextActivity(context: Context) {
//    val intent = Intent(context, MapsActivity::class.java)
//    context.startActivity(intent)
//}

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
