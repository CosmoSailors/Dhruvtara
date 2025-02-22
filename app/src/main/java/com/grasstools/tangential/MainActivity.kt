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
import com.grasstools.tangential.data.db.TangentialDatabase
import com.grasstools.tangential.presentation.ui.mapscreen.MapsActivity
import com.grasstools.tangential.services.DruvTaraService
import com.grasstools.tangential.ui.theme.TangentialTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.room.Room

class MainActivity : ComponentActivity() {



    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { it.value }) {
            navigateToNextActivity()
        } else {
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

        val notifChannel = NotificationChannel(
            "SERVICE_CHAN",
            getString(R.string.notif_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notifChannel.description = getString(R.string.notif_channel_desc)
        val notificationManager = getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notifChannel)

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
                navigateToNextActivity()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {

                locationPermissionRequest.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
            }
            else -> {
                locationPermissionRequest.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
            }
        }
    }

    private fun navigateToNextActivity() {
        val intent = Intent(this, MapsActivity::class.java) // Replace NextActivity
        startActivity(intent)
    }
}

