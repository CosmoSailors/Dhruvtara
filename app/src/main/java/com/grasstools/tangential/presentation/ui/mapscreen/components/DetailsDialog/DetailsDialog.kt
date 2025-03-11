import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.grasstools.tangential.domain.model.GeofenceType
import com.grasstools.tangential.presentation.ui.mapscreen.MapsViewModel
import com.grasstools.tangential.presentation.ui.mapscreen.components.DetailsDialog.DialogContent
import com.grasstools.tangential.presentation.ui.mapscreen.components.DetailsDialog.DialogHeader
import javax.inject.Inject

@Composable
fun DetailsDialog(
    onDismissRequest: () -> Unit,
    onLocationAdded: (String) -> Unit,
    vm: MapsViewModel = hiltViewModel()

) {
    var locationName by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                DialogHeader(onDismissRequest, onLocationAdded, locationName)
                DialogContent(
                    locationName = locationName,
                    onLocationNameChange = { locationName = it },
                )
            }
        }
    }
}





