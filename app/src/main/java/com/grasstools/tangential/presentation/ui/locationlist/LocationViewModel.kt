package com.grasstools.tangential.presentation.ui.locationlist

import androidx.lifecycle.ViewModel
import com.grasstools.tangential.data.db.LocationDao
import com.grasstools.tangential.domain.model.LocationTriggers
import kotlinx.coroutines.flow.Flow

class LocationViewModel(
    private val dao: LocationDao
): ViewModel() {

    suspend fun insertDummyData() {
        val dummyLocations = listOf(
            LocationTriggers(name = "Home", latitude = 12.9716, longitude = 79.1581, isDndEnabled = false, radius = 5.5),
            LocationTriggers(name = "Office", latitude = 12.9229, longitude = 79.1044, isDndEnabled = true, radius = 5.5),
            LocationTriggers(name = "Park", latitude = 13.0045, longitude = 79.1256, isDndEnabled = false, radius = 5.5),
            LocationTriggers(name = "Restaurant", latitude = 12.9876, longitude = 79.1123, isDndEnabled = true, radius = 5.5),
            LocationTriggers(name = "Gym", latitude = 12.9567, longitude = 79.1345, isDndEnabled = false, radius = 5.5)
        )
        dao.insertLocation(dummyLocations.get(0))


    }

    fun getAllRecords(): Flow<List<LocationTriggers>> {
        return dao.getAllLocations()
    }
}