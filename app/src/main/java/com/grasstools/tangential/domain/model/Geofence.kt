package com.grasstools.tangential.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

//@Entity(tableName = "locations")
//data class LocationTriggers(
//
//    @PrimaryKey(autoGenerate = true)
//    val id: Int = 0,
//    val name: String,
//    val latitude: Double,
//    val longitude: Double,
//    val isDndEnabled: Boolean,
//    val radius: Double
//
//)

enum class GeofenceType {
    DND,
    Alarm
}

@Entity(tableName = "geofences")
data class Geofence (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val radius: Float,
    val type: GeofenceType,
    val config: String,
    var enabled: Boolean
)
