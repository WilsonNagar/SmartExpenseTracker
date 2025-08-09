package com.wilson.smartexpensetracker.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppContext(app: android.app.Application): Context {
        return app.applicationContext
    }

    // Add other shared singletons here (e.g., network client, data store, etc.)
}
