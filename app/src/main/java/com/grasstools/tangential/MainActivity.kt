package com.grasstools.tangential

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.grasstools.tangential.ui.theme.TangentialTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainActivity : ComponentActivity() {

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { it.value }) {
            // All permissions granted, navigate to next activity
            navigateToNextActivity()
        } else {
            // Handle permission denial (e.g., show a message)
            // You might want to request permissions again or explain why they are needed.
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        checkAndRequestPermissions()
        setContent {
            TangentialTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        modifier = Modifier.padding(innerPadding),
                        onButtonClick = {  } // Pass the check function
                    )
                }
            }
        }
    }


    private fun checkAndRequestPermissions() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permission is already granted
                navigateToNextActivity()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                // Show rationale if needed (optional)
                // ... explain why the permission is needed ...

                // Then request the permission
                locationPermissionRequest.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
            }
            else -> {
                // Request the permission
                locationPermissionRequest.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
            }
        }
    }

    private fun navigateToNextActivity() {
        val intent = Intent(this, MapsActivity::class.java) // Replace NextActivity
        startActivity(intent)
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier, onButtonClick: () -> Unit) {  // Add a parameter
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }
    var locationInfo by remember {
        mutableStateOf("")
    }
    Button(
        modifier = modifier,
        onClick = {
            onButtonClick() // Call the passed function to check permissions

            scope.launch(Dispatchers.IO) {
                // ... (rest of your location code)
            }
        }
    ) {
        Text("Click")
    }
    Text(
        text = locationInfo,
        modifier = modifier
    )
}

// ... (Preview remains the same)