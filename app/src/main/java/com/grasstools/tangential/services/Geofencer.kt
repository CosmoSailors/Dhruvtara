package com.grasstools.tangential.services

import android.location.Location

enum class GeofenceType {
    DND,
    Alarm
}

data class Geofence (
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val radius: Float,
    val type: GeofenceType,
    val config: String
)