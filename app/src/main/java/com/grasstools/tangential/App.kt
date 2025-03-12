package com.grasstools.tangential

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.grasstools.tangential.data.db.TangentialDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    companion object {
        @Volatile
        private lateinit var instance: App

        fun getInstance(): App = instance

        fun getContext(): Context = instance.applicationContext
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
