package com.grasstools.tangential.presentation.ui.locationlist


import android.os.Bundle
import android.util.Log
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

data class LocationItem(val name: String, var isDndEnabled: Boolean)

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
        Log.i("asv", database.toString())
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
        shape = RoundedCornerShape(24.dp), // Sets the border radius
        modifier =  Modifier.fillMaxWidth()
            .padding(4.dp) ,
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)


    ) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)

    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onExpand(geofence.id.toString()) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = geofence.name,
                    fontSize = 24.sp,
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                )

                IconButton(onClick = { onExpand(geofence.id.toString()) }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand/Collapse",
                    )
                }

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onExpand(geofence.id.toString()) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = geofence.type.toString(),
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f),

                    )


                Switch(
                    checked = geofence.enabled,
                    onCheckedChange = { onToggle() }
                )
            }
        }

        if (isExpanded) {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {

                Column (Modifier.weight(1f)){
                    Text(
                        text = "Latitude: ${geofence.latitude}",
                        fontSize = 14.sp,

                    )
                    Text(
                        text = "Longitude: ${geofence.longitude}",
                        fontSize = 14.sp,

                    )
                    Text(
                        text = "Radius: ${geofence.radius}",
                        fontSize = 14.sp,

                        )
                }



                IconButton(onClick = { onDelete() }, Modifier.align(
                    Alignment.Bottom
                )) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.Gray,
                        modifier = Modifier.size(36.dp).align(
                             Alignment.CenterVertically
                        )
                    )
                }
            }


        }
    }}
}