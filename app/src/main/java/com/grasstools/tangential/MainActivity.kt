package com.grasstools.tangential

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.LocationServices
import com.grasstools.tangential.presentation.ui.mapscreen.MapsActivity
import com.grasstools.tangential.services.DruvTaraService
import com.grasstools.tangential.ui.theme.TangentialTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { it.value }) {
            // All permissions granted, navigate to next activity
            navigateToNextActivity()
        } else {
            // Handle permission denial (e.g., show a message)
            // You might want to request permissions again or explain why they are needed.
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        val splashscreen = installSplashScreen()
        var keepSplashScreen = true
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        checkAndRequestPermissions()
        splashscreen.setKeepOnScreenCondition { keepSplashScreen }
        lifecycleScope.launch {
            delay(1000)
            keepSplashScreen = false
        }

        // create a notification channel
        val notifChannel = NotificationChannel(
            "SERVICE_CHAN",
            getString(R.string.notif_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notifChannel.description = getString(R.string.notif_channel_desc)
        val notificationManager = getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notifChannel)

        // launch service
        val intent = Intent(this, DruvTaraService::class.java)
        this.startForegroundService(intent)

        enableEdgeToEdge()
        setContent {
            TangentialTheme {

            }
        }
    }


    private fun checkAndRequestPermissions() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permission is already granted
                navigateToNextActivity()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                // Show rationale if needed (optional)
                // ... explain why the permission is needed ...

                // Then request the permission
                locationPermissionRequest.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
            }
            else -> {
                // Request the permission
                locationPermissionRequest.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
            }
        }
    }

    private fun navigateToNextActivity() {
        val intent = Intent(this, MapsActivity::class.java) // Replace NextActivity
        startActivity(intent)
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier, onButtonClick: () -> Unit) {  // Add a parameter
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }
    var locationInfo by remember {
        mutableStateOf("")
    }
    Button(
        modifier = modifier,
        onClick = {
            onButtonClick() // Call the passed function to check permissions

            scope.launch(Dispatchers.IO) {
                // ... (rest of your location code)
            }
        }
    ) {
        Text("Click")
    }
    Text(
        text = locationInfo,
        modifier = modifier
    )
}

// ... (Preview remains the same)