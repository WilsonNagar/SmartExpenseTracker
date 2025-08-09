package com.wilson.smartexpensetracker.domain.repository

import com.wilson.smartexpensetracker.domain.model.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    suspend fun addExpense(expense: Expense)
    suspend fun updateExpense(expense: Expense)
    suspend fun deleteExpense(expense: Expense)

    fun getTodayExpenses(): Flow<List<Expense>>
    fun getExpensesByDate(epochMillis: Long): Flow<List<Expense>>
    fun getAllExpenses(): Flow<List<Expense>>

    suspend fun getExpenseById(id: String): Expense?
    suspend fun clearAll()
}
