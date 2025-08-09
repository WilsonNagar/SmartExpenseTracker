package com.wilson.smartexpensetracker.di

import android.content.Context
import androidx.room.Room
import com.wilson.smartexpensetracker.data.local.dao.ExpenseDao
import com.wilson.smartexpensetracker.data.local.ExpenseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideExpenseDatabase(context: Context): ExpenseDatabase {
        return Room.databaseBuilder(
                context,
                ExpenseDatabase::class.java,
                "expense_db"
            ).fallbackToDestructiveMigration(false) // Replace with proper migration in production
            .build()
    }

    @Provides
    @Singleton
    fun provideExpenseDao(database: ExpenseDatabase): ExpenseDao {
        return database.expenseDao()
    }
}
