package com.wilson.smartexpensetracker.data.local.dao

import androidx.room.*
import com.wilson.smartexpensetracker.data.local.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity)

    @Update
    suspend fun updateExpense(expense: ExpenseEntity)

    @Delete
    suspend fun deleteExpense(expense: ExpenseEntity)

    @Query("SELECT * FROM expenses WHERE date(timestamp / 1000, 'unixepoch') = date('now') ORDER BY timestamp DESC")
    fun getTodayExpenses(): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE date(timestamp / 1000, 'unixepoch') = date(:epochMillis / 1000, 'unixepoch') ORDER BY timestamp DESC")
    fun getExpensesByDate(epochMillis: Long): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses ORDER BY timestamp DESC")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE id = :id LIMIT 1")
    suspend fun getExpenseById(id: String): ExpenseEntity?

    @Query("DELETE FROM expenses")
    suspend fun clearAll()
}
