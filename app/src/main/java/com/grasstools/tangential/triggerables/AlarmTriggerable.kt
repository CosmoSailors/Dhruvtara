package com.grasstools.tangential.triggerables

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import com.grasstools.tangential.App
import com.grasstools.tangential.presentation.ui.alarmscreen.AlarmScreen

object AlarmTriggerable: Triggerable {
    override fun onEntry(config: String) {
        Log.i("LOL", "Hey?")
        val intent = Intent(App.getContext()!!, AlarmScreen::class.java) // Replace NextActivity
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK + FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(App.getContext()!!, intent, null)
    }

    override fun onExit(config: String) {
        // TODO("Not yet implemented")
    }
}