package com.grasstools.tangential

import android.app.Application
import androidx.room.Room
import com.grasstools.tangential.data.db.TangentialDatabase

class CustomApplication: Application() {
    val database by lazy {
        Room.databaseBuilder(
            context = applicationContext, // Use applicationContext
            klass = TangentialDatabase::class.java,
            name = "tangential.db"
        ).build()
    }
}