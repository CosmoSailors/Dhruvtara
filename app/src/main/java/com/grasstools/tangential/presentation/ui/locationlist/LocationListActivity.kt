package com.grasstools.tangential.presentation.ui.locationlist


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
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
import androidx.lifecycle.viewModelScope
import com.grasstools.tangential.DruvTaraApplication
import com.grasstools.tangential.domain.model.LocationTriggers
import com.grasstools.tangential.presentation.ui.locationlist.ui.theme.TangentialTheme
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.ui.text.font.FontWeight

data class LocationItem(val name: String, var isDndEnabled: Boolean)

class LocationListActivity : ComponentActivity() {
    private val database by lazy { (application as DruvTaraApplication).database }

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
    val locationsList by vm.getAllRecords().collectAsState(initial = emptyList())
    var expandedLocationId by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(locationsList) { location ->
            LocationRow(
                location = location,
                expandedLocationId = expandedLocationId,
                onToggle = { vm.updateDndStatus(location) },
                onDelete = { vm.deleteLocation(location) },
                onExpand = { id -> expandedLocationId = if (expandedLocationId == id) null else id }
            )
        }
    }
}

@Composable
fun LocationRow(
    location: LocationTriggers,
    expandedLocationId: String?,
    onToggle: () -> Unit,
    onDelete: () -> Unit,
    onExpand: (String) -> Unit
) {
    val isExpanded = location.id.toString() == expandedLocationId
    Card(
        modifier =  Modifier.fillMaxWidth()
            .padding(4.dp)
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
                    .clickable { onExpand(location.id.toString()) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = location.name,
                    fontSize = 24.sp,
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                )

                IconButton(onClick = { onExpand(location.id.toString()) }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand/Collapse",
                    )
                }

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onExpand(location.id.toString()) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Toggle Alert",
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f),

                    )


                Switch(
                    checked = location.isDndEnabled,
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
                        text = "Latitude: ${location.latitude}",
                        fontSize = 14.sp,

                    )
                    Text(
                        text = "Longitude: ${location.longitude}",
                        fontSize = 14.sp,

                    )
                    Text(
                        text = "Radius: ${location.radius}",
                        fontSize = 14.sp,

                        )
                }



                IconButton(onClick = { onDelete() }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.Red,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }


        }
    }}
}