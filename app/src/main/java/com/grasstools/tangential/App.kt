package com.grasstools.tangential

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.grasstools.tangential.data.db.TangentialDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {
    companion object {
        private var applicationInstance: App? = null

        fun getInstance(): App? {
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