package com.grasstools.tangential.presentation.ui.locationlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grasstools.tangential.data.db.LocationDao
import com.grasstools.tangential.domain.model.LocationTriggers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class LocationViewModel(
    private val dao: LocationDao
): ViewModel() {

    fun updateDndStatus(location: LocationTriggers) {
        viewModelScope.launch {
            dao.updateDndStatus(location.id, !location.isDndEnabled)
        }
    }

    fun deleteLocation(location: LocationTriggers) {
        viewModelScope.launch {
            dao.deleteLocationById(location.id)
        }
    }

    fun getAllRecords(): Flow<List<LocationTriggers>> {
        return dao.getAllLocations()
    }
}