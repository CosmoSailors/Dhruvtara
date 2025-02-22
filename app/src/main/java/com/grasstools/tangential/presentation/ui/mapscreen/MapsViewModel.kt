package com.grasstools.tangential.presentation.ui.mapscreen

import android.util.Log
import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.grasstools.tangential.data.db.LocationDao
import com.grasstools.tangential.domain.model.LocationTriggers

class MapsViewModel(
    private val dao: LocationDao
): ViewModel(){

    var sliderPosition by mutableFloatStateOf(0f)
        private set

    fun updateSliderPosition(value: Float) {
        sliderPosition = value
        Log.i("i", "Slider position updated: $value")
    }
    suspend fun insertLocationTrigger(locationTrigger: LocationTriggers) {

        dao.insertLocation(locationTrigger)

//        TODO("handle success or failure")
    }


}