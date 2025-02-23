package com.grasstools.tangential

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.grasstools.tangential.presentation.ui.PermissionsManager
import com.grasstools.tangential.services.GeofenceManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // splash screen
        val splashscreen = installSplashScreen()
        var keepSplashScreen = true
        splashscreen.setKeepOnScreenCondition { keepSplashScreen }
        lifecycleScope.launch {
            delay(1000)
            keepSplashScreen = false
        }

        // create a notification channel, TODO: put this elsewhere?
        val notifChannel = NotificationChannel(
            "SERVICE_CHAN",
            getString(R.string.notif_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notifChannel.description = getString(R.string.notif_channel_desc)
        val notificationManager = getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notifChannel)

        navigateToNextActivity()

        // launch service
        val intent = Intent(this, GeofenceManager::class.java)
        this.startForegroundService(intent)
    }

    private fun navigateToNextActivity() {
        val intent = Intent(this, PermissionsManager::class.java) // Replace NextActivity
        startActivity(intent)
    }
}
