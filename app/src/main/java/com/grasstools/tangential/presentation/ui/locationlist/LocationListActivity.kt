import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.grasstools.tangential.presentation.ui.locationlist.ui.theme.TangentialTheme

data class LocationItem(val name: String, var isDndEnabled: Boolean)

class LocationListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TangentialTheme {
                LocationListScreen()
            }
        }
    }
}

@Composable
fun LocationListScreen() {
    var locations by remember {
        mutableStateOf(
            listOf(
                LocationItem("Home", true),
                LocationItem("Work", false),
                LocationItem("Gym", false),
                LocationItem("Cafe", false),
                LocationItem("Library", false),
            )
        )
    }

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

@Preview(showBackground = true)
@Composable
fun LocationListPreview() {
    TangentialTheme {
        LocationListScreen()
    }
}
