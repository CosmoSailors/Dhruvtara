package com.grasstools.tangential.broadcast_recievers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import com.grasstools.tangential.DruvTaraApplication
import com.grasstools.tangential.models.LocationTrigger

class LocationTriggerBroadcastReceiver : BroadcastReceiver() {
    private val LOG_TAG = "LocationTriggerBroadcastReciever"
    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent == null) {
            Log.e(LOG_TAG, "geofencingEvent from intent was null?")
            return
        }

        if (geofencingEvent.hasError()) {
            val errorMessage = GeofenceStatusCodes
                .getStatusCodeString(geofencingEvent.errorCode)
            Log.e(LOG_TAG, errorMessage)
            return
        }

        val geofencingTransition = geofencingEvent.geofenceTransition;
        // TODO: Updoot this to use new APIs, this is here for getting stuff done for now.
        @Suppress("DEPRECATION") val locationTrigger = intent.getSerializableExtra("locationTrigger")!! as LocationTrigger

        if (geofencingTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            onEnterLocationTrigger(locationTrigger)
        } else if (geofencingTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            onExitLocationTrigger(locationTrigger)
        }
    }

    fun onEnterLocationTrigger(locationTrigger: LocationTrigger) {
        Log.i(LOG_TAG, "TriggerEnter for target " + locationTrigger.name)
        Toast.makeText(DruvTaraApplication.getContext(), "ENTER!!!", Toast.LENGTH_LONG).show()
    }

    fun onExitLocationTrigger(locationTrigger: LocationTrigger) {
        Log.i(LOG_TAG, "TriggerExit for target " + locationTrigger.name)
        Toast.makeText(DruvTaraApplication.getContext(), "EXIT!!!", Toast.LENGTH_LONG).show()
    }
}