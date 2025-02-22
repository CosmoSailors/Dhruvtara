package com.grasstools.tangential.services

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.location.Location
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.grasstools.tangential.DruvTaraApplication
import com.grasstools.tangential.repository.LocationTrigger
import com.grasstools.tangential.repository.LocationTriggerRepository

class DruvTaraService : Service() {
    private val TAG = "DruvTaraService"

    private val locationProvider = LocationServices.getFusedLocationProviderClient(DruvTaraApplication.getContext()!!)
    private val locationTriggerRepository = LocationTriggerRepository()

    private val handler = Handler(Looper.getMainLooper())
    private val pollLocation = object: Runnable {
        override fun run() {
            if (ActivityCompat.checkSelfPermission(
                    DruvTaraApplication.getContext()!!,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    DruvTaraApplication.getContext()!!,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(DruvTaraApplication.getContext(), "Not enough permission", Toast.LENGTH_SHORT).show()
                // TODO: Request more permission
                return
            }

            locationProvider.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener { location ->
                    Log.i(TAG, "Location: ${location.latitude}, ${location.longitude}")
                    for (trigger in locationTriggerRepository.getAllLocationTriggers()) {
                        Log.i(TAG, "Distance to trigger ${trigger.name}: ${distanceToTrigger(location.latitude, location.longitude, trigger)}")
                    }
                }

            handler.postDelayed(this, 5000)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun startForeground() {
        try {
            val notification = NotificationCompat.Builder(this, "SERVICE_CHAN")
                .build()
            ServiceCompat.startForeground(
                this,
                100,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
            )
        } catch (e: Exception) {
            Log.e(TAG, "onBind: ", e)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground()
        handler.post(pollLocation)
        return super.onStartCommand(intent, flags, startId)
    }


    fun distanceToTrigger(latitude: Double, longitude: Double, trigger: LocationTrigger): Float {
        var currentLocation = Location("current")
        currentLocation.latitude = latitude
        currentLocation.longitude = longitude

        var triggerLocation = Location("trigger")
        triggerLocation.latitude = trigger.latitude
        triggerLocation.longitude = trigger.longitude

        return currentLocation.distanceTo(triggerLocation)
    }
}