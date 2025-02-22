package com.grasstools.tangential

import android.app.Application
import android.content.Context

class DruvTaraApplication: Application() {
    companion object {
        private var applicationInstance: DruvTaraApplication? = null

        fun getInstance(): DruvTaraApplication? {
            return applicationInstance
        }

        fun getContext(): Context? {
            return applicationInstance?.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        applicationInstance = this
    }
}