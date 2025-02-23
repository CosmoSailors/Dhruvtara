package com.grasstools.tangential.presentation.ui.locationlist


import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.grasstools.tangential.App
import com.grasstools.tangential.presentation.ui.locationlist.ui.theme.TangentialTheme
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.ui.text.font.FontWeight
import com.grasstools.tangential.domain.model.Geofence

class LocationListActivity : ComponentActivity() {
    private val database by lazy { (application as App).database }

    private val viewModel by viewModels<LocationViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return LocationViewModel(database.dao()) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TangentialTheme {
                LocationListScreen(viewModel)
            }
        }
    }
}

@Composable
fun LocationListScreen(vm: LocationViewModel) {
    val geofencesList by vm.getAllRecords().collectAsState(initial = emptyList())
    var expandedGeofenceId by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(geofencesList) { geofence ->
            LocationRow(
                geofence = geofence,
                expandedGeofenceId = expandedGeofenceId,
                onToggle = { vm.toggleEnabled(geofence) },
                onDelete = { vm.deleteGeofence(geofence) },
                onExpand = { id -> expandedGeofenceId = if (expandedGeofenceId == id) null else id }
            )
        }
    }
}

@Composable
fun LocationRow(
    geofence: Geofence,
    expandedGeofenceId: String?,
    onToggle: () -> Unit,
    onDelete: () -> Unit,
    onExpand: (String) -> Unit
) {
    val isExpanded = geofence.id.toString() == expandedGeofenceId

    Card(
        shape = RoundedCornerShape(16.dp), // Softer rounded corners
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 🔹 Row with Title and Expand Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onExpand(geofence.id.toString()) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        text = geofence.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = geofence.type.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(onClick = { onExpand(geofence.id.toString()) }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand/Collapse",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // 🔹 Toggle and Divider
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (geofence.enabled) "Active" else "Disabled",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (geofence.enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.weight(1f))

                Switch(
                    checked = geofence.enabled,
                    onCheckedChange = { onToggle() }
                )
            }

            if (isExpanded) {
                Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f), thickness = 1.dp)

                Spacer(modifier = Modifier.height(8.dp))

                Column {
                    Text(
                        text = "Latitude: ${geofence.latitude}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Longitude: ${geofence.longitude}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Radius: ${geofence.radius}m",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier
                            .size(40.dp)
                            .padding(end = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}
