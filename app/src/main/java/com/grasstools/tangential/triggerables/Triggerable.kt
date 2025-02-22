package com.grasstools.tangential.triggerables

interface Triggerable {
    fun onEntry(config: String)
    fun onExit(config: String)
}