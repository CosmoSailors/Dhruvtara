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
        // 🔹 Stylish Settings Button
        IconButton(
            onClick = { expanded = true },
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = "Settings",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        // 🔹 Styled Dropdown Menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(220.dp)
                .padding(4.dp)
        ) {
            DropdownMenuItem(
                text = { Text("Show All Locations", style = MaterialTheme.typography.bodyMedium) },
                onClick = { expanded = false }, // Close dropdown when clicked
                trailingIcon = {
                    Switch(
                        checked = showAllMarkers,
                        onCheckedChange = { isChecked ->
                            onToggleShowAllMarkers(isChecked) // Update ViewModel
                        },
                        modifier = Modifier.graphicsLayer(scaleX = 0.8f, scaleY = 0.8f) // Make switch smaller
                    )
                }
            )
        }
    }
}
