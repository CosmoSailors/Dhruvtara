package com.grasstools.tangential.presentation.ui.alarmscreen

import android.content.Context
import android.media.MediaPlayer
import android.os.*
import android.provider.Settings
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grasstools.tangential.presentation.ui.alarmscreen.ui.theme.TangentialTheme

class AlarmScreen : ComponentActivity() {
    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TangentialTheme {
                AlarmUI(onDismiss = { stopAlarmAndFinish() })
            }
        }
    }

    private fun stopAlarmAndFinish() {
        stopAlarm() // Stop sound & vibration
        finish() // Close activity
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAlarm()
    }
}

// 🔹 Function to Stop Alarm (Reusable in Preview Mode)
fun stopAlarm(mediaPlayer: MediaPlayer? = null, vibrator: Vibrator? = null) {
    mediaPlayer?.stop()
    mediaPlayer?.release()
    vibrator?.cancel()
}

@Composable
fun AlarmUI(onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
        contentAlignment = Alignment.Center

    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Alarm Triggered!", fontSize = 32.sp, color = Color.White)
            Spacer(modifier = Modifier.height(18.dp))
            Button(onClick = onDismiss) {
                Text(text = "Dismiss", fontSize = 18.sp)
            }
        }
    }
}

// 🔹 Preview Mode with Real Sound + Vibration
@Preview(showBackground = true)
@Composable
fun AlarmUIPreview() {

    TangentialTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AlarmUI(
                onDismiss = {
                }
            )
        }
    }
}
