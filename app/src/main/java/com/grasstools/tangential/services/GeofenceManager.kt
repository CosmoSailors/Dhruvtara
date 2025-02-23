package com.grasstools.tangential.services

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.location.Location
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.grasstools.tangential.DruvTaraApplication
import com.grasstools.tangential.domain.model.Geofence
import com.grasstools.tangential.domain.model.GeofenceType
import com.grasstools.tangential.triggerables.*
import java.util.Timer
import java.util.TimerTask


class GeofenceManager : Service() {
    fun register(geofence: Geofence) {
        taggedGeofences.add(TaggedGeofence(false, geofence))
    }

    fun register(geofences: List<Geofence>) {
        for (geofence in geofences) {
            register(geofence)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground()
        scheduleLocationPolling()

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel() // Always cancel your timer to prevent leaks.
    }

    private val POLL_RATE: Long = 5*1000
    private var timer: Timer? = null
    private val taggedGeofences = mutableListOf<TaggedGeofence>(
        TaggedGeofence(false, Geofence(
            name = "Yo mama is here",
            latitude = 37.40948,
            longitude = -121.49235,
            radius = 500.0f,
            type = GeofenceType.DND,
            config = "null",
            enabled = true
        )
    ))

    private fun startForeground() {
        try {
            val notification = NotificationCompat
                .Builder(this, "SERVICE_CHAN")
                .build()
            ServiceCompat.startForeground(
                this,
                3701,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
            )
        } catch (e: Exception) {
            Log.e("GeofenceManager", "onBind: ", e)
        }
    }

    private fun scheduleLocationPolling() {
        timer = Timer()
        Log.i("GeofenceManagerService", "Polling location...")
        timer?.schedule(object : TimerTask() {
            override fun run() {
                locationProvider
                    .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener { location ->
                        Log.i("GeofenceManagerService", "location is (${location.latitude},${location.longitude})")
                        for (tagged_geofence in taggedGeofences) {
                            val geofence = tagged_geofence.geofence;
                            val triggerable = when (geofence.type) {
                                GeofenceType.DND -> DNDTrigerable
                                GeofenceType.Alarm -> AlarmTriggerable
                            }
                            if (location.isInside(geofence) && !tagged_geofence.tag) {
                                tagged_geofence.tag = true
                                triggerable.onEntry(geofence.config)
                            } else if (!location.isInside(geofence) && tagged_geofence.tag) {
                                tagged_geofence.tag = false
                                triggerable.onExit(geofence.config)
                            }
                        }
                    }
            }
        }, 0, POLL_RATE)
    }

    private val locationProvider = LocationServices.getFusedLocationProviderClient(
        DruvTaraApplication.getContext()!!)

    data class TaggedGeofence(
        var tag: Boolean,
        val geofence: Geofence
    )

    private fun Location.isInside(geofence: Geofence): Boolean{
        val x = distanceToGeofence(this, geofence)
        Log.i("GeofenceManagerService", "isInside: $x")
        return x < geofence.radius
    }

    private fun distanceToGeofence(currentLocation: Location, geofence: Geofence): Float {
        var geofenceLocation = Location("geofence")
        geofenceLocation.latitude = geofence.latitude
        geofenceLocation.longitude = geofence.longitude
        return currentLocation.distanceTo(geofenceLocation)
    }
}