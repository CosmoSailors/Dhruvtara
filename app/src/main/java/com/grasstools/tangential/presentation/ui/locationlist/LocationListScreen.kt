package com.grasstools.tangential.presentation.ui.locationlist

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.grasstools.tangential.presentation.ui.locationlist.components.LocationRow
import com.grasstools.tangential.services.GeofenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LocationListScreen(
    onNavigateToAlert: () -> Unit,
    onNavigationToMaps: () -> Unit,
    geofenceManager: GeofenceManager
) {

    val viewModel: LocationViewModel = viewModel()

    val geofencesList by viewModel.getAllRecords().collectAsState(initial = emptyList())
    var expandedGeofenceId by remember { mutableStateOf<String?>(null) }

    fun resync() {
        CoroutineScope(Dispatchers.IO).launch {
            geofenceManager.clear()
            geofenceManager.register(viewModel.dao.getAllGeofencesSnapshot())
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(geofencesList) { geofence ->
            LocationRow(
                geofence = geofence,
                expandedGeofenceId = expandedGeofenceId,
                onToggle = {
                    viewModel.toggleEnabled(geofence)
                    resync()
                },
                onDelete = {
                    viewModel.deleteGeofence(geofence)
                    resync()
                },
                onExpand = { id -> expandedGeofenceId = if (expandedGeofenceId == id) null else id }
            )
        }
    }
}