package com.grasstools.tangential

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.grasstools.tangential.data.db.TangentialDatabase

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

    val database by lazy {
        Room.databaseBuilder(
            context = applicationContext, // Use applicationContext
            klass = TangentialDatabase::class.java,
            name = "tangential.db"
        ).build()
    }

    override fun onCreate() {
        super.onCreate()
        applicationInstance = this
    }
}