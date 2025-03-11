package com.grasstools.tangential.presentation.ui.alarmscreen

import android.content.Context
import android.media.MediaPlayer
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grasstools.tangential.R

@Composable
fun AlarmScreen(
    onNavigateToMaps: () -> Unit
) {
    val context = LocalContext.current
    val mediaPlayer = remember { MediaPlayer.create(context, R.raw.ringtone2) }
    val vibrator = remember { context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator }

    LaunchedEffect(Unit) {
        mediaPlayer.isLooping = true
        mediaPlayer.start()

        val vibrationEffect =
            VibrationEffect.createWaveform(longArrayOf(0, 500, 500), 0)
        vibrator.vibrate(vibrationEffect)
    }

    DisposableEffect(Unit) {
        onDispose {
            stopAlarm(mediaPlayer, vibrator)
        }
    }

    AlarmUI(onDismiss = onNavigateToMaps)
}

fun stopAlarm(mediaPlayer: MediaPlayer?, vibrator: Vibrator?) {
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
