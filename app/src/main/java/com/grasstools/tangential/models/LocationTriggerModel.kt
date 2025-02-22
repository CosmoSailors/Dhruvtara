package com.grasstools.tangential.models

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.grasstools.tangential.DruvTaraApplication
import com.grasstools.tangential.broadcast_recievers.LocationTriggerBroadcastReceiver
import java.io.Serializable

data class LocationTrigger(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val radius: Float
) : Serializable

class LocationTriggerModel() {
    private val context = DruvTaraApplication.getContext()!!
    private var geofencingClient: GeofencingClient = LocationServices.getGeofencingClient(context)
    private var locationTriggerList: MutableList<LocationTrigger> = mutableListOf()

    fun addLocationTrigger(locationTrigger: LocationTrigger, callback: (Boolean) -> Unit) {
        locationTriggerList.add(locationTrigger)
        val geofencingRequest = GeofencingRequest.Builder()
            .addGeofence(Geofence.Builder()
                .setRequestId(locationTrigger.name)
                .setCircularRegion(locationTrigger.latitude,
                                   locationTrigger.longitude,
                                   locationTrigger.radius)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or
                                    Geofence.GEOFENCE_TRANSITION_EXIT)
                .build()).setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .build()

        val intent = Intent(DruvTaraApplication.getContext(), LocationTriggerBroadcastReceiver::class.java)
        intent.putExtra("locationTrigger", locationTrigger)
        val geofencingPendingIndent = PendingIntent.getBroadcast(DruvTaraApplication.getContext(), 0, intent, PendingIntent.FLAG_MUTABLE)

        if (ActivityCompat.checkSelfPermission(
                DruvTaraApplication.getContext()!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            callback(false)
            return
        }
        geofencingClient.addGeofences(geofencingRequest, geofencingPendingIndent).run {
            addOnSuccessListener {
                callback(true)
            }
            addOnFailureListener { err ->
                Log.e("LocationTriggerModel", "Error adding geofence: ${err}")
                callback(false)
            }
        }
    }
}