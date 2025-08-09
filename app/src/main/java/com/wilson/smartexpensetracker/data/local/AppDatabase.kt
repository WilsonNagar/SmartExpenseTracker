package com.wilson.smartexpensetracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wilson.smartexpensetracker.data.local.dao.ExpenseDao
import com.wilson.smartexpensetracker.data.local.entity.ExpenseEntity

@Database(
    entities = [ExpenseEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ExpenseDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
}