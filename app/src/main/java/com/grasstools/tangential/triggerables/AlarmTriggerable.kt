package com.grasstools.tangential.triggerables

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import com.grasstools.tangential.App
import com.grasstools.tangential.presentation.ui.AlertScreen
import com.grasstools.tangential.presentation.ui.alarmscreen.AlarmScreen
import com.grasstools.tangential.utils.NavControllerHolder

object AlarmTriggerable: Triggerable {
    override fun onEntry(config: String) {
        NavControllerHolder.navController?.navigate(AlertScreen)

    }

    override fun onExit(config: String) {
        // TODO("Not yet implemented")
    }
}