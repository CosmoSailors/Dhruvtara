package com.grasstools.tangential.modules

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.grasstools.tangential.data.LocalDataSource
import com.grasstools.tangential.data.RoomLocalDataSource
import com.grasstools.tangential.data.db.GeofenceDao
import com.grasstools.tangential.data.db.TangentialDatabase
import com.grasstools.tangential.repositories.GeofenceRepository
import com.grasstools.tangential.repositories.GeofenceRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): TangentialDatabase {
        return Room.databaseBuilder(
            application.applicationContext,
            TangentialDatabase::class.java,
            "tangential.db"
        ).build()
    }

    @Provides
    fun provideGeofenceDao(appDatabase: TangentialDatabase): GeofenceDao {
        return appDatabase.dao()
    }

    @Provides
    fun provideLocalDataSource(geofenceDao: GeofenceDao): LocalDataSource {
        return RoomLocalDataSource(geofenceDao)
    }

    @Provides
    fun provideGeofenceRepository(localDataSource: LocalDataSource): GeofenceRepository {
        return GeofenceRepositoryImpl(localDataSource)
    }
}