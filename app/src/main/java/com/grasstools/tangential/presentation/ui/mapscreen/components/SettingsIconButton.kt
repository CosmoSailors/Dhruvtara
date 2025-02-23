import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.flow.StateFlow

@Composable
fun SettingsIconButton(
    showAllMarkersFlow: StateFlow<Boolean>,
    onToggleShowAllMarkers: (Boolean) -> Unit
) {
    val showAllMarkers by showAllMarkersFlow.collectAsState()

    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Filled.Settings, contentDescription = "Settings")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(180.dp)
        ) {
            DropdownMenuItem(
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Show All Locations", modifier = Modifier.weight(1f))
                        Switch(
                            checked = showAllMarkers,
                            onCheckedChange = { isChecked ->
                                onToggleShowAllMarkers(isChecked) // Update ViewModel
                            },
                            modifier = Modifier.graphicsLayer(scaleX = 0.7f, scaleY = 0.7f)
                        )
                    }
                },
                onClick = { }
            )

        }
    }
}
