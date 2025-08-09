package com.wilson.smartexpensetracker.di

import com.wilson.smartexpensetracker.data.util.CsvExporter
import com.wilson.smartexpensetracker.data.util.PdfExporter
import com.wilson.smartexpensetracker.data.util.SyncManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilsModule {

    @Provides
    @Singleton
    fun provideCsvExporter(): CsvExporter = CsvExporter

    @Provides
    @Singleton
    fun providePdfExporter(): PdfExporter = PdfExporter

    @Provides
    @Singleton
    fun provideSyncManager(): SyncManager = SyncManager()
}
