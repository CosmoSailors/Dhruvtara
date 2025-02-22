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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.grasstools.tangential.DruvTaraApplication
import com.grasstools.tangential.domain.model.LocationTriggers
import com.grasstools.tangential.presentation.ui.locationlist.ui.theme.TangentialTheme
import com.grasstools.tangential.presentation.ui.mainscreen.viewmodel
import kotlinx.coroutines.launch

data class LocationItem(val name: String, var isDndEnabled: Boolean)

class LocationListActivity : ComponentActivity() {
    private val database by lazy { (application as DruvTaraApplication).database }

    private val viewModel by viewModels<LocationViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return LocationViewModel(database.dao()) as T // Use database from Application
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
    val locationsList by vm.getAllRecords().collectAsState(initial = emptyList()) // Collect Flow as State



    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        items(locationsList) { location ->
            LocationRow(
                location = location,
                onToggle = { vm.updateDndStatus(location) },
                onDelete = { vm.deleteLocation(location)
        }
            )
        }
    }
}


@Composable
fun LocationRow(location: LocationTriggers, onToggle: () -> Unit, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.DarkGray, shape = RoundedCornerShape(8.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = location.name,
            color = Color.White,
            fontSize = 18.sp,
            modifier = Modifier.weight(1f)
        )

        Switch(
            checked = location.isDndEnabled,
            onCheckedChange = { onToggle() }
        )

        IconButton(onClick = { onDelete() }) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
        }
    }
}


