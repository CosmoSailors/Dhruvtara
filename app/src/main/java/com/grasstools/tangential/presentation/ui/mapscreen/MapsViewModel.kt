package com.grasstools.tangential.presentation.ui.mapscreen

import android.view.View
import androidx.lifecycle.ViewModel
import com.grasstools.tangential.data.db.LocationDao
import com.grasstools.tangential.domain.model.LocationTriggers

class MapsViewModel(
    private val dao: LocationDao
): ViewModel(){


    suspend fun insertLocationTrigger(locationTrigger: LocationTriggers) {

        dao.insertLocation(locationTrigger)

//        TODO("handle success or failure")
    }


}