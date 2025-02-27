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
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.grasstools.tangential.presentation.ui.DhruvtaraScreen
import com.grasstools.tangential.services.GeofenceManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    private var geofenceManager by mutableStateOf<GeofenceManager?>(null)
    private var geofenceManagerBound: Boolean = false
    private val database by lazy { (application as App).database }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as GeofenceManager.LocalBinder
            geofenceManager = binder.getService() // Update state
            geofenceManagerBound = true

            if (!geofenceManager!!.isInit) {
                CoroutineScope(Dispatchers.IO).launch {
                    for (geofence in database.dao().getAllGeofencesSnapshot()) {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val splashscreen = installSplashScreen()
        var keepSplashScreen = true
        splashscreen.setKeepOnScreenCondition { keepSplashScreen }
        lifecycleScope.launch {
            delay(1000)
            keepSplashScreen = false
        }

        setContent {
            geofenceManager?.let { manager ->
                DhruvtaraScreen(geofenceManager = manager)
            } ?: Text("Loading...")
        }

        val notifChannel = NotificationChannel(
            "SERVICE_CHAN",
            getString(R.string.notif_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notifChannel.description = getString(R.string.notif_channel_desc)
        val notificationManager = getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notifChannel)

        val intent = Intent(this, GeofenceManager::class.java)
        this.startForegroundService(intent)
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
}