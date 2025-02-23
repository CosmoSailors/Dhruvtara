package com.grasstools.tangential

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.grasstools.tangential.presentation.ui.PermissionsManager
import com.grasstools.tangential.services.GeofenceManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class MainActivity : ComponentActivity() {
    private lateinit var geofenceManager: GeofenceManager
    private var geofenceManagerBound: Boolean = false
    private val database by lazy { (application as App).database }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as GeofenceManager.LocalBinder
            geofenceManager = binder.getService()
            geofenceManagerBound = true

            if (!geofenceManager.isInit) {
                CoroutineScope(Dispatchers.IO).launch {
                    for (geofence in database.dao().getAllGeofencesSnapshot()) {
                        geofenceManager.register(geofence)
                    }
                    geofenceManager.isInit = true
                }
            } else {
                Log.i("LOL", "Already init")
            }
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            geofenceManagerBound = false
        }
    }

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

        Log.i("MainActivity", "Creating service...")
        // launch service
        val intent = Intent(this, GeofenceManager::class.java)
        this.startForegroundService(intent)

        navigateToNextActivity()
    }

    override fun onStart() {
        super.onStart()
        Intent(this, GeofenceManager::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }

    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
    }

    private fun navigateToNextActivity() {
        val intent = Intent(this, PermissionsManager::class.java) // Replace NextActivity
        startActivity(intent)
    }
}
