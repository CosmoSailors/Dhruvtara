package com.grasstools.tangential.presentation.ui.alarmscreen

import android.content.Context
import android.media.MediaPlayer
import android.os.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grasstools.tangential.R
import com.grasstools.tangential.presentation.ui.alarmscreen.ui.theme.TangentialTheme

class AlarmScreen : ComponentActivity() {
    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mediaPlayer = MediaPlayer.create(this, R.raw.mustard)
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()

        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val vibrationEffect = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            VibrationEffect.createWaveform(longArrayOf(0, 500, 500), 0)
        } else {
            VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
        }
        vibrator?.vibrate(vibrationEffect)

        setContent {
            TangentialTheme {
                AlarmUI(onDismiss = { stopAlarmAndFinish() })
            }
        }
    }

    private fun stopAlarmAndFinish() {
        stopAlarm(mediaPlayer, vibrator)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAlarm(mediaPlayer, vibrator)
    }
}

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
            .background(Color(0xFF1E1E1E)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.alarm),
                contentDescription = "Alarm Icon",
                modifier = Modifier.size(120.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Alarm Triggered!",
                fontSize = 32.sp,
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(18.dp))
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text(text = "Dismiss", fontSize = 18.sp, color = Color.White)
            }
        }
    }
}

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
                onDismiss = {}
            )
        }
    }
}
