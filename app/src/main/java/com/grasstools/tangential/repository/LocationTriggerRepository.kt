package com.grasstools.tangential.repository

data class LocationTrigger(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val radius: Float
)

class LocationTriggerRepository {
    fun getAllLocationTriggers(): List<LocationTrigger> {
        return listOf(
            LocationTrigger("test",12.9131445, 77.6363384, 20.0f)
        )
    }
}