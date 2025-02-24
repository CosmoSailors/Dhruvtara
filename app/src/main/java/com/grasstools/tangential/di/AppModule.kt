package com.grasstools.tangential.di

import android.content.Context
import androidx.room.Room
import com.grasstools.tangential.data.db.GeofenceDao
import com.grasstools.tangential.data.db.TangentialDatabase
import com.grasstools.tangential.domain.repository.GeofenceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGeofenceDatabase(@ApplicationContext context: Context): TangentialDatabase =
        Room.databaseBuilder(context, TangentialDatabase::class.java, "tangential_database")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideGeofenceDao(database: TangentialDatabase) = database.dao()

    @Provides
    fun provideGeofenceRepository(dao: GeofenceDao): GeofenceRepository = GeofenceRepository(dao)
}