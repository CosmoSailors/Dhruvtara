package com.grasstools.tangential.triggerables

import android.util.Log
import com.grasstools.tangential.triggerables.Triggerable

object DNDTrigerable: Triggerable {
    override fun onEntry(config: String) {
        Log.i("Triggered", "We Enter")
    }

    override fun onExit(config: String) {
        Log.i("Triggered", "We Exit from Rome")
    }
}