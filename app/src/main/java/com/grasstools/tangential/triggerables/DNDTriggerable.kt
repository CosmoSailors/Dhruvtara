package com.grasstools.tangential.triggerables

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.grasstools.tangential.DruvTaraApplication

object DNDTrigerable: Triggerable {
    override fun onEntry(config: String) {
        val context = DruvTaraApplication.getContext()
        if (context != null) {
            setDoNotDisturb(context)
        }
    }

    override fun onExit(config: String) {
        val context = DruvTaraApplication.getContext()
        if (context != null) {
            clearDoNotDisturb(context)
        }
    }
}

private fun setDoNotDisturb(context: Context) {
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE)
    }
}

fun clearDoNotDisturb(context: Context) {
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
    }
}