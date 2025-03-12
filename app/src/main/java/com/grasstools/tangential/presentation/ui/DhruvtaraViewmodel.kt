package com.grasstools.tangential.presentation.ui

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.accompanist.permissions.rememberPermissionState
import com.grasstools.tangential.domain.model.Geofence
import com.grasstools.tangential.repositories.GeofenceRepository
import com.grasstools.tangential.services.GeofenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DhruvtaraViewmodel
@Inject constructor(
    private val repository: GeofenceRepository,
    var geofenceManager: GeofenceManager
): ViewModel() {

    private val _allGeofence = MutableStateFlow<List<Geofence>>(emptyList())
    val allGeofence: StateFlow<List<Geofence>> = _allGeofence.asStateFlow()

    suspend fun getAllGeofence() = repository.getAllGeofenceFromDB()

    var geofenceManagerBound = mutableStateOf(false)

    val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as GeofenceManager.LocalBinder
            geofenceManager = binder.getService()
            geofenceManagerBound.value = true

            if (!geofenceManager.isInit) {
                CoroutineScope(Dispatchers.IO).launch {
                    for (geofence in allGeofence.value) {
                        geofenceManager.register(geofence)
                    }
                    geofenceManager.isInit = true
                }
            } else {
                Log.i("LOL", "Already init")
            }
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            geofenceManager.clear()
            geofenceManagerBound.value = false
        }
    }

}