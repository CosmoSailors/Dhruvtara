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
import com.grasstools.tangential.CustomApplication
import com.grasstools.tangential.presentation.ui.locationlist.ui.theme.TangentialTheme

data class LocationItem(val name: String, var isDndEnabled: Boolean)

class LocationListActivity : ComponentActivity() {
    private val database by lazy { (application as CustomApplication).database }

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
    var locations by remember {
        mutableStateOf(
            listOf<LocationItem>()
        )
           }
    LaunchedEffect(Unit) { // Ensures it runs only once
        vm.insertDummyData()
    }

    fun addLocation(name: String) {
        if (locations.none { it.name == name }) {
            locations = locations + LocationItem(name, true)
        }
    }

    addLocation("Home")


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        items(locations) { location ->
            LocationRow(
                location = location,
                onToggle = {
                    locations = locations.map {
                        if (it.name == location.name) it.copy(isDndEnabled = !it.isDndEnabled)
                        else it
                    }
                },onDelete = {

                    locations = locations.filter { it.name != location.name }
                }

            )
        }
    }
}

@Composable
fun LocationRow(location: LocationItem, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onToggle() },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = location.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = if (location.isDndEnabled) "DND Enabled" else "Tap to enable",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
        Switch(
            checked = location.isDndEnabled,
            onCheckedChange = { onToggle() }
        )
    }
}
@Composable
fun LocationRow(location: LocationItem, onToggle: () -> Unit, onDelete: () -> Unit) {
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


